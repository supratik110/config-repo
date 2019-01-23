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
			configProp = load commonProps.configFile
			echo 'LOAD SUCCESS'
				}
			}
			}
			stage('READ GIT') {
                  steps {
		  	script {
			git url: gitProps.gitUrl,
			branch: gitProps.branchName
			echo 'READ SUCCESS'
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
			stage("INPUT") {
            	steps {
                	script {
                    	env.RELEASE_SCOPE = input message: 'User input required', ok: 'Release!',
                            parameters: [choice(name: 'RELEASE_SCOPE', choices: 'patch\nminor\nmajor', description: 'What is the release scope?')]
                		}
                	echo "${env.RELEASE_SCOPE}"
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
