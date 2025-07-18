pipeline {
    agent {
        label 'local-agent'
    }
    options {
        ansiColor('xterm')
        timestamps()
        timeout(time: 10, unit: 'MINUTES')
    }
    parameters {
        credentials(
            name: 'credentials',
            credentialType: 'com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey',
            required: true,
            description: 'SSH Username with private key from Jenkins Credentials for ssh connection.'
        )
        choice(
            name: 'ansible_limit',
            choices: [
                'ARM',
                'AMD'
            ],
            description: 'Select host group in inventory file.'
        )
        string(
            name: 'ansible_port',
            defaultValue: '22',
            description: 'Port for connecting to remote hosts.'
        )
        activeChoice(
            name: 'containers',
            choiceType: 'PT_CHECKBOX',
            filterable: true,
            filterLength: 1,
            script: [
                $class: 'GroovyScript',
                script: [
                    sandbox: true,
                    script: '''
                        return [
                            'node_exporter',
                            'cadvisor',
                            'logporter',
                            'loki_promtail'
                        ]
                    '''
                ]
            ]
        )
        string(
            name: 'node_exporter_port',
            defaultValue: '9100'
        )
        string(
            name: 'cadvisor_port',
            defaultValue: '8080'
        )
        string(
            name: 'logporter_port',
            defaultValue: '9333'
        )
        string(
            name: 'loki_promtail_port',
            defaultValue: '9080'
        )
        string(
            name: 'loki_server_addr',
            defaultValue: 'http://192.168.3.105'
        )
        string(
            name: 'loki_server_port',
            defaultValue: '3100'
        )
    }
    stages {
        stage('Run Ansible Playbook') {
            steps {
                script {
                    echo "Selected containers: ${params.containers}"
                    def containersArr = params.containers.split(',')
                    def containersArrSelected = [
                        node_exporter: containersArr.contains('node_exporter'),
                        cadvisor: containersArr.contains('cadvisor'),
                        logporter: containersArr.contains('logporter'),
                        loki_promtail: containersArr.contains('loki_promtail')
                    ]
                    echo "Selected containers: $containersArrSelected"
                    ansiblePlaybook(
                        // playbook: "/home/jenkins/workspace/ansible/playbooks/deploy.yml",
                        // inventory: "/home/jenkins/workspace/ansible/inventories/inventory.ini",
                        playbook: "./playbooks/deploy.yml",
                        inventory: "./inventories/inventory.ini",
                        limit: params.ansible_limit,
                        extraVars: [
                            ansible_port: params.ansible_port,
                            node_exporter: containersArrSelected.node_exporter,
                            node_exporter_port: params.node_exporter_port,
                            cadvisor: containersArrSelected.cadvisor,
                            cadvisor_port: params.cadvisor_port,
                            logporter: containersArrSelected.logporter,
                            logporter_port: params.logporter_port,
                            loki_promtail: containersArrSelected.loki_promtail,
                            loki_promtail_port: params.loki_promtail_port,
                            loki_server_addr: params.loki_server_addr,
                            loki_server_port: params.loki_server_port
                        ],
                        credentialsId: credentials,
                        colorized: true
                    )
                }
            }
        }
    }
}