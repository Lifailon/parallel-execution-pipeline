def remote = [:]

pipeline {
    agent any
    options {
        ansiColor('xterm')
        timestamps()
        timeout(time: 10, unit: 'MINUTES')
    }
    parameters {
        string(
            name: 'stackName',
            defaultValue: 'jenkins-agent',
            description: 'Directory name or full path to store the stack.'
        )
        booleanParam(
            name: "build",
            defaultValue: true,
            description: 'Use pre-build (pass path in "urlDockerfile" parameter).'
        )
        string(
            name: 'urlDockerfile',
            defaultValue: 'https://raw.githubusercontent.com/Lifailon/parallel-execution-pipeline/refs/heads/main/jenkins-agent/Dockerfile',
            description: 'URL of Dockerfile for build.'
        )
        string(
            name: "urlDockerCompose",
            defaultValue: "https://raw.githubusercontent.com/Lifailon/parallel-execution-pipeline/refs/heads/main/jenkins-agent/docker-compose.yml",
            description: 'URL of docker-compose.yml file for deployment.'
        )
        text(
            name: 'envParams',
            defaultValue: 'JENKINS_SERVER_URL=http://192.168.3.105:8080\nJENKINS_AGENT_NAME=agent-02\nJENKINS_SECRET=',
            description: 'List of environment variables for .env file generation.'
        )
        text(
            name: 'commands',
            defaultValue: 'mkdir -p ./jenkins-agent/jenkins_agent\nsudo chown -R 1000:1000 ./jenkins-agent/jenkins_agent',
            description: 'List of commands before build.'
        )
        string(
            name: 'address',
            defaultValue: '192.168.3.106',
            description: 'Remote host address for deployment.'
        )
        credentials(
            name: 'credentials',
            description: 'SSH Username with private key from Jenkins Credentials for ssh connection.',
            credentialType: 'com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey',
            required: true
        )
        string(
            name: 'user',
            defaultValue: '',
            description: 'Username for ssh connection (by default from credentials parameter).'
        )
        string(
            name: 'port',
            defaultValue: '',
            description: 'Port for ssh connection (by default 22).'
        )
    }
    environment {
        SSH_KEY_FILE = "/tmp/ssh_key_${UUID.randomUUID().toString()}"
    }
    stages {
        stage('Get ssh credentials') {
            when {
                expression { params.address && params.credentials }
            }
            steps {
                script {
                    withCredentials(
                        [
                            sshUserPrivateKey(
                                credentialsId: params.credentials,
                                usernameVariable: 'SSH_USER',
                                keyFileVariable: 'SSH_KEY',
                                passphraseVariable: ''
                            )
                        ]
                    ) {
                        writeFile(
                            file: env.SSH_KEY_FILE,
                            text: readFile(SSH_KEY)
                        )
                        sh "chmod 600 ${env.SSH_KEY_FILE}"
                        remote.name = params.address
                        remote.host = params.address
                        remote.user = params.user ? params.user : SSH_USER
                        remote.port = params.port ? params.port.toInteger() : 22
                        remote.identityFile = env.SSH_KEY_FILE
                        remote.allowAnyHosts = true
                    }
                    echo "SSH username: ${remote.user}"
                    echo "SSH port: ${remote.port}"
                }
            }
        }
        stage('Build and Deploy container') {
            when {
                expression { params.address && params.credentials && params.urlDockerfile && params.urlDockerCompose }
            }
            steps {
                script {
                    sshCommand remote: remote, command: "mkdir -p ${params.stackName}"
                    sshCommand remote: remote, command: "curl -sSL ${params.urlDockerCompose} -o ${params.stackName}/docker-compose.yml"
                    sshCommand remote: remote, command: "echo '${params.envParams}' > ${params.stackName}/.env"
                    def commandsArr = params.commands.split('\n')
                    for (command in commandsArr) {
                        sshCommand remote: remote, command: command
                    }
                    if (params.build) {
                        sshCommand remote: remote, command: "curl -sSL ${params.urlDockerfile} -o ${params.stackName}/Dockerfile"
                        sshCommand remote: remote, command: "cd ${params.stackName} && bash -l -c 'docker-compose up -d --build'"
                    } else {
                        sshCommand remote: remote, command: "cd ${params.stackName} && bash -l -c 'docker-compose up -d'"
                    }
                }
            }
        }
    }
}