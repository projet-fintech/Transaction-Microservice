pipeline {
    agent any
    tools {
        maven 'Maven' // Remplace 'Maven_Auto' par le nom de ton installation Maven
    }
    stages {
        stage('Checkout') {
    steps {
         sh 'git config --global --unset http.proxy'
        sh 'git config --global --unset https.proxy'
        checkout scmGit(
            branches: [[name: '*/main']],
            extensions: [],
            userRemoteConfigs: [[credentialsId: 'ser3elah', url: 'https://github.com/projet-fintech/Transaction-Microservice.git']]
        )
    }
}
        stage('Build') {
            steps {
                dir('Backend') {
                    sh 'mvn clean install -DskipTests=true'
                }
            }
        }
   }
}
