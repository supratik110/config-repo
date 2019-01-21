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
				echo 'LOAD SUCCESS'
                             }
                      }    
               }
		stage('READ GIT') {
                  steps {
                         git url: gitProps.gitUrl,
                         branch: gitProps.branchName
						 
                         echo 'READ SUCCESS'
                        }    
                   }
		stage('SONAR SCAN') {
                  steps {
			dir(gitProps.path)
			{
				sh commonProps.buildSonarScan
				echo 'SONAR SCAN SUCCESS'
			}
                        }    
                   }
		stage('BUILD') {
                  steps {
			dir(gitProps.path)
			{
				sh commonProps.mavenClean
				echo 'BUILD SUCCESS'
			}
                        }    
                   }
		stage('UPLOAD ARTIFACT') {
                  steps {
			script {
				server = Artifactory.server artifactoryProps.artifactServer
				def uploadSpec = """{
                		"files": [
                    		{
                       			"pattern": "/target/*.war",
                        		"target": "demo-java/target"
                    		}
                    		]
                		}"""
            			server.upload(uploadSpec)
				}
				echo 'ARTIFACT SUCCESS'
				}
			}
		stage('DEPLOY') {
                  steps {
		  	script {
						try	{
							sh deployProps.dockerContainerId
							if((output=readFile('result').trim())!=null)
							{
								echo output
								sh deployProps.dockerContainerRm
								
							}
							}catch (err)
							{
							echo 'DELETE FAILED'
							}
							sh deployProps.dockerDeploy
							sh deployProps.dockerRestart
							echo 'DEPLOY SUCCESS'
							
				}
							
				  
            }
    }
   }
}
