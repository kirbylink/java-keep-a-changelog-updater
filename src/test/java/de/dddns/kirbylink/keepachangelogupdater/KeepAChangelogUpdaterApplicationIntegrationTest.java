package de.dddns.kirbylink.keepachangelogupdater;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import de.dddns.kirbylink.keepachangelogupdater.service.CommandLineService;

class KeepAChangelogUpdaterApplicationIntegrationTest {

  private static String program;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    var jarPath = CommandLineService.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
    var optionalJarName = new File(jarPath).getName();
    program = optionalJarName.endsWith(".jar") ? optionalJarName : "keep-a-changelog-updater-x.y.z-jar-with-dependencies.jar";
  }

  @Test
  void testKeepAChangelogUpdaterApplication() {
    // Given

    // When
    var throwAbleMethod = catchThrowable(() -> {
      new KeepAChangelogUpdaterApplication();
    });

    // Then
    assertThat(throwAbleMethod).isNull();

  }

  @Nested
  @DisplayName("Test main and print help")
  class TestHelpPrint {

    @Test
    void testMain_WhenMethodWithEmptyArgumentsIsCalled_ThenHelpIsPrinted() throws URISyntaxException {

      // Given
      var args = new String[] {};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var stringFormat = """
          usage: java -jar %s -h | -s <arg>
           -h,--help             Print this help message
           -s,--scenario <arg>   Scenario to execute: create, add-entry, release, auto-generate
          """;
      var expectedOutput = String.format(stringFormat, program);

      try {
        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(normalizeOutput(consoleOutput)).isNotEmpty().contains(normalizeOutput(expectedOutput));
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testMain_WhenMethodWithHelpArgumentIsCalled_ThenHelpIsPrinted() throws URISyntaxException {

      // Given
      var args = new String[] {"-h"};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var stringFormat = """
          usage: java -jar %s -h | -s <arg>
           -h,--help             Print this help message
           -s,--scenario <arg>   Scenario to execute: create, add-entry, release, auto-generate
          """;
      var expectedOutput = String.format(stringFormat, program);

      try {
        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(normalizeOutput(consoleOutput)).isNotEmpty().isEqualTo(normalizeOutput(expectedOutput));
      } finally {
        System.setOut(originalOut);
      }
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForPrintingHelInMainMethod")
    void testMain_WhenMethodWithScenarioButWithoutRequiredArgumentIsCalled_ThenHelpIsPrinted(String scenario, String expectedOutput) throws URISyntaxException {

      // Given
      var args = new String[] {"-s", scenario};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      try {
        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(normalizeOutput(consoleOutput)).isNotEmpty().contains(normalizeOutput(expectedOutput));
      } finally {
        System.setOut(originalOut);
      }
    }

    private static Stream<Arguments> provideArgumentsForPrintingHelInMainMethod() {

      var stringFormatCreate = """
          usage: java -jar %s
                 -s create -b <arg> -c | -o <arg> [-d <arg>]  -r <arg> [-t <arg>]
           -b,--branch <arg>        Main branch for link generation
           -c,--console             Output result to console instead of a file
           -d,--description <arg>   Description for a new entry
           -o,--output <arg>        Path to the output file
           -r,--repository <arg>    Repository URL for link generation
           -t,--category <arg>      Category for the new entry (Added, Changed,
                                    Fixed, Removed, Security, Deprecated)
          """;
      var expectedOutputCreate = String.format(stringFormatCreate, program);

      var stringFormatAddEntry = """
          usage: java -jar %s
                 -s add-entry -c | -o <arg> -d <arg> -i <arg>  -t <arg> [-v <arg>]
           -c,--console             Output result to console instead of a file
           -d,--description <arg>   Description for a new entry
           -i,--input <arg>         Path to the existing CHANGELOG.md file
           -o,--output <arg>        Path to the output file (default: input path)
           -t,--category <arg>      Category for the new entry (Added, Changed,
                                    Fixed, Removed, Security, Deprecated)
           -v,--version <arg>       Existing release version (default: Unreleased)
          """;
      var expectedOutputAddEntry = String.format(stringFormatAddEntry, program);

      var stringFormatRelease = """
         usage: java -jar %s -s release -b <arg> -c | -o <arg> -i <arg>  -r <arg> -rt <arg>
          -b,--branch <arg>          Main branch for link generation
          -c,--console               Output result to console instead of a file
          -i,--input <arg>           Path to the existing CHANGELOG.md file
          -o,--output <arg>          Path to the output file (default: input path)
          -r,--repository <arg>      Repository URL for link generation
          -rt,--release-type <arg>   Release type: major, minor, patch
         """;
      var expectedOutputRelease = String.format(stringFormatRelease, program);

      return Stream.of(
              Arguments.of("create", expectedOutputCreate),
              Arguments.of("add-entry", expectedOutputAddEntry),
              Arguments.of("release", expectedOutputRelease)
      );
    }
  }


  @Nested
  @DisplayName("Test main and print or console log")
  class TestPrintAndConsole {

    @Test
    void testMain_WhenMethodWithScenarioUndefinedIsCalled_ThenScenarioHelpIsPrinted() throws URISyntaxException {

      // Given
      var args = new String[] {"-s", "invalid"};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var stringFormat = """
          Unknown scenario value: invalid
          usage: java -jar %s -h | -s <arg>
           -h,--help             Print this help message
           -s,--scenario <arg>   Scenario to execute: create, add-entry, release
          """;
      var expectedOutput = String.format(stringFormat, program);

      try {
        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(normalizeOutput(consoleOutput)).isNotEmpty().contains(normalizeOutput(expectedOutput));
      } finally {
        System.setOut(originalOut);
      }
    }

    @Nested
    @DisplayName("Test the scenario 'create'")
    class TestScenarioCreate {

      @Test
      void testMain_WhenMethodWithScenarioCreateAndAllRequiredArgsIsCalled_ThenNewChangelogIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "create", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
        System.setOut(printStream);

        var expectedOutput = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            """;

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

      @Test
      void testMain_WhenMethodWithScenarioCreateAndAllRequiredArgsIsCalled_ThenNewChangelogIsSaved() throws URISyntaxException, IOException {

        // Given
        var temporaryFolder = Files.createTempDirectory("target");
        var targetPath = temporaryFolder.resolve("CHANGELOG.md");
        var filePath = targetPath.toFile().getAbsolutePath();
        var args = new String[] {"-s", "create", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-o", filePath};

        var expectedOutput = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            """;

        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        AssertionsForInterfaceTypes.assertThat(targetPath).exists()
          .binaryContent().isEqualTo(expectedOutput.getBytes());

        deleteDirectoryRecursively(temporaryFolder);
      }

      @Test
      void testMain_WhenMethodWithScenarioCreateWithAllArgsIsCalled_ThenNewChangelogWithEntryIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "create", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-d", "Add new entry", "-t", "added", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
        System.setOut(printStream);

        var expectedOutput = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]
            ### Added
            - Add new entry

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            """;

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

      @Test
      void testMain_WhenMethodWithScenarioCreateWithAllArgsIsCalled_ThenNewChangelogWithEntryIsSaved() throws URISyntaxException, IOException {

        // Given
        var temporaryFolder = Files.createTempDirectory("target");
        var targetPath = temporaryFolder.resolve("CHANGELOG.md");
        var filePath = targetPath.toFile().getAbsolutePath();
        var args = new String[] {"-s", "create", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-d", "Add new entry", "-t", "Added", "-o", filePath};

        var expectedOutput = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]
            ### Added
            - Add new entry

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            """;

        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        AssertionsForInterfaceTypes.assertThat(targetPath).exists()
          .binaryContent().isEqualTo(expectedOutput.getBytes());

        deleteDirectoryRecursively(temporaryFolder);
      }

    }

    @Nested
    @DisplayName("Test the scenario 'add-entry'")
    class TestScenarioAddEntry {

      @Test
      void testMain_WhenMethodWithScenarioAddEntryAndAllRequiredArgsIsCalled_ThenNewEntryAddedToUnreleasedAndResultIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "add-entry", "-i", "src/test/resources/CHANGELOG-created.md", "-d", "Add new entry", "-t", "Added", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
         System.setOut(printStream);

        var expectedOutput = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]
            ### Added
            - Add new entry

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            """;

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

      @Test
      void testMain_WhenMethodWithScenarioAddEntryAndAllRequiredArgsIsCalled_ThenNewEntryAddedToUnreleasedAndResultIsSaved() throws URISyntaxException, IOException {

        // Given
        var temporaryFolder = Files.createTempDirectory("target");
        var targetPath = temporaryFolder.resolve("CHANGELOG.md");
        var filePath = targetPath.toFile().getAbsolutePath();
        var args = new String[] {"-s", "add-entry", "-i", "src/test/resources/CHANGELOG-created.md", "-d", "Add new entry", "-t", "Added", "-o", filePath};

        var expectedOutput = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]
            ### Added
            - Add new entry

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            """;

        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        AssertionsForInterfaceTypes.assertThat(targetPath).exists()
          .binaryContent().isEqualTo(expectedOutput.getBytes());

        deleteDirectoryRecursively(temporaryFolder);
      }

      @Test
      void testMain_WhenMethodWithScenarioAddEntryWithSpecificVersionAndAllRequiredArgsIsCalled_ThenNewEntryAddedToSpecificVersionAndResultIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "add-entry", "-i", "src/test/resources/CHANGELOG-with-entries.md", "-v", "0.1.0", "-d", "Add new entry", "-t", "Added", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
         System.setOut(printStream);

         var date = LocalDate.now().toString();
         var stringFormat = """
            # Changelog of some application

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            And there is some additional description.

            ## [Unreleased]
            ### Added
            - Document the `convert()` method

            ## [0.1.0] - %s
            ### Added
            - Add new entry

            ### Changed
            - Change structure of VersionEntity

            ### Fixed
            - Replace application name in MAKE.md file

            ### Removed
            - Remove unused method `parse()`
            - Delete check for NullpointerException in `main()` method
            
            ### Security
            - Fixing a buffer overflow
            
            ### Deprecated
            - Mark API as deprecated in further version

            ## [0.0.1] - 2024-06.30
            ### Added
            - Add initial files

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            [0.1.0]: https://git.example.com:443/organization/repo.git/compare/0.0.1...0.1.0
            [0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/0.0.1
            """;
         var expectedOutput = String.format(stringFormat, date);

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

    }

    @Nested
    @DisplayName("Test the scenario 'release'")
    class TestScenarioRelease {

      @Test
      void testMain_WhenMethodWithScenarioReleaseWithMajorAndAllRequiredArgsIsCalled_ThenNewMajorVersionCreatedAndResultIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "release", "-i", "src/test/resources/CHANGELOG-with-entry.md", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-rt", "major", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
        System.setOut(printStream);

        var date = LocalDate.now().toString();
        var stringFormat = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]

            ## [1.0.0] - %s
            ### Added
            - Add new entry

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            [1.0.0]: https://git.example.com:443/organization/repo.git/releases/tag/1.0.0

            """;

        var expectedOutput = String.format(stringFormat, date);

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

      @Test
      void testMain_WhenMethodWithScenarioReleaseWithMajorAndAllRequiredArgsIsCalled_ThenNewMajorVersionCreatedAndResultIsSaved() throws URISyntaxException, IOException {

        // Given
        var temporaryFolder = Files.createTempDirectory("target");
        var targetPath = temporaryFolder.resolve("CHANGELOG.md");
        var filePath = targetPath.toFile().getAbsolutePath();
        var args = new String[] {"-s", "release", "-i", "src/test/resources/CHANGELOG-with-entry.md", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-rt", "major", "-o", filePath};

        var date = LocalDate.now().toString();
        var stringFormat = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]

            ## [1.0.0] - %s
            ### Added
            - Add new entry

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            [1.0.0]: https://git.example.com:443/organization/repo.git/releases/tag/1.0.0
            """;

        var expectedOutput = String.format(stringFormat, date);

        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        AssertionsForInterfaceTypes.assertThat(targetPath).exists()
          .binaryContent().isEqualTo(expectedOutput.getBytes());

        deleteDirectoryRecursively(temporaryFolder);
      }

      @Test
      void testMain_WhenMethodWithScenarioReleaseWithPatchAndExistingVersionsAndAllRequiredArgsIsCalled_ThenNewPatchVersionCreatedAndResultIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "release", "-i", "src/test/resources/CHANGELOG-with-entries.md", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-rt", "patch", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
        System.setOut(printStream);

        var date = LocalDate.now().toString();
        var stringFormat = """
                # Changelog of some application

                All notable changes to this project will be documented in this file.

                The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
                and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

                And there is some additional description.

                ## [Unreleased]

                ## [0.1.1] - %s
                ### Added
                - Document the `convert()` method

                ## [0.1.0] - 2024-07-20
                ### Changed
                - Change structure of VersionEntity

                ### Fixed
                - Replace application name in MAKE.md file

                ### Removed
                - Remove unused method `parse()`
                - Delete check for NullpointerException in `main()` method

                ### Security
                - Fixing a buffer overflow
                
                ### Deprecated
                - Mark API as deprecated in further version

                ## [0.0.1] - 2024-06.30
                ### Added
                - Add initial files

                [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
                [0.1.1]: https://git.example.com:443/organization/repo.git/compare/0.1.0...0.1.1
                [0.1.0]: https://git.example.com:443/organization/repo.git/compare/0.0.1...0.1.0
                [0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/0.0.1
                """;

        var expectedOutput = String.format(stringFormat, date);

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

      @Test
      void testMain_WhenMethodWithScenarioReleaseWithMajorAndOutputWithoutPathIsCalled_ThenNewMajorVersionCreatedAndResultIsSavedAtInputFile() throws URISyntaxException, IOException {

        // Given
        var temporaryFolder = Files.createTempDirectory("target");
        var inputAndOutputPath = temporaryFolder.resolve("CHANGELOG.md");
        var filePath = inputAndOutputPath.toFile().getAbsolutePath();
        var source = Path.of("src/test/resources/CHANGELOG-with-entry.md");
        Files.copy(source, inputAndOutputPath);
        var args = new String[] {"-s", "release", "-i", filePath, "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-rt", "major", "-o"};

        var date = LocalDate.now().toString();
        var stringFormat = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]

            ## [1.0.0] - %s
            ### Added
            - Add new entry

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            [1.0.0]: https://git.example.com:443/organization/repo.git/releases/tag/1.0.0
            """;

        var expectedOutput = String.format(stringFormat, date);

        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        AssertionsForInterfaceTypes.assertThat(inputAndOutputPath).exists()
          .binaryContent().isEqualTo(expectedOutput.getBytes());

        deleteDirectoryRecursively(temporaryFolder);
      }
    }

    @Nested
    @DisplayName("Test the scenario 'auto-generate'")
    class TestScenarioAutoGenerate {

      @Test
      void testMain_WhenMethodWithScenarioAutoGenerateWithoutAutoReleaseAndAllRequiredArgsIsCalled_ThenNewEntriesAreAddedToUnreleasedAndResultIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "auto-generate", "-i", "src/test/resources/CHANGELOG-created.md", "-g", "src/test/resources/git-log-with-all-important-commits.txt", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
         System.setOut(printStream);

        var expectedOutput = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]
            ### Added
            - Implement git log parsing
            
            ### Changed
            - Updating dependencies
            - Changing sorting algorithm

            ### Fixed
            - Correct minor bug in parsing logic
            
            ### Removed
            - Delete depricated option of command line
            
            ### Security
            - Fixing a buffer overflow
            
            ### Deprecated
            - Mark option of command line as deprecated

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            """;

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

      @Test
      void testMain_WhenMethodWithScenarioAutoGenerateWithoutAutoReleaseAndAllRequiredArgsIsCalled_ThenNewEntriesAreAddedToWantedVersionAndResultIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "auto-generate", "-i", "src/test/resources/CHANGELOG-with-entries.md", "-g", "src/test/resources/git-log.txt", "-c", "-v", "0.1.0"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
        System.setOut(printStream);

        var expectedOutput = """
            # Changelog of some application

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            And there is some additional description.

            ## [Unreleased]
            ### Added
            - Document the `convert()` method

            ## [0.1.0] - 2024-07-20
            ### Added
            - Implement git log parsing

            ### Changed
            - Change structure of VersionEntity

            ### Fixed
            - Replace application name in MAKE.md file
            - Correct minor bug in parsing logic

            ### Removed
            - Remove unused method `parse()`
            - Delete check for NullpointerException in `main()` method

            ### Security
            - Fixing a buffer overflow
            
            ### Deprecated
            - Mark API as deprecated in further version

            ## [0.0.1] - 2024-06.30
            ### Added
            - Add initial files

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            [0.1.0]: https://git.example.com:443/organization/repo.git/compare/0.0.1...0.1.0
            [0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/0.0.1
            """;

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

      @Test
      void testMain_WhenMethodWithScenarioAutoGenerateWithoutAutoReleaseAndAllRequiredArgsIsCalled_ThenNewEntriesAreAddedWithBreakingChangeAndResultIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "auto-generate", "-i", "src/test/resources/CHANGELOG-created.md", "-g", "src/test/resources/git-log-with-breaking-changes.txt", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
        System.setOut(printStream);

        var expectedOutput = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]
            ### Added
            - Implement git log parsing

            ### Fixed
            - Correct minor bug in parsing logic

            BREAKING CHANGE: Additional Multiple lines
            that shows that there are breaking changes.

            Some Multiple lines
            that shows that there are breaking changes.

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            """;

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

      @Test
      void testMain_WhenMethodWithScenarioAutoGenerateWithAutoReleaseAndAllRequiredArgsIsCalled_ThenNewReleaseIsCreatedAndResultIsPrinted() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "auto-generate", "-i", "src/test/resources/CHANGELOG-created.md", "-g", "src/test/resources/git-log.txt", "-a", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
        System.setOut(printStream);

        var date = LocalDate.now().toString();
        var stringFormat = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]

            ## [0.1.0] - %s
            ### Added
            - Implement git log parsing

            ### Fixed
            - Correct minor bug in parsing logic

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            [0.1.0]: https://git.example.com:443/organization/repo.git/releases/tag/0.1.0
            """;

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);
          var expectedOutput = String.format(stringFormat, date);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

        @Test
        void testMain_WhenMethodWithScenarioAutoGenerateWithAutoReleaseAndBreakingChangeAndAllRequiredArgsIsCalled_ThenNewReleaseIsCreatedAndResultIsPrinted() throws URISyntaxException {

          // Given
          var args = new String[] {"-s", "auto-generate", "-i", "src/test/resources/CHANGELOG-created.md", "-g", "src/test/resources/git-log-with-breaking-changes.txt", "-a", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-c"};
          var byteArrayOutputStream = new ByteArrayOutputStream();
          var printStream = new PrintStream(byteArrayOutputStream);
          var originalOut = System.out;
          System.setOut(printStream);

          var date = LocalDate.now().toString();
          var stringFormat = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]

            ## [1.0.0] - %s
            ### Added
            - Implement git log parsing

            ### Fixed
            - Correct minor bug in parsing logic

            BREAKING CHANGE: Additional Multiple lines
            that shows that there are breaking changes.

            Some Multiple lines
            that shows that there are breaking changes.

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            [1.0.0]: https://git.example.com:443/organization/repo.git/releases/tag/1.0.0
            """;

          try {
            // When
            KeepAChangelogUpdaterApplication.main(args);
            var expectedOutput = String.format(stringFormat, date);

            // Then
            var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
            assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
          } finally {
            System.setOut(originalOut);
          }
      }

      @Test
      void testMain_WhenMethodWithScenarioAutoGenerateWithAutoReleaseAndCustomProperties_ThenMajorReleaseWillBeBuildAndCommitsUnderOtherCategories() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "auto-generate", "-i", "src/test/resources/CHANGELOG-created.md", "-g", "src/test/resources/git-log-for-properties.txt", "-p", "src/test/resources/properties-test.yaml", "-a", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
        System.setOut(printStream);

        var date = LocalDate.now().toString();
        var stringFormat = """
            # Changelog

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            ## [Unreleased]

            ## [1.0.0] - %s
            ### Added
            - Will be under Added

            ### Changed
            - Will trigger a major release and under Changed

            ### Fixed
            - Will be under Fixed

            ### Removed
            - Will be under Removed
            
            ### Security
            - Will be under Security
            
            ### Deprecated
            - Will be under Deprecated

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            [1.0.0]: https://git.example.com:443/organization/repo.git/releases/tag/1.0.0
            """;

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);
          var expectedOutput = String.format(stringFormat, date);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
        } finally {
          System.setOut(originalOut);
        }
      }

      @Test
      void testMain_WhenMethodWithScenarioAutoGenerateWithAutoReleaseAndNoConventionalCommits_ThenChangelogWillNotBeChanged() throws URISyntaxException {

        // Given
        var args = new String[] {"-s", "auto-generate", "-i", "src/test/resources/CHANGELOG-with-entries.md", "-g", "src/test/resources/git-log-with-duplicated-entries-and-non-conventional-commits.txt", "-a", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-c"};
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);
        var originalOut = System.out;
        System.setOut(printStream);

        var sourceAndTargetChangelog = """
            # Changelog of some application

            All notable changes to this project will be documented in this file.

            The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
            and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

            And there is some additional description.

            ## [Unreleased]
            ### Added
            - Document the `convert()` method

            ## [0.1.0] - 2024-07-20
            ### Changed
            - Change structure of VersionEntity

            ### Fixed
            - Replace application name in MAKE.md file

            ### Removed
            - Remove unused method `parse()`
            - Delete check for NullpointerException in `main()` method

            ### Security
            - Fixing a buffer overflow
            
            ### Deprecated
            - Mark API as deprecated in further version

            ## [0.0.1] - 2024-06.30
            ### Added
            - Add initial files

            [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
            [0.1.0]: https://git.example.com:443/organization/repo.git/compare/0.0.1...0.1.0
            [0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/0.0.1
            """;

        try {
          // When
          KeepAChangelogUpdaterApplication.main(args);

          // Then
          var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
          assertThat(consoleOutput).isNotEmpty().contains(sourceAndTargetChangelog);
        } finally {
          System.setOut(originalOut);
        }
      }
    }
  }

  @Nested
  @DisplayName("Test error handling")
  class TestErrorHandling {

    @Test
    void testMain_WhenMethodWithScenarioAddEntryWithInvalidInputFile_ThenErrorMessageWillBePrinted() throws URISyntaxException {

      // Given
      var args = new String[] {"-s", "add-entry", "-i", "invalid/path", "-d", "Add new entry", "-t", "Added", "-c"};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var expectedOutput = "Error occurred during execution: invalid/path";

      try {
        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testMain_WhenMethodWithScenarioReleaseWithInvalidInputFile_ThenErrorMessageWillBePrinted() throws URISyntaxException {

      // Given
      var args = new String[] {"-s", "release", "-i", "invalid/path", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-rt", "major", "-o"};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var expectedOutput = "Error occurred during execution: invalid/path";

      try {
        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testMain_WhenMethodWithScenarioReleaseWithInvalidOutputFile_ThenErrorMessageWillBePrinted() throws URISyntaxException {

      // Given
      var args = new String[] {"-s", "release", "-i", "src/test/resources/CHANGELOG-with-entry.md", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-rt", "major", "-o", "invalid/path"};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var expectedOutput = "Couldn't save content to invalid/path";

      try {
        // When
        KeepAChangelogUpdaterApplication.main(args);

        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }
  }

  private void deleteDirectoryRecursively(Path path) throws IOException {
    if (Files.isDirectory(path)) {
      try (var entries = Files.newDirectoryStream(path)) {
        for (Path entry : entries) {
          deleteDirectoryRecursively(entry);
        }
      }
    }
    Files.delete(path);
  }

  private String normalizeOutput(String output) {
    return output.trim().replaceAll("\\s+", " ");
  }
}
