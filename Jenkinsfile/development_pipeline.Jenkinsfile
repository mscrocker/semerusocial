pipeline {

    agent none

    environment {
        AGENT_GIT = 'alpine/git'
        MAVEN_IMAGE = 'maven:3.6.3-jdk-11-openj9'
        MAVEN_IMAGE_ARGS = '--net=host'
        EMAIL_RECEIVER = 'marco.martinez.sanchez98@gmail.com'
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
                sh 'mvn validate > validate_out.txt'
                archiveArtifacts 'validate_out.txt'
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
                sh 'mvn compile > compile_out.txt'
                archiveArtifacts 'compile_out.txt'
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
                sh 'mvn test > test_out.txt'
                archiveArtifacts 'test_out.txt'
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
                    sh 'mvn package > package_out.txt'

                    def frontendArtifactName = 
                        sh (script: 'OUTPUT=$(mvn help:evaluate -Dexpression=' + 
                        'project.build.finalName -q -DforceStdout --projects frontend).war' +
                          '&& echo "$OUTPUT"', returnStdout: true)
                    def backendArtifactName = 
                        sh (script: 'OUTPUT=$(mvn help:evaluate -Dexpression=' +
                        'project.build.finalName -q -DforceStdout --projects backend).war' +
                          '&& echo "$OUTPUT"', returnStdout: true)

                    archiveArtifacts 'package_out.txt'
                    archiveArtifacts 'frontend/target/' + frontendArtifactName + ', backend/target/' + backendArtifactName
                }
                
                
            }
        }
        stage('Verify'){
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
                        sh 'cp -r $(pwd)/backend h2_build'
                        sh 'cp -r $(pwd)/frontend h2_build'
                        sh 'cp -r $(pwd)/benchmark h2_build'
                        sh 'cp -r $(pwd)/pom.xml h2_build'
                        sh 'cd h2_build && mvn verify -P h2 > ../verify_h2_out.txt'
                        archiveArtifacts 'verify_h2_out.txt'
                    },

                    'Verify-mysql': {
                        sh 'mkdir mysql_build'
                        sh 'cp -r $(pwd)/backend mysql_build'
                        sh 'cp -r $(pwd)/frontend mysql_build'
                        sh 'cp -r $(pwd)/benchmark mysql_build'
                        sh 'cp -r $(pwd)/pom.xml mysql_build'
                        sh 'cd mysql_build && mvn verify -P mysql > ../verify_mysql_out.txt'
                        archiveArtifacts 'verify_mysql_out.txt'
                    }
                )
            }
        }

        stage('Benchmark'){
            agent {
                docker {
                    image "$MAVEN_IMAGE"
                    args "$MAVEN_IMAGE_ARGS"
                }
            }
            steps {
                script {
                    try {
                        timeout(time: 1, unit: 'MINUTES') {
                            sh 'mvn verify -P h2,benchmark > benchmark_out.txt'
                        }
                    } catch(error){
                        sh 'echo "Benchmark aborted by timeout!" >> benchmark_out.txt'
                    }
                    
                    archiveArtifacts 'benchmark_out.txt'
                }
            }    
        }
        
    }
    post {
    
        always {
            node ('master') {
                script {
                    sh 'tar -cvzf reports.tar.gz *_out.txt'
                    emailext (
                        attachmentsPattern: 'reports.tar.gz',
                        body: 'The build ' + env.JOB_NAME + ' has completed with status: ' + currentBuild.result, 
                        subject: 'Build completed', 
                        from: 'notificaciones.torusnewies@gmail.com',
                        replyTo: '',
                        to: "$EMAIL_RECEIVER")
                    cleanWs()
                }

            }
        }
    
    }

}