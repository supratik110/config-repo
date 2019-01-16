pipeline {
     agent any
        stages {
		stage('LOAD PROPERTIES FILES') {
                  steps {
                       script {
				commonProps = readProperties file:'properties/common.properties'
				gitProps = readProperties file:'properties/git.properties'
				deployProps = readProperties file:'properties/deploy.properties'
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
		stage('DEPLOY') {
                  steps {
			deployProps.tomcatDeploy+' '+deployProps.tomcatPath
			deployProps.restartTomcat
				  
            }
    }
   }
}
