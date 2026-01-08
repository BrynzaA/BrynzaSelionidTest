pipeline {
    agent any

    stages {
        stage('Check Tools') {
            steps {
                script {
                    def dockerCheck = bat(script: 'docker --version', returnStdout: true).trim()
                    echo "Docker version: ${dockerCheck}"

                    def mavenCheck = bat(script: 'mvn --version 2>nul || echo "Maven not found"', returnStdout: true).trim()
                    echo "Maven check: ${mavenCheck}"
                }
            }
        }

        stage('Run Full Test Suite') {
            steps {
                script {
                    echo 'Запускаем start-all.bat...'
                    bat 'start-all.bat'
                }
            }
        }
    }

    post {
        always {
            script {
                if (fileExists('test-results')) {
                    archiveArtifacts artifacts: 'test-results/**/*', fingerprint: true
                    junit 'test-results/surefire-reports/*.xml'
                }
                if (fileExists('selenoid-videos')) {
                    archiveArtifacts artifacts: 'selenoid-videos/**/*', allowEmptyArchive: true
                }
            }

            bat 'docker stop selenoid selenoid-ui 2>nul || echo "No containers to clean"'
        }
    }
}