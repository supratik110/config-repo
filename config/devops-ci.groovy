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
					server = Artifactory.server 'artifactoryProps.artifactServer'
					 uploadSpec = """{
						"files": [
						{
                        "pattern": artifactoryProps.uploadSpecSource,
                        "target": artifactoryProps.uploadSpecTarget
						}
						]
						}"""
					server.upload(uploadSpec)
				  }
				}
			}
		stage('DEPLOY') {
                  steps {
		  	sh deployProps.dockerStop
			sh deployProps.tomcatDeploy
			sh deployProps.restartTomcat
			echo 'DEPLOY SUCCESS'
				  
            }
    }
   }
}
