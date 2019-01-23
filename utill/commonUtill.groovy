pipeline {
	agent any
        	stages {
		stage('LOAD PROPERTIES FILES') {
                  steps {
                       script {
		       	commonProps = readProperties file:'properties/common.properties'
			gitProps = readProperties file:'properties/git.properties'
			deployProps = readProperties file:'properties/deploy.properties'
			artifactoryProps = readProperties file:'properties/artifactory.properties'
			configProp = readProperties file:'${commonProps.configFile}'
			echo 'LOAD SUCCESS'
				}
			}
			}
			stage('READ GIT') {
                  steps {
		  	script {
			echo 'READ SUCCESS'
			configProp.read()
				}
					}
				}
			stage('SONAR SCAN') {
                  steps {
		  	script {
			configProp.scan()
				}
				}
			}
			stage('BUILD') {
                  steps {
		  	script {
			configProp.build()
			}
				}
			}
			stage('UPLOAD ARTIFACT') {
                  steps {
		  	script {
			configProp.artifactory()
			}
				}
			}
			stage('DEPLOY') {
                  steps {
		  	script {
			configProp.deploy()
			}
                }         
            }    
     	}
}
