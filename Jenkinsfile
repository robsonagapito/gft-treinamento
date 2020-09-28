pipeline {
    agent any
    tools {
            maven 'maven'
            jdk 'java'
    }
    stages {
		stage('Install Dependencies'){
			steps{
				 script {
					if (isUnix()) {
							 sh 'docker pull elgalu/selenium'
							 sh 'docker pull dosel/zalenium'
						} else {
							 bat 'docker pull elgalu/selenium'
							 bat 'docker pull dosel/zalenium'
						}
				   }
			   }
		   }
        stage ('Start Zalenium'){
            steps{
                script {
                    if (isUnix()) {
                        sh 'docker run --rm -ti --name zalenium -d -p 4444:4444 -e PULL_SELENIUM_IMAGE=true -v /var/run/docker.sock:/var/run/docker.sock -v /tmp/videos:/home/seluser/videos --privileged dosel/zalenium start'
                    } else {
                        bat 'docker run --rm -ti --name zalenium -d -p 4444:4444 -e PULL_SELENIUM_IMAGE=true -v /var/run/docker.sock:/var/run/docker.sock -v /tmp/videos:/home/seluser/videos --privileged dosel/zalenium start'
                    }
                }
            }
        }
        stage ('Run Tests'){
			steps{
				script {
					if (isUnix()) {
						sh 'mvn clean test -Denv=des -Dbrowser=chrome'
					} else {
						bat 'mvn clean test -Denv=des -Dbrowser=chrome'
					}
				}
			}
        }
        stage ('Generate Allure Reports'){
            steps{
                script {
                    allure([
                            includeProperties: false,
                            jdk: '',
                            properties: [],
                            reportBuildPolicy: 'ALWAYS',
                            results: [[path: 'target/allure-results']]
                    ])
                }
            }
         }
        stage ('Stop Zalenium'){
            steps{
                script {
                    if (isUnix()) {
                            sh 'docker stop zalenium'
                        } else {
                            bat 'docker stop zalenium'
                    }
                 }
            }
        }
     }
    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/xml-junit/*.xml'
            archiveArtifacts artifacts: 'target/log/*.log'
        }
    }
}