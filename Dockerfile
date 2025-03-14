# Utiliser une image officielle de OpenJDK comme base
FROM openjdk:17-jdk-slim

# Définir le port sur lequel l'application sera exposée
EXPOSE 8089

# Copier le fichier .jar de ton application
COPY target/tp-foyer-5.0.0.jar tp-foyer.jar

# Définir la commande d'exécution de l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/tp-foyer.jar"]
