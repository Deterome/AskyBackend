pipeline {
    agent {label 'Astra'}
    
    stages {
        stage('Build') {
            steps {
                ./create_tomcat_image.bat
            }
        }
        stage('Deploy') {
            steps {
                ./run_docker_web-server.bat
            }
        }
    }
}