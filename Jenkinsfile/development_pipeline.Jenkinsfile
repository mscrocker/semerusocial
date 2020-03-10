pipeline {

    agent {
        docker {
            image "$MAVEN_IMAGE"
            args "$MAVEN_IMAGE_ARGS"
        }
    }

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

                    archiveArtifacts 'frontend/target/' + frontendArtifactName + ', backend/target/' + backendArtifactName
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
                        script {
                            def path = sh(
                                script: '$(pwd)_h2',
                                returnStdout: true
                            )
                            if (fileExists(path)){
                                sh 'rm -r $(pwd)_h2'
                            }
                        }
                        
                        sh 'mkdir $(pwd)_h2'
                        sh 'cp --reflink=always -r $(pwd)/{backend,frontend,benchmark,pom.xml} $(pwd)_h2'
                        sh 'cd $(pwd)_h2'
                        sh 'mvn verify -P h2'
                        
                    },

                    'Verify-mysql': {
                        script {
                            def path = sh(
                                script: '$(pwd)_mysql',
                                returnStdout: true
                            )
                            if (fileExists(path)){
                                sh 'rm -r $(pwd)_mysql'
                            }
                        }
                        sh 'mkdir $(pwd)_mysql'
                        sh 'cp --reflink=always -r $(pwd)/{backend,frontend,benchmark,pom.xml} $(pwd)_mysql'
                        sh 'cp --reflink=always -r $(pwd) $(pwd)_mysql'
                        sh 'cd $(pwd)_mysql'
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
                    image.inside('-u root') {
                        sh 'find .. -user root -name \'*\' | xargs chmod ugo+rw'
                    }
                }
                script {
                    def path = sh(
                        script: '$(pwd)',
                        returnStdout: true
                    )
                    cleanWs(
                        deleteDirs: true,
                        patterns: [
                            [pattern: path, type: 'INCLUDE'],
                            [pattern: path + '_mysql', type: 'INCLUDE'], 
                            [pattern: path + '_h2', type: 'INCLUDE']
                        ]
                    )
                }
            }
        }
    
    }

}