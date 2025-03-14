# Utiliser une image officielle de OpenJDK comme base
FROM openjdk:17-jdk-slim

# Définir le port sur lequel l'application sera exposée
EXPOSE 8089
# Set the working directory in the container
WORKDIR /app
# Define the build argument for the version
ARG VERSION
# Copier le fichier .jar de ton application
COPY target/tp-foyer-${VERSION}.jar tp-foyer.jar

# Définir la commande d'exécution de l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/tp-foyer.jar"]
