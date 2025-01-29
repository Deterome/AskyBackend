pipeline {
    agent {label 'Astra'}
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') { 
            steps {
                sh 'mvn test' 
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' 
                }
            }
        }
        stage('Deploy') {
            steps {
                sh './run_docker_web-server.bat'
                sh './create_tomcat_image.bat'
            }
        }
    }
}