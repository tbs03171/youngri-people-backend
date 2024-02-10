pipeline {
    agent any
    stages {
        stage('Git Clone') {
            steps {
                git branch: 'master', url: 'https://github.com/tbs03171/youngri-people-backend.git'
            }
        }
        stage('BE-Build') {
            steps {
                sh "chmod +x gradlew"
                sh "./gradlew clean build"
            }
        }
        stage('Deploy') {
            steps {
                sh "sudo /var/lib/jenkins/workspace/deploy.sh"
            }
        }
    }
}
