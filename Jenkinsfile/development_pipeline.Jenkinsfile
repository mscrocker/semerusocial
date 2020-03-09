pipeline {

    agent {
        docker {
            image "maven:3.6.3-jdk-11-openj9"
            args "--net=host"
        }
    }

    environment {
        AGENT_GIT = 'alpine/git'
    }


    stages {


        stage('Validate'){
            steps {
                sh 'mvn validate'
            }
        }

        stage('Compile'){
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test'){
            steps {
                sh 'mvn test'
            }
        }

        stage('Package'){
            steps {
                script {
                    sh 'mvn package'

                    def frontendArtifactName = 
                        sh (script: 'OUTPUT=$(mvn help:evaluate -Dexpression=' + 
                        'project.build.finalName -q -DforceStdout --projects frontend).war' +
                          '&& echo "$OUTPUT"', returnStdout: true)
                    def backendArtifactName = 
                        sh (script: 'OUTPUT=$(mvn help:evaluate -Dexpression=' +
                        'project.build.finalName -q -DforceStdout --projects backend).war' +
                          '&& echo "$OUTPUT"', returnStdout: true)

                    archiveArtifacts 'frontend/target/' + frontendArtifactName + ', backend/target/' + backendArtifactName
                }
                

                
                
            }
        }
        stage('Parallel verify and benchmark'){
            parallel {
                stage('Verify-h2'){
                    steps {
                        sh 'mvn verify -P h2'
                    }
                }

                stage('Verify-mysql'){
                    steps {
                        sh 'mvn verify -P mysql'
                    }
                }

                stage('Benchmark'){
                    steps {
                        sh 'mvn verify -P h2,benchmark'
                    }
                }
            }
        }
        
    }
}