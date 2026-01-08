pipeline {
    agent any

    stages {
        stage('Run Tests') {
            steps {
                script {
                    echo 'Запускаем start-all.bat...'

                    def dockerCheck = bat(script: 'docker --version', returnStdout: true).trim()
                    echo "Docker version: ${dockerCheck}"

                    bat 'start-all.bat'
                }
            }
        }
    }

    post {
        always {
            script {
                if (fileExists('test-results')) {
                    echo 'Архивация результатов тестов...'
                    archiveArtifacts artifacts: 'test-results/**/*', fingerprint: true

                    if (fileExists('test-results/surefire-reports')) {
                        junit 'test-results/surefire-reports/*.xml'
                    }
                }

                if (fileExists('selenoid-videos')) {
                    echo 'Архивация видео...'
                    archiveArtifacts artifacts: 'selenoid-videos/**/*', allowEmptyArchive: true
                }
            }
        }
    }
}