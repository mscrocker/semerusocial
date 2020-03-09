pipeline {

	agent {docker "maven:3.6.3-jdk-11-openj9"}



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
						sh (script: 'OUTPUT=$(mvn help:evaluate -Dexpression=
						project.build.finalName -q -DforceStdout --projects frontend).war\
          				&& echo "$OUTPUT"', returnStdout: true)
					def backendArtifactName = 
						sh (script: 'OUTPUT=$(mvn help:evaluate -Dexpression=
						project.build.finalName -q -DforceStdout --projects backend).war\
          				&& echo "$OUTPUT"', returnStdout: true)

					archiveArtifacts 'frontend/target/' + frontendArtifactName + ', backend/target/' + backendArtifactName
				}
				

				
				
			}
		}

		stage('Verify'){
			steps {
				sh 'mvn verify'
			}
		}
	}
}