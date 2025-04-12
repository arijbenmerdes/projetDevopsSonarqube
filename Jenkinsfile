pipeline {
    agent any
    environment {
        DOCKER_USERNAME = credentials('DOCKER_CREDENTIALS')  // Utilise l'ID de ton credential Docker Hub
        DOCKER_PASSWORD = credentials('DOCKER_CREDENTIALS')  // Utilise le même ID ici
        VERSION = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()

    }

    stages {
        stage('Clone Repository') {
            steps {
                // Cloner le dépôt Git depuis GitHub
                git branch: 'feature/arij', url: 'https://github.com/arijbenmerdes/projetDevopsSonarqube.git'
            }
        }

        stage('Clean') {
            steps {
                // Nettoyer le projet
                sh "mvn clean install -Dversion=${VERSION}"
            }
        }
        stage('Compile') {
            steps {
                // Compiler le projet
                sh 'mvn compile'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Analyser avec SonarQube
                    withSonarQubeEnv('SonarQube') { // Assurez-vous que 'SonarQube' est le nom de votre serveur SonarQube
                        sh 'mvn sonar:sonar -Dsonar.projectKey=projetDevopsSonarqube -Dsonar.projectName="projetDevopsSonarqube" -Dsonar.login=sqp_547896f73092aa1b0fd008535cdf2a29968eb58c'
                    }
                }
            }
        }
        stage('Build Docker Images with Docker Compose') {
            steps {
                script {
                    // Construire l'image Docker avec docker-compose et passer la version
                    sh "docker-compose -f docker-compose.yml build --build-arg VERSION=${VERSION}"
                }
            }
        }

        stage('Start Docker Containers with Docker Compose') {
            steps {
                script {
                    // Démarrer les conteneurs via docker-compose
                    sh 'docker-compose -f docker-compose.yml up -d'
                }
            }
        }


        // stage('Deploy to Nexus') {
        //     steps {
        //         script {
        //             // Déployer directement dans Nexus sans exécuter les tests
        //             sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://localhost:8081/repository/maven-releases/'
        //         }
        //     }
        // }

        // stage('Build Docker Image') {
        //     steps {
        //         script {
        //             // Construire l'image Docker en passant l'argument VERSION
        //             sh "sudo docker build --build-arg VERSION=${VERSION} -t tp-foyer:${VERSION} ."
        //         }
        //     }
        // }
        // stage('Push Docker Image') {
        //     steps {
        //         script {
        //             // Connecter à Docker Hub en utilisant les credentials stockés dans Jenkins

        //             sh 'echo "$DOCKER_PASSWORD" | sudo docker login -u "$DOCKER_USERNAME" --password-stdin'
        //             sh 'sudo docker tag tp-foyer:${VERSION} arijbms/tp-foyer:${VERSION}'
        //             sh 'sudo docker push arijbms/tp-foyer:${VERSION}'
        //         }
        //     }
        // }
    }

    post {
        success {
            echo 'Analyse SonarQube terminée avec succès.'
            emailext (
                to: 'arij2000bms@gmail.com',
                subject: "Build Success: ${currentBuild.fullDisplayName}",
                body: "Le build a réussi.\n\nConsultez le détail du build ici: ${BUILD_URL}"
            )
        }
        failure {
            echo 'L\'analyse SonarQube a échoué.'
            emailext (
                to: 'arij2000bms@gmail.com',
                subject: "Build Failed: ${currentBuild.fullDisplayName}",
                body: "Le build a échoué.\n\nConsultez le détail du build ici: ${BUILD_URL}"
            )
        }
    }

}