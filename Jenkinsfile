pipeline {
    agent any
    tools {
        maven 'Maven'
         jdk 'JAVA21'
    }
    stages {
       stage('Checkoutt') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'ser3elah', url: 'https://github.com/projet-fintech/Transaction-Microservice.git']]
                )
            }
        }
        stage('Build') {
            steps {
                dir('Backend') { // Assure-toi que c'est le bon répertoire
                    sh 'mvn clean install -DskipTests=true'
                }
            }
            }
        }
    }
