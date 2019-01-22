pipeline {
	agent any
        	stages {
                       
			prop = readProperties file:'properties/common.properties'
			configProp = load prop.configFile
			configProp.loadProps()
			configProp.readScanBuild()
			configProp.artifactory()
			configProp.deploy()						
                         
                      }    
     	}
