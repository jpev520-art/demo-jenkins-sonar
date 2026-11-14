pipeline {
    agent any

    tools {
        // Nombre configurado en Manage Jenkins → Tools → Maven installations
        maven 'Maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean compile'
            }
        }

        stage('Tests y cobertura') {
            steps {
                // verify ejecuta los tests JUnit y genera el reporte de JaCoCo
                sh 'mvn -B verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Análisis SonarQube') {
            steps {
                // Nombre configurado en Manage Jenkins → System → SonarQube servers
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn -B sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        success { echo 'Pipeline OK: tests pasaron y Quality Gate aprobado' }
        failure { echo 'El pipeline falló, revisa la etapa en rojo' }
    }
}
