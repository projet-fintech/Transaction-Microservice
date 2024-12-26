pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'ser3elah', url: 'https://github.com/mouad4949/GHE_KNJRB_OSF.git']]
                )
            }
        }
        stage('Build') {
            steps {
                dir('jenkins') { // Assure-toi que c'est le bon répertoire
                    sh 'mvn clean install -DskipTests=true'
                }
            }
        }
}

