pipeline {
	agent any
        	stages {
			stage('LOAD PROPERTIES FILES') {
                  steps {
                       script {
		       	commonProps = readProperties file:'properties/common.properties'
			echo 'LOAD SUCCESS1'
			gitProps = readProperties file:'properties/git.properties'
			echo 'LOAD SUCCESS2'
			deployProps = readProperties file:'properties/deploy.properties'
			echo 'LOAD SUCCESS3'
			artifactoryProps = readProperties file:'properties/artifactory.properties'
			echo 'LOAD SUCCESS4'
			configProp = load commonProps.configFile
			echo 'LOAD SUCCESS5'
								}
							}
						}
			stage('READ GIT') {
                  steps {
		  	script {
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
