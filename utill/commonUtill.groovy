pipeline {
	agent any
        	stages {
                       stage('PRE LOAD') {
                  	steps {
			prop = readProperties file:'properties/common.properties'
			configProp = load prop.configFile
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
			configProp.read()
					}
				}
			stage('SONAR SCAN') {
                  steps {
			configProp.scan()
				}
			}
			stage('BUILD') {
                  steps {
			configProp.build()
				}
			}
			stage('UPLOAD ARTIFACT') {
                  steps {
			configProp.artifactory()
				}
			}
			stage('DEPLOY') {
                  steps {
			configProp.deploy()						
                }         
            }    
     	}
}
