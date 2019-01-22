pipeline {
	agent any
        	stages {
                       script {
								prop = readProperties file:'properties/common.properties'
								configProp = load prop.configFile
								configProp.loadProps()
								configProp.readScanBuild()
								configProp.artifactory()
								configProp.deploy()
								
                             }
                      }    
               
		
   
}
