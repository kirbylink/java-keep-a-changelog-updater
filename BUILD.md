# Building Java Keep-A-Changelog Updater

## Inhaltsverzeichnis

- [Prerequisites](#prerequisites)
- [Build the Source Code](#build-the-source-code)
- [Docker](#docker)
  - [Building the Docker Image](#building-the-docker-image)

## Prerequisites
- Java Development Kit (JDK) 17 or higher
- Maven
- Docker (optional, for running in a containerized environment)

## Build the Source Code
To use the source code directly, follow these steps:
1. Ensure you have all prerequisites installed.
2. Clone the repository:
   ```sh
   git clone https://github.com/kirbylink/java-keep-a-changelog-updater.git
   cd java-keep-a-changelog-updater
   ```
3. Build the project using Maven:
   ```sh
   mvn clean verify
   ```
   Or to skip the tests during the build:
   ```sh
   mvn clean package -Dmaven.test.skip=true
   ```
4. Run the application:
   ```sh
   java -jar target/keep-a-changelog-updater-2.0.15-jar-with-dependencies.jar
   ```

## Docker

### Building the Docker Image
To build the Docker image, you can use the provided `Dockerfile` in the root directory of the project. Run the following command to build the image:

```sh
docker build -t java-keep-a-changelog-updater .
```