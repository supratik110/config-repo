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
			configProp.loadProps()
			configProp.readScanBuild()
			configProp.artifactory()
			configProp.deploy()						
                         
                      }    
     	}
