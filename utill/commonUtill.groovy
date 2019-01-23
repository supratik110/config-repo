pipeline {
	agent any
        	stages {
                       stage('PRE LOAD') {
                  	steps {
			script {
			prop = readProperties file:'properties/common.properties'
			configProp =prop.configFile
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
