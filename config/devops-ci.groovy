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
					script{
						try{
							dir(gitProps.path){
								sh commonProps.mavenClean
								currentBuild.result = "SUCCESSFUL"
								echo 'BUILD SUCCESS'
								}
							}
						catch (e) {
							currentBuild.result = "FAILED"
							throw e
								} 
						finally {
							notifyBuild(currentBuild.result)
								}
							} 
						}
					}
		stage('UPLOAD ARTIFACT') {
                  steps {
			script {
				server = Artifactory.server artifactoryProps.artifactServer
				uploadSpec = """{
                		"files": [
                    		{
                       			"pattern": "target/*.war",
                        		"target": "demo-java/target/"
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
							output=readFile('result').trim()
							if(output!=null)
							{
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
def notifyBuild(String buildStatus)
{
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"
  def details = """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at "<a href="${env.BUILD_URL}">${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>"</p>"""
	
  if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }
 slackSend (color: colorCode, message: summary)
 hipchatSend (color: color, notify: true, message: summary)
 emailext (
      subject: subject,
      body: details,
      recipientProviders: [[$class: 'DevelopersRecipientProvider']]
    )
 
}
