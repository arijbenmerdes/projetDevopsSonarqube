pipeline {

    agent any
     triggers {
            githubPush()
        }
    environment {
        DOCKER_CREDENTIALS = credentials('DOCKER_CREDENTIALS')  // Utilise l'ID de ton credential Docker Hub
        SONAR_TOKEN = credentials('SONAR_TOKEN')


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
        stage('Run Unit Tests') {
                    steps {
                        script {
                            echo " Exécution des tests JUnit et Mockito..."
                            sh 'mvn test'
                        }
                    }
         }
        stage('SonarQube Analysis') {
            steps {
                script {
                      sh 'docker start sonarqube'
                    // Analyser avec SonarQube
                    withSonarQubeEnv('SonarQube') { // Assurez-vous que 'SonarQube' est le nom de votre serveur SonarQube
                        sh 'mvn sonar:sonar -Dsonar.projectKey=projetDevopsSonarqube -Dsonar.projectName="projetDevopsSonarqube" -Dsonar.host.url=http://192.168.50.4:9000  -Dsonar.token=$SONAR_TOKEN'
                    }
                }
            }
        }

        // stage('Deploy to Nexus') {
                  //    steps {
                  //      script {
                  //          sh 'docker start nexus'
                            // Déployer directement dans Nexus sans exécuter les tests
                           //  sh 'mvn deploy -DskipTests -DaltDeploymentRepository=deploymentRepo::default::http://localhost:8081/repository/maven-releases/'
                       //  }
                    // }
                // }


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

stage('Start Prometheus & Grafana') {
           steps {
               script {
                   sh 'docker start prometheus grafana'
               }
           }
       }


          stage('Push Docker Image') {
             steps {
                 script {
                     // Connecter à Docker Hub en utilisant les credentials stockés dans Jenkins

                     sh 'echo "$DOCKER_CREDENTIALS_PSW" | docker login -u "$DOCKER_CREDENTIALS_USR" --password-stdin'
                     sh 'sudo docker tag tp-foyer:${VERSION} arijbms/tp-foyer:${VERSION}'
                     sh 'sudo docker push arijbms/tp-foyer:${VERSION}'
                 }
             }
         }
    }

    post {
        success {
            echo 'Analyse  terminée avec succès.'

        }
        failure {
            echo 'L\'analyse  a échoué.'
        }
    }

}

