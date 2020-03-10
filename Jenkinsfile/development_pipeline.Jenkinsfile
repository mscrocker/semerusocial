pipeline {

    agent {
        docker {
            image "maven:3.6.3-jdk-11-openj9"
            args "--net=host"
        }
    }

    environment {
        AGENT_GIT = 'alpine/git'
        HOME_PATH = '/var/jenkins_home'
        JAVA_TOOL_OPTIONS = "-Duser.home=${HOME_PATH}"
        MAVEN_DEPENDENCY_PATH = "${HOME_PATH}/.m2"
    }


    stages {
        stage('Restore maven dependencies'){
            steps{
                script{
                    try{
                        unstash 'dependency-cache'
                    }catch(Exception e){

                    }
                }
            }
        }

        stage('Validate'){
            steps {
                sh 'mvn validate'
                stash includes: "$MAVEN_DEPENDENCY_PATH/.m2", name: 'dependency-cache'
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