FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /opt/java-keep-a-changelog-updater

# Copy the JAR file to the image
COPY --chown=1000:1000 target/*jar-with-dependencies.jar app.jar

# Switch to non-root user
USER 1000

# Run the application
ENTRYPOINT ["java", "-jar", "/opt/java-keep-a-changelog-updater/app.jar"]