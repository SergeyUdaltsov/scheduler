#!groovy

pipeline {
    agent any

    parameters {
        choice(name: 'stack', choices: 'dev\ntest\nprod', description: 'Stack to deploy')
        choice(name: 'action', choices: 'deploy\ndestroy', description: 'Deployment action')
        booleanParam(name: 'deployOverride', defaultValue: false, description: 'Used for deploying non-main branches')
    }

    stages {
        stage('Build') {
            steps {
                echo "Action: ${params.action}, stack: ${params.stack}"
                sh 'mvn clean install'

            }

        }
    }

}