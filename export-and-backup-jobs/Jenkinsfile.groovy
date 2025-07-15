import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.http.HttpHeaders
import groovy.json.JsonSlurper

def getCredScript(credentials) {
    def creds = com.cloudbees.plugins.credentials.CredentialsProvider
        .lookupCredentials(
            com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials.class,
            Jenkins.instance
        )
        .find { it.id == credentials }
    if (!creds) {
        return ["Credentials not found"]
    }
    def username = creds.username
    def password = creds.password.plainText
    return [
        username,
        password
    ]
}

def getCredPipeline(credentials) {
    def username = ""
    def password = ""
    withCredentials([usernamePassword(
        credentialsId: credentials,
        usernameVariable: 'USERNAME',
        passwordVariable: 'PASSWORD'
    )]) {
        username = env.USERNAME
        password = env.PASSWORD
    }
    return [username, password]
}

def getJobs(jenkinsUrl,username,password) {
    try {
        def authString = "${username}:${password}".bytes.encodeBase64().toString()
        def authHeader = "Basic ${authString}"
        def client = HttpClients.createDefault()
        def getRequest = new HttpGet("${jenkinsUrl}/api/json?tree=jobs[name]")
        getRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader)
        def response = client.execute(getRequest)
        try {
            def json = EntityUtils.toString(response.getEntity())
            def slurper = new JsonSlurper()
            def jobsList = slurper.parseText(json)
            def jobsListName = jobsList.jobs.name
            return jobsListName
        } 
        finally {
            response.close()
            client.close()
        }
    }
    catch(Exception err) {
        return "${err.message}"
    }
}

def exportJobConfigs(jenkinsUrl, username, password, jobsList, exportPath) {
    def authString = "${username}:${password}".bytes.encodeBase64().toString()
    def authHeader = "Basic ${authString}"
    def exportDir = new File(exportPath)
    if (!exportDir.exists()) {
        exportDir.mkdirs()
    }
    jobsList.each { jobName ->
        try {
            def client = HttpClients.createDefault()
            def jenkinsJobName = jobName.replace(' ', '%20')
            def getRequest = new HttpGet("${jenkinsUrl}/job/${jenkinsJobName}/config.xml")
            getRequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader)
            def response = client.execute(getRequest)
            try {
                if (response.statusLine.statusCode == 200) {
                    def configXml = EntityUtils.toString(response.getEntity())
                    def safeFileName = jobName.replaceAll('[\\\\/:*?"<>| ]', '_')
                    def exportFile = new File(exportDir, "${safeFileName}.xml")
                    exportFile.write(configXml)
                    println "Successfully export '${jobName}' to ${exportFile.absolutePath}"
                } else {
                    println "Failed export '${jobName}': HTTP ${response.statusLine.statusCode}"
                }
            } 
            finally {
                response.close()
                client.close()
            }
        }
        catch(Exception err) {
            println "Failed export '${jobName}': ${err.message}"
        }
    }
}

// def jenkinsUrl = "http://192.168.3.105:8080"
// def credentials = "15d05be6-682a-472b-9c1d-cf5080e98170"
// if (!jenkinsUrl) {
//     return ["Jenkins url not set"]
// }
// def creds = getCredScript(credentials)
// def username = creds[0]
// def password = creds[1]
// if (!username || !password) {
//     return ["Credentials not found"]
// }
// def jobsList = getJobs(jenkinsUrl,username,password)
// if (!(jobsList instanceof List)) {
//     return [jobsList]
// }
// def exportPath = "/var/jenkins_home/jobs-backup/"
// exportJobConfigs(jenkinsUrl, username, password, jobsList, exportPath)

pipeline {
    agent any
    triggers {
        cron('30 23 * * 1-6')
    }
    options {
        timeout(time: 10, unit: 'MINUTES')
    }
    parameters {
        string(
            name: 'jenkinsUrl',
            defaultValue: 'http://192.168.3.105:8080'
        )
        credentials(
            name: 'credentials',
            credentialType: 'Username with password	',
            description: 'Username with password from Jenkins Credentials for API connection.'
        )
        booleanParam(
            name: "export",
            defaultValue: false,
            description: 'Export config in artifacts.'
        )
    }
    stages {
        stage('Backup') {
            steps {
                script {
                    if (!params.jenkinsUrl) {
                        echo "Jenkins url not set"
                        return
                    }
                    def creds = getCredPipeline(params.credentials)
                    def username = creds[0]
                    def password = creds[1]
                    if (!username || !password) {
                        echo "Credentials not found"
                        return
                    }
                    def jobsList = getJobs(params.jenkinsUrl,username,password)
                    if (!(jobsList instanceof List)) {
                        echo jobsList
                        return
                    }
                    def exportPath = ""
                    if (params.export) {
                        exportPath = "${env.WORKSPACE}/jobs-backup/"
                    } else {
                        exportPath = "/var/jenkins_home/jobs-backup/"
                    }
                    exportJobConfigs(params.jenkinsUrl, username, password, jobsList, exportPath)
                }
            }
        }
        stage('Export') {
            when {
                expression { params.export }
            }
            steps {
                archiveArtifacts artifacts: "jobs-backup/*", allowEmptyArchive: true
            }
        }
    }
}