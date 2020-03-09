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