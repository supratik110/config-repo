pipeline {
	agent any
        	stages {
                       stage('PRE LOAD') {
                  	steps {
			script {
			commonProps = readProperties file:'properties/common.properties'
			configProp = load commonProps.configFile
			}
			}
			}
			stage('LOAD PROPERTIES FILES') {
                  steps {
                       script {
								configProp.loadProps()
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
