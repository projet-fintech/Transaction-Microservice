pipeline {
    agent any
       tools {
           maven 'maven'
           jdk 'Java'
           dockerTool 'Docker'
       }
       environment {
               AWS_REGION = 'eu-west-3'
               ECR_REGISTRY = {ecr_registry}
               IMAGE_NAME = 'transaction'
           }
    stages {
       stage('Checkout') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'ser3elah', url: 'https://github.com/projet-fintech/Transaction-Microservice.git']]
                )
            }
        }
         stage('Prepare Dependencies') {
                    steps {
                        script {
                            sh '''
                                mkdir -p repo/com/banque/events-lib/1.0-SNAPSHOT
                                cp /var/jenkins_home/shared-artifacts/repo/com/banque/events-lib/1.0-SNAPSHOT/events-lib-1.0-SNAPSHOT.jar ./events-lib-1.0-SNAPSHOT.jar
                            '''
                        }
                    }
                }

               stage('Unit Tests') {
                    steps {
                        script {

                          sh '''

                          mvn test'''
                        }
                    }
                }

                  stage('Build Docker Image') {
                    steps {
                        script {
                            def localImageName = "${IMAGE_NAME}:${BUILD_NUMBER}"
                            sh "docker build -t ${localImageName} ."
                        }
                    }
                }

                stage('Push to ECR') {
                    steps {
                        script {
                            withCredentials([aws(credentialsId: 'aws-credentials')]) {

                                def awsCredentials = "-e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}"

                                docker.image('amazon/aws-cli').inside("--entrypoint='' ${awsCredentials}") {
                                    sh """
                                        aws ecr get-login-password --region ${AWS_REGION} > ecr_password.txt
                                    """
                                }

                                // Login à ECR
                                sh "cat ecr_password.txt | docker login --username AWS --password-stdin ${ECR_REGISTRY}"
                                sh "rm ecr_password.txt"

                                // Tag et push des images
                                def localImageName = "${IMAGE_NAME}:${BUILD_NUMBER}"
                                def ecrImageLatest = "${ECR_REGISTRY}/${IMAGE_NAME}:latest"
                                def ecrImageVersioned = "${ECR_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}"

                                sh """
                                    docker tag ${localImageName} ${ecrImageLatest}
                                    docker tag ${localImageName} ${ecrImageVersioned}
                                    docker push ${ecrImageLatest}
                                    docker push ${ecrImageVersioned}
                                """
                            }
                        }
                    }
                }

                 stage('Cleanup') {
                    steps {
                        script {
                            sh """
                                docker rmi ${IMAGE_NAME}:${BUILD_NUMBER} || true
                                docker rmi ${ECR_REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} || true
                                docker rmi ${ECR_REGISTRY}/${IMAGE_NAME}:latest || true
                            """
                        }
                    }
                }
                stage('Deploy to EKS') {
                    steps {
                        script {
                            withCredentials([aws(credentialsId: 'aws-credentials')]) {
                                sh """
                                    aws eks --region ${AWS_REGION} update-kubeconfig --name main-eks-cluster
                                    kubectl apply -f kubernetes/transaction-deployement.yaml
                                    kubectl apply -f kubernetes/transacation-service.yaml
                                """
                            }
                        }
                    }
                }
            }
            post {
                failure {
                    echo 'Pipeline failed! Cleaning up...'
                    sh 'rm -f ecr_password.txt || true'
                }
                success {
                    echo 'Pipeline succeeded! Image pushed to ECR.'
                }
                always {
                    cleanWs()
                }
            }
        }
