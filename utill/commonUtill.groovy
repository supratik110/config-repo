pipeline {
	agent any
        	stages {
                       script {
								prop = readProperties file: 'Properties/pipelineFile.properties'
								bat prop.javaConfigUrl
								configProp = load prop.configFile
								configProp.loadProps()
								configProp.readScanBuild()
								configProp.artifactory()
								configProp.deploy()
								
                             }
                      }    
               
		
   
}
