[![Build Status](https://drone.phoenix.ipv64.de/api/badges/David/java-keep-a-changelog-updater/status.svg?ref=refs/heads/main)](https://drone.phoenix.ipv64.de/David/java-keep-a-changelog-updater) for `main`<br />
[![Build Status develop branch](https://drone.phoenix.ipv64.de/api/badges/David/java-keep-a-changelog-updater/status.svg?ref=refs/heads/develop)](https://drone.phoenix.ipv64.de/David/java-keep-a-changelog-updater) for `develop`<br />
[![Bugs](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=bugs&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)
[![Code Smells](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=code_smells&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)
[![Duplizierte Quellcodezeilen (%)](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=duplicated_lines_density&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)
[![Technische Schulden](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=sqale_index&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)
[![Vulnerabilities](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=vulnerabilities&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)
[![Quellcodezeilen](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=ncloc&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)<br />
[![SQALE-Bewertung](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=sqale_rating&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)
[![Reliability Rating](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=reliability_rating&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)
[![Security Rating](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=security_rating&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)<br />
[![Alarmhinweise](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=alert_status&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater)<br /> 
[![Abdeckung](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=coverage&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater) for `main`<br /> 
[![Abdeckung](https://sonarqube.phoenix.ipv64.de/api/project_badges/measure?branch=develop&project=de.dddns.kirbylink%3Akeep-a-changelog-updater&metric=coverage&token=sqb_7f8845918396766dc3292b8e3a4613f764a38f99)](https://sonarqube.phoenix.ipv64.de/dashboard?id=de.dddns.kirbylink%3Akeep-a-changelog-updater&branch=develop) for `develop`<br /> 

# Java Keep-A-Changelog Updater

## Table of Contents
- [Java Keep-A-Changelog Updater](#java-keep-a-changelog-updater)
  - [Introduction](#introduction)
  - [Features](#features)
  - [Prerequisites](#prerequisites)
  - [Usage](#usage)
    - [Using the Source Code](#using-the-source-code)
    - [Using the Compiled JAR](#using-the-compiled-jar)
    - [Command-Line Parameters](#command-line-parameters)
  - [Examples](#examples)
    - [Example 1: Create empty Changelog](#example-1-create-empty-changelog)
    - [Example 2a: Add an entry to Unreleased version](#example-2a-add-an-entry-to-unreleased-version)
    - [Example 2b: Add an entry to existing or new version](#example-2b-add-an-entry-to-existing-or-new-version)
    - [Example 3: Create a Release version](#example-3-create-a-release-version)
  - [Docker](#docker)
    - [Using Docker](#using-docker)
    - [Building the Docker Image](#building-the-docker-image)
    - [Running the Docker Container](#running-the-docker-container)
      - [Create Changelog](#create-changelog)
      - [Add Entry to Unreleased in Changelog](#add-entry-to-unreleased-in-changelog)
      - [Add Entry to specific Version in Changelog](#add-entry-to-specific-version-in-changelog)
      - [Release Changelog](#release-changelog)
  - [Example for Drone CI](#example-for-drone-ci)
  - [Building the Project](#building-the-project)
  - [Contributing](#contributing)
  - [License](#license)
  - [Contact](#contact)


## Introduction
Java Keep-A-Changelog Updater is a tool designed to automate the process of updating the changelog in your Java projects. It follows the [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) format, ensuring consistency and clarity in your project's change history.

## Features
- **Automated Changelog Updates**: Automatically updates the changelog with new entries based on your given description.
- **Automated Changelog Release**: Automatically creates Releases (Major, Minor, Patch) and updates the links.
- **Keep a Changelog Format**: Adheres to the Keep a Changelog format for clear and standardized changelog entries.
- **Integration with CI/CD**: Easily integrates with CI/CD pipelines to automate the changelog update process.
- **Customizable**: Allows customization of changelog entries and sections.

## Prerequisites
- Java Environment (JRE) 17 or higher.

## Usage

### Using the Source Code
See [BUILD.md](./BUILD.md) how to build from Source Code 

### Using the Compiled JAR
To use the compiled JAR file, follow these steps:

1. Download the latest release (`keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar`) from the [Releases page](https://github.com/kirbylink/java-keep-a-changelog-updater/releases).
2. Run the JAR file:
   ```sh
   java -jar keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar
   ```

### Command-Line Parameters
You can start the program with optional parameters. To get all parameters, start the program with `-h` or `--help`:
```bash
usage: java -jar
       keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar
       -h | -s <arg>
 -h,--help             Print this help message
 -s,--scenario <arg>   Scenario to execute: create, add-entry, release
```

With `-s` or `--scenario` and `create|add-entry|release` you get all parameters that is needed for the scenario:
```bash
usage: java -jar
       keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar
       -s create -b <arg> -c | -o <arg> [-d <arg>]  -r <arg> [-t <arg>]
 -b,--branch <arg>        Main branch for link generation
 -c,--console             Output result to console instead of a file
 -d,--description <arg>   Description for a new entry
 -o,--output <arg>        Path to the output file
 -r,--repository <arg>    Repository URL for link generation
 -t,--category <arg>      Category for the new entry (Added, Changed,
                          Fixed, Removed)
```

```bash
usage: java -jar
       keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar
       -s add-entry -c | -o <arg> -d <arg> -i <arg>  -t <arg> [-v <arg>]
 -c,--console             Output result to console instead of a file
 -d,--description <arg>   Description for a new entry
 -i,--input <arg>         Path to the existing CHANGELOG.md file
 -o,--output <arg>        Path to the output file (default: input path)
 -t,--category <arg>      Category for the new entry (Added, Changed,
                          Fixed, Removed)
 -v,--version <arg>       Existing release version (default: Unreleased)
```

```bash
usage: java -jar
       keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar
       -s release -b <arg> -c | -o <arg> -i <arg>  -r <arg> -rt <arg>
 -b,--branch <arg>          Main branch for link generation
 -c,--console               Output result to console instead of a file
 -i,--input <arg>           Path to the existing CHANGELOG.md file
 -o,--output <arg>          Path to the output file (default: input path)
 -r,--repository <arg>      Repository URL for link generation
 -rt,--release-type <arg>   Release type: major, minor, patch
```

## Examples
Here are some examples of how to use the Java Keep-A-Changelog Updater:

### Example 1: Create empty Changelog
Creates an empty CHANGELOG.md file under /path/to/output/folder/CHANGELOG.md. Using repository URL and branch to link unreleased commitments.
```sh
java -jar keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar -s create -o /path/to/output/folder/CHANGELOG.md -b main -r https://example.com/example-project.git
```

Example outcome:
```markdown
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

[unreleased]: https://example.com/example-project.git/compare/main...HEAD
```

### Example 2a: Add an entry to Unreleased version
Reads a CHANGELOG.md file as input, writes under `Unreleased` in category `Changed` the entry `Update Maven dependencies` and save it in the same file.
```sh
java -jar keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar -s add-entry -i /path/to/CHANGELOG.md -d 'Update Maven dependencies' -t Changed -o
```

Example outcome:
```markdown
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Changed
- Update Maven dependencies

[unreleased]: https://example.com/example-project.git/compare/main...HEAD
```

### Example 2b: Add an entry to existing or new version
Reads a CHANGELOG.md file as input, writes under `1.0.0` in category `Added` the entry `Some amazing features` and save it in the same file.
```sh
java -jar keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar -s add-entry -i /path/to/CHANGELOG.md -d 'Some amazing features' -t Added -v 1.0.0 -o
```

Example outcome:
```markdown
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Changed
- Update Maven dependencies

## [1.0.0] - 2024-08-05
### Added
- Some amazing features

[unreleased]: https://example.com/example-project.git/compare/main...HEAD
[1.0.0]: https://example.com/example-project.git/releases/tag/1.0.0
```

### Example 3: Create a Release version
Reads a CHANGELOG.md file as input and creates a new Release version with increased Patch version. Using repository URL and branch to create links for all versions.
```sh
java -jar keep-a-changelog-updater-1.0.0-jar-with-dependencies.jar -s release -i /path/to/CHANGELOG.md -b main -r https://example.com/example-project.git -rt patch -o
```

Example outcome:
```markdown
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.1] - 2024-08-05
### Changed
- Update Maven dependencies

## [1.0.0] - 2024-08-05
### Added
- Some amazing features

[unreleased]: https://example.com/example-project.git/compare/main...HEAD
[1.0.1]: https://example.com/example-project.git/compare/1.0.0...1.0.1
[1.0.0]: https://example.com/example-project.git/releases/tag/1.0.0
```

## Docker

### Using Docker
Java Keep-A-Changelog Updater can be run within a Docker container, providing an easy and consistent environment for the application.

### Building the Docker Image
See [BUILD.md](./BUILD.md) how to build a Docker Image.

### Running the Docker Container
To run the Docker container with different commands, use the following examples. In each case, the current directory is mounted to `/workspace` in the container.

#### Create Changelog

```sh
docker run --rm \
  -v $(pwd):/workspace \
  java-keep-a-changelog-updater -s create \
  -b main \
  -r https://git.example.com/example-project.git \
  -o /workspace/CHANGELOG.md
```

#### Add Entry to Unreleased in Changelog

```sh
docker run --rm \
  -v $(pwd):/workspace \
  java-keep-a-changelog-updater -s add-entry \
  -i /workspace/CHANGELOG.md \
  -d 'Update Maven dependencies' \
  -t Changed \
  -o
```

#### Add Entry to specific Version in Changelog

```sh
docker run --rm \
  -v $(pwd):/workspace \
  java-keep-a-changelog-updater -s add-entry \
  -i /workspace/CHANGELOG.md \
  -d 'Some amazing features' \
  -t Added \
  -v 1.0.0 \
  -o
```

#### Release Changelog

```sh
docker run --rm \
  -v $(pwd):/workspace \
  java-keep-a-changelog-updater -s release \
  -i /workspace/CHANGELOG.md \
  -b main \
  -r https://git.example.com/example-project.git \
  -rt patch \
  -o
```

## Example for Drone CI
To integrate Java Keep-A-Changelog Updater with a Drone CI pipeline, you can add the following steps to your `.drone.yml` file:

```yaml
kind: pipeline
type: docker
name: changelog-update

steps:
  - name: Create Changelog
    image: java-keep-a-changelog-updater
    command:
      - -s
      - create
      - -b
      - main
      - -r
      - https://git.example.com/example-project.git
      - -o
      - /drone/src/CHANGELOG.md

  - name: Add Entry to Changelog
    image: java-keep-a-changelog-updater
    command:
      - -s
      - add-entry
      - -i
      - /drone/src/CHANGELOG.md
      - -d
      - 'Update Maven dependencies'
      - -t
      - Changed
      - -o

  - name: Release Changelog
    image: java-keep-a-changelog-updater
    command:
      - -s
      - release
      - -i
      - /drone/src/CHANGELOG.md
      - -b
      - main
      - -r
      - https://git.example.com/example-project.git
      - -rt
      - patch
      - -o
```

In this configuration:
- The `image` specifies the Docker image for the Java Keep-A-Changelog Updater.
- Each command uses `-s` to specify the action (`create`, `add-entry`, or `release`).
- Input or output source folder is `/drone/src` since Drone usually checks out the source code to that folder.
- Each step demonstrates a different command that can be used with the Docker container to manage the changelog.
- If you have an automated update process that also releases new versions, the changelog can be easily updated.

## Building the Project
For detailed instructions on how to build the project from source, please refer to [BUILD.md](./BUILD.md).

## Contributing
Contributions to the project from the community are welcome. Please read the [CONTRIBUTING.md](./CONTRIBUTING.md) for guidelines on how to contribute.

## License
This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.

## Contact
For any questions or feedback, please open an issue on GitHub.