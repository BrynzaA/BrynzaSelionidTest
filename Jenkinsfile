pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-24'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository -Dmaven.test.failure.ignore=true'
    }

    stages {
        stage('Cleanup Docker') {
            steps {
                script {
                    bat 'docker stop selenoid selenoid-ui 2>nul || echo "No containers to stop"'
                    bat 'docker rm selenoid selenoid-ui 2>nul || echo "No containers to remove"'
                }
            }
        }

        stage('Build and Run Tests') {
            steps {
                script {
                    echo 'Запуск тестов через start-all.bat...'

                    bat 'if not exist C:\\selenoid mkdir C:\\selenoid'
                    bat 'if not exist test-results mkdir test-results'
                    bat 'if not exist selenoid-videos mkdir selenoid-videos'

                    bat 'copy /Y browsers.json C:\\selenoid\\browsers.json 2>nul || echo "Copy failed"'

                    bat 'start-all.bat'
                }
            }
        }

        stage('Archive Results') {
            steps {
                archiveArtifacts artifacts: 'test-results/**/*', fingerprint: true
                archiveArtifacts artifacts: 'selenoid-videos/**/*', allowEmptyArchive: true

                junit 'test-results/surefire-reports/*.xml'
            }
        }
    }

    post {
        always {
            bat 'docker stop selenoid selenoid-ui 2>nul || echo "Cleanup done"'

            echo '=== Сборка завершена ==='
            echo "Рабочая директория: ${env.WORKSPACE}"
            echo "Статус: ${currentBuild.currentResult}"
        }
    }
}