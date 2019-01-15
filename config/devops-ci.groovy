pipeline {
     agent any
        stages {
           stage('load properties file..') {
                  steps {
                       script {
                             commonProps = readProperties file:'properties/common.properties'
			     gitProps = readProperties file:'properties/git.properties'
                             echo 'LOAD SUCCESS'
                             }
                      }    
               }
               stage('read git url file..') {
                  steps {
                         git url: gitProps.gitUrl,
                         branch: gitProps.branchName
						 
                         echo 'READ SUCCESS'
                        }    
                   }
              stage('sonar scan..') {
                  steps {
						 dir(gitProps.path)
						 {
							sh commonProps.buildSonarScan
							echo 'SONAR SCAN SUCCESS'
						 }
                        }    
                   }
              stage('maven build..') {
                  steps {
						 dir(gitProps.path)
						 {
							sh commonProps.mavenClean
							echo 'BUILD SUCCESS'
						 }
                        }    
                   }    
            }
    }
