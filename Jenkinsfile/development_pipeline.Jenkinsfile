pipeline {

    agent none

    environment {
        AGENT_GIT = 'alpine/git'
        MAVEN_IMAGE = 'maven:3.6.3-jdk-11-openj9'
        MAVEN_IMAGE_ARGS = '--net=host'
    }


    stages {

        stage('Validate'){
            agent {
                docker {
                    image "$MAVEN_IMAGE"
                    args "$MAVEN_IMAGE_ARGS"
                }
            }
            steps {
                sh 'mvn validate'
            }
        }

        stage('Compile'){
            agent {
                docker {
                    image "$MAVEN_IMAGE"
                    args "$MAVEN_IMAGE_ARGS"
                }
            }
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test'){
            agent {
                docker {
                    image "$MAVEN_IMAGE"
                    args "$MAVEN_IMAGE_ARGS"
                }
            }
            steps {
                sh 'mvn test'
            }
        }

        stage('Package'){
            agent {
                docker {
                    image "$MAVEN_IMAGE"
                    args "$MAVEN_IMAGE_ARGS"
                }
            }
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

                    // TODO: re-enable artifacts archiving
                    //archiveArtifacts 'frontend/target/' + frontendArtifactName + ', backend/target/' + backendArtifactName
                }
                

                
                
            }
        }
        stage('Parallelize verify with h2 and mysql'){
            agent {
                docker {
                    image "$MAVEN_IMAGE"
                    args "$MAVEN_IMAGE_ARGS"
                }
            }
            steps {
                parallel(
                    'Verify-h2': {
                        sh 'mkdir h2_build'
                        sh 'cp --reflink=always -r $(pwd)/{backend,frontend,benchmark,pom.xml} h2_build'
                        sh 'cd h2_build'
                        sh 'mvn verify -P h2'
                        
                    },

                    'Verify-mysql': {
                        sh 'mkdir $(pwd)_mysql'
                        sh 'cp --reflink=always -r $(pwd)/{backend,frontend,benchmark,pom.xml} mysql_build'
                        sh 'cd mysql_build'
                        sh 'mvn verify -P mysql'
                    }
                )
            }
        }

        stage('Benchmark'){
            steps {

                sh 'mvn verify -P h2,benchmark'
            }
        }
        
    }
    post {
    
        always {
            node ('master') {
                script {
                    cleanWs()
                }
            }
        }
    
    }

}