pipeline {
    agent any

    tools {
        maven 'M3'
        jdk 'jdk11'
    }

    environment {
        DOCKER_REGISTRY = ''
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository -Dmaven.test.failure.ignore=true'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'CI4',
                    url: 'https://github.com/BrynzaA/BrynzaSelionidTest.git',
                    credentialsId: ''
            }
        }

        stage('Build and Run with Docker') {
            steps {
                script {
                    sh 'docker stop selenoid selenoid-ui 2>/dev/null || true'
                    sh 'docker rm selenoid selenoid-ui 2>/dev/null || true'

                    bat 'start-all.bat'
                }
            }
        }

        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'test-results/**/*', fingerprint: true
                archiveArtifacts artifacts: 'selenoid-videos/**/*', allowEmptyArchive: true
                junit 'test-results/surefire-reports/*.xml'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'test-results/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            sh 'docker stop selenoid selenoid-ui 2>/dev/null || true'
            echo 'Pipeline execution completed. Cleanup done.'
        }
        success {
            echo 'Все тесты успешно выполнены!'
        }
        failure {
            echo ' В пайплайне произошла ошибка.'
        }
    }
}