pipeline {
    agent {label 'Astra'}
    
    stages {
        stage('Build') {
            steps {
                bash create_tomcat_image.bat
            }
        }
        stage('Deploy') {
            steps {
                bash run_docker_web-server.bat
            }
        }
    }
}