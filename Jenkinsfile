pipeline {
    agent any
    stages {
        steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'ser3elah', url: 'https://github.com/projet-fintech/Transaction-Microservice.git']]
                )
            }
         stage('Build JAR') {
            steps {
                 sh "mvn clean install -Dspring.profiles.active=test"
            }
         }
    }

