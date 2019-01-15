pipeline {
     agent any
        stages {
           stage('load properties file..') {
                  steps {
                       script {
                             props = readProperties file:'properties/common.properties'
                             echo 'LOAD SUCCESS'
                             }
                      }    
               }
               stage('read git url file..') {
                  steps {
                         git url: props.gitUrl,
                         branch: props.branchName
                         echo 'READ SUCCESS'
                        }    
                   }
              stage('sonar scan..') {
                  steps {
                         sh props.buildSonarScan
                         echo 'SONAR SCAN SUCCESS'
                        }    
                   }
              stage('maven build..') {
                  steps {
                         sh props.mavenClean
                         echo 'BUILD SUCCESS'
                        }    
                   }    
            }
    }
