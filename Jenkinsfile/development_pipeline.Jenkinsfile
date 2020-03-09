pipeline {
	environment {
		AGENT_MAVEN = '3.6.3-jdk-11-openj9'
	}

	agent {docker "$AGENT_MAVEN"}



	stages {
		stage('Get latest code revision'){
			steps {
				sh 'git checkout FETCH_HEAD'
			}
		}

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
				sh 'mvn package'
			}
		}

		stage('Verify'){
			steps {
				sh 'mvn verify'
			}
		}
	}
}