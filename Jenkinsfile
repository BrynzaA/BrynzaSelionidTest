pipeline {
    agent any

    stages {
        stage('Cleanup Docker') {
            steps {
                bat 'docker stop selenoid selenoid-ui 2>nul || echo "No containers to stop"'
                bat 'docker rm selenoid selenoid-ui 2>nul || echo "No containers to remove"'
            }
        }

        stage('Prepare Environment') {
            steps {
                bat 'if not exist C:\\selenoid mkdir C:\\selenoid'
                bat 'if not exist test-results mkdir test-results'
                bat 'if not exist selenoid-videos mkdir selenoid-videos'
                bat 'copy /Y browsers.json C:\\selenoid\\browsers.json 2>nul || echo "Copy failed"'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'start-all.bat'
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
            echo "Build status: ${currentBuild.currentResult}"
        }
    }
}