node{
    stage("Git") {
    // coping from git
    git 'https://github.com/supratik110/SupratikDevops301.git'
    }
    stage('Building SONAR ...'){
    def project_path= "Application/"
    dir(project_path) {
    // some block
    withSonarQubeEnv('My SonarQube Server') {
      // requires SonarQube Scanner for Maven 3.2+
      sh 'mvn clean package org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
    }
    archiveArtifacts 'sampleWebApp/target/*.war'
    }
}
}
