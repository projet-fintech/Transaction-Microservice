pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage('Checkout') {
            steps {
              sh 'git config --global --unset http.proxy'
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'ser3elah', url: 'https://github.com/projet-fintech/Transaction-Microservice.git']]
                )
            }
        }
        stage('Build') {
            steps {
             withMaven(maven: 'Maven') {
                dir('Backend') {
                  sh 'mvn clean install -DskipTests=true'
                }
              }
            }
        }
    }
}