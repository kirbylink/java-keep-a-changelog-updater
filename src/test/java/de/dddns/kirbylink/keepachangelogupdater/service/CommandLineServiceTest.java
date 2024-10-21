package de.dddns.kirbylink.keepachangelogupdater.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.apache.commons.cli.Options;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Scenario;

class CommandLineServiceTest {

  private CommandLineService commandLineService;

  @BeforeEach
  void setUp() {
    commandLineService = new CommandLineService();
  }

  @Nested
  @DisplayName("Test for get options")
  class TestGetOptions {

    @Test
    void testGetScenarioOptions_WhenMethodWithScenarioIsCalled_ThenOptionsWithAdditionalHelpOptionIsReturned() {
      // Given

      // When
      var options = commandLineService.getScenarioOptions();

      // Then
      AssertionsForInterfaceTypes.assertThat(options.getOptions()).hasSize(2);

      assertThat(options.hasOption("s")).isTrue();
      assertThat(options.getOption("s").isRequired()).isFalse();
      assertThat(options.getOption("s").hasArg()).isTrue();
      assertThat(options.getOption("s").getLongOpt()).isEqualTo("scenario");
      assertThat(options.getOption("s").getDescription()).isEqualTo("Scenario to execute: create, add-entry, release, auto-generate");

      assertThat(options.hasOption("h")).isTrue();
      assertThat(options.getOption("h").isRequired()).isFalse();
      assertThat(options.getOption("h").hasArg()).isFalse();
      assertThat(options.getOption("h").getLongOpt()).isEqualTo("help");
      assertThat(options.getOption("h").getDescription()).isEqualTo("Print this help message");
    }

    @Test
    void testGetScenarioCreateOptions_WhenMethodWithScenarioCreateIsCalled_ThenOptionsForCreateWithOptionsAreReturned() {
      // Given

      // When
      var options = commandLineService.getScenarioCreateOptions();

      // Then
      AssertionsForInterfaceTypes.assertThat(options.getOptions()).hasSize(6);

      assertThat(options.hasOption("o")).isTrue();
      assertThat(options.getOption("o").isRequired()).isFalse();
      assertThat(options.getOption("o").hasArg()).isTrue();

      assertThat(options.hasOption("c")).isTrue();
      assertThat(options.getOption("c").isRequired()).isFalse();
      assertThat(options.getOption("c").hasArg()).isFalse();

      assertThat(options.hasOption("r")).isTrue();
      assertThat(options.getOption("r").isRequired()).isTrue();
      assertThat(options.getOption("r").hasArg()).isTrue();

      assertThat(options.hasOption("b")).isTrue();
      assertThat(options.getOption("b").isRequired()).isTrue();
      assertThat(options.getOption("b").hasArg()).isTrue();

      assertThat(options.hasOption("d")).isTrue();
      assertThat(options.getOption("d").isRequired()).isFalse();
      assertThat(options.getOption("d").hasArg()).isTrue();

      assertThat(options.hasOption("t")).isTrue();
      assertThat(options.getOption("t").isRequired()).isFalse();
      assertThat(options.getOption("t").hasArg()).isTrue();
    }

    @Test
    void testGetScenarioAddEntryOptions_WhenMethodWithScenarioAddEntryIsCalled_ThenOptionsForAddEntryWithOptionsAreReturned() {
      // Given

      // When
      var options = commandLineService.getScenarioAddEntryOptions();

      // Then
      AssertionsForInterfaceTypes.assertThat(options.getOptions()).hasSize(6);

      assertThat(options.hasOption("i")).isTrue();
      assertThat(options.getOption("i").isRequired()).isTrue();
      assertThat(options.getOption("i").hasArg()).isTrue();

      assertThat(options.hasOption("o")).isTrue();
      assertThat(options.getOption("o").isRequired()).isFalse();
      assertThat(options.getOption("o").hasArg()).isTrue();

      assertThat(options.hasOption("c")).isTrue();
      assertThat(options.getOption("c").isRequired()).isFalse();
      assertThat(options.getOption("c").hasArg()).isFalse();

      assertThat(options.hasOption("d")).isTrue();
      assertThat(options.getOption("d").isRequired()).isTrue();
      assertThat(options.getOption("d").hasArg()).isTrue();

      assertThat(options.hasOption("t")).isTrue();
      assertThat(options.getOption("t").isRequired()).isTrue();
      assertThat(options.getOption("t").hasArg()).isTrue();

      assertThat(options.hasOption("v")).isTrue();
      assertThat(options.getOption("v").isRequired()).isFalse();
      assertThat(options.getOption("v").hasArg()).isTrue();
    }

    @Test
    void testGetScenarioReleaseOptions_WhenMethodWithScenarioReleaseIsCalled_ThenOptionsForAddEntryWithOptionsAreReturned() {
      // Given

      // When
      var options = commandLineService.getScenarioReleaseOptions();

      // Then
      AssertionsForInterfaceTypes.assertThat(options.getOptions()).hasSize(6);

      assertThat(options.hasOption("i")).isTrue();
      assertThat(options.getOption("i").isRequired()).isTrue();
      assertThat(options.getOption("i").hasArg()).isTrue();

      assertThat(options.hasOption("o")).isTrue();
      assertThat(options.getOption("o").isRequired()).isFalse();
      assertThat(options.getOption("o").hasArg()).isTrue();

      assertThat(options.hasOption("c")).isTrue();
      assertThat(options.getOption("c").isRequired()).isFalse();
      assertThat(options.getOption("c").hasArg()).isFalse();

      assertThat(options.hasOption("r")).isTrue();
      assertThat(options.getOption("r").isRequired()).isTrue();
      assertThat(options.getOption("r").hasArg()).isTrue();

      assertThat(options.hasOption("b")).isTrue();
      assertThat(options.getOption("b").isRequired()).isTrue();
      assertThat(options.getOption("b").hasArg()).isTrue();

      assertThat(options.hasOption("rt")).isTrue();
      assertThat(options.getOption("rt").isRequired()).isTrue();
      assertThat(options.getOption("rt").hasArg()).isTrue();
    }

    @Test
    void testGetScenarioAutoGenerateOptions_WhenMethodScenarioReleaseWithOutAutoReleaseIsCalled_ThenOptionsForAutoReleaseWithOptionsAreReturned() {
      // Given

      // When
      var options = commandLineService.getScenarioAutoGenerateOptions(false);

      // Then
      assertAutoGenerateOptions(options);

      assertThat(options.hasOption("b")).isTrue();
      assertThat(options.getOption("b").isRequired()).isFalse();
      assertThat(options.getOption("b").hasArg()).isTrue();

      assertThat(options.hasOption("r")).isTrue();
      assertThat(options.getOption("r").isRequired()).isFalse();
      assertThat(options.getOption("r").hasArg()).isTrue();
    }

    @Test
    void testGetScenarioAutoGenerateOptions_WhenMethodScenarioReleaseWithAutoReleaseIsCalled_ThenOptionsForAutoReleaseWithOptionsAreReturned() {
      // Given

      // When
      var options = commandLineService.getScenarioAutoGenerateOptions(true);

      // Then
      assertAutoGenerateOptions(options);

      assertThat(options.hasOption("b")).isTrue();
      assertThat(options.getOption("b").isRequired()).isTrue();
      assertThat(options.getOption("b").hasArg()).isTrue();

      assertThat(options.hasOption("r")).isTrue();
      assertThat(options.getOption("r").isRequired()).isTrue();
      assertThat(options.getOption("r").hasArg()).isTrue();
    }

    private void assertAutoGenerateOptions(Options options) {
      AssertionsForInterfaceTypes.assertThat(options.getOptions()).hasSize(9);

      assertThat(options.hasOption("i")).isTrue();
      assertThat(options.getOption("i").isRequired()).isTrue();
      assertThat(options.getOption("i").hasArg()).isTrue();

      assertThat(options.hasOption("o")).isTrue();
      assertThat(options.getOption("o").isRequired()).isFalse();
      assertThat(options.getOption("o").hasArg()).isTrue();

      assertThat(options.hasOption("c")).isTrue();
      assertThat(options.getOption("c").isRequired()).isFalse();
      assertThat(options.getOption("c").hasArg()).isFalse();

      assertThat(options.hasOption("g")).isTrue();
      assertThat(options.getOption("g").isRequired()).isTrue();
      assertThat(options.getOption("g").hasArg()).isTrue();

      assertThat(options.hasOption("v")).isTrue();
      assertThat(options.getOption("v").isRequired()).isFalse();
      assertThat(options.getOption("v").hasArg()).isTrue();

      assertThat(options.hasOption("a")).isTrue();
      assertThat(options.getOption("a").isRequired()).isFalse();
      assertThat(options.getOption("a").hasArg()).isFalse();

      assertThat(options.hasOption("p")).isTrue();
      assertThat(options.getOption("p").isRequired()).isFalse();
      assertThat(options.getOption("p").hasArg()).isTrue();

    }
  }

  @Nested
  @DisplayName("Test for get CommandLine")
  class TestGetCommandLine {

    @Test
    void testGetCommandLine_WhenMethodWithEmptyArgumentsIsCalled_ThenHelpIsPrinted() throws URISyntaxException {
      // Given
      var args = new String[] {};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var expectedOutput = """
          usage: java -jar keep-a-changelog-updater-x.y.z-jar-with-dependencies.jar
                 -h | -s <arg>
           -h,--help             Print this help message
           -s,--scenario <arg>   Scenario to execute: create, add-entry, release,
                                 auto-generate
          """;

      try {
        // When
        commandLineService.getCommandLine(args);

        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testGetCommandLine_WhenMethodWithHelpArgumentsIsCalled_ThenHelpIsPrinted() throws URISyntaxException {
      // Given
      var args = new String[] {"-h"};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var expectedOutput = """
          usage: java -jar keep-a-changelog-updater-x.y.z-jar-with-dependencies.jar
                 -h | -s <arg>
           -h,--help             Print this help message
           -s,--scenario <arg>   Scenario to execute: create, add-entry, release,
                                 auto-generate
          """;

      try {
        // When
        commandLineService.getCommandLine(args);

        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOutput).isNotEmpty().isEqualTo(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testGetCommandLine_WhenMethodWithScenarioReleaseArgumentsAndMissingRequiredArgumentsIsCalled_ThenHelpIsPrinted() throws URISyntaxException {
      // Given
      var args = new String[] {"-s", "release"};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var expectedOutput = """
          usage: java -jar keep-a-changelog-updater-x.y.z-jar-with-dependencies.jar
                 -s release -b <arg> -c | -o <arg> -i <arg>  -r <arg> -rt <arg>
           -b,--branch <arg>          Main branch for link generation
           -c,--console               Output result to console instead of a file
           -i,--input <arg>           Path to the existing CHANGELOG.md file
           -o,--output <arg>          Path to the output file (default: input path)
           -r,--repository <arg>      Repository URL for link generation
           -rt,--release-type <arg>   Release type: major, minor, patch
          """;

      try {
        // When
        commandLineService.getCommandLine(args);
        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testGetCommandLine_WhenMethodWithScenarioAutoGenerateArgumentsWithoutAutoReleaseAndMissingRequiredArgumentsIsCalled_ThenHelpIsPrinted() throws URISyntaxException {
      // Given
      var args = new String[] {"-s", "auto-generate"};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var expectedOutput = """
              usage: java -jar keep-a-changelog-updater-x.y.z-jar-with-dependencies.jar
                     -s auto-generate [-a] [-b <arg>] -c | -o <arg> -g <arg> -i <arg>
                     [-p <arg>] [-r <arg>] [-v <arg>]
               -a,--auto-release            Create automatically a Release after log
                                            analysis. Ignores 'version' if set.
               -b,--branch <arg>            Main branch for link generation
               -c,--console                 Output result to console instead of a file
               -g,--git-log-path <arg>      Path to the git log file
               -i,--input <arg>             Path to the existing CHANGELOG.md file
               -o,--output <arg>            Path to the output file (default: input
                                            path)
               -p,--properties-path <arg>   Path to the custom properties file
               -r,--repository <arg>        Repository URL for link generation
               -v,--version <arg>           Existing release version (default:
                                            Unreleased)
               """;

      try {
        // When
        commandLineService.getCommandLine(args);
        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }

    @Test
    void testGetCommandLine_WhenMethodWithScenarioAutoGenerateArgumentsWithAutoReleaseAndMissingRequiredArgumentsIsCalled_ThenHelpIsPrinted() throws URISyntaxException {
      // Given
      var args = new String[] {"-s", "auto-generate", "-i", "/tmp/CHANGELOG.md", "-c", "-g", "/tmp/git-log.txt", "-a"};
      var byteArrayOutputStream = new ByteArrayOutputStream();
      var printStream = new PrintStream(byteArrayOutputStream);
      var originalOut = System.out;
      System.setOut(printStream);

      var expectedOutput = """
              usage: java -jar keep-a-changelog-updater-x.y.z-jar-with-dependencies.jar
                     -s auto-generate [-a] -b <arg> -c | -o <arg> -g <arg> -i <arg>  [-p
                     <arg>] -r <arg> [-v <arg>]
               -a,--auto-release            Create automatically a Release after log
                                            analysis. Ignores 'version' if set.
               -b,--branch <arg>            Main branch for link generation
               -c,--console                 Output result to console instead of a file
               -g,--git-log-path <arg>      Path to the git log file
               -i,--input <arg>             Path to the existing CHANGELOG.md file
               -o,--output <arg>            Path to the output file (default: input
                                            path)
               -p,--properties-path <arg>   Path to the custom properties file
               -r,--repository <arg>        Repository URL for link generation
               -v,--version <arg>           Existing release version (default:
                                            Unreleased)
               """;

      try {
        // When
        commandLineService.getCommandLine(args);
        // Then
        var consoleOutput = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        assertThat(consoleOutput).isNotEmpty().contains(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }
  }

  @ParameterizedTest
  @MethodSource ("provideArgsForGetScenarioMethod")
  void testGetScenarioAndNonNullCommandLine_WhenArgumentsContainsAllRequiredOptionsForScenario_ThenWantedScenarioIsReturnedAndCommandLineIsNotNull(String args, Scenario expectedScenario) throws URISyntaxException {
    // Given
    var arguments = args.split("\\s+");

    // When
    var commandLine = commandLineService.getCommandLine(arguments);
    var actualScenario = commandLineService.getScenario();

    // Then
    assertThat(actualScenario).isEqualTo(expectedScenario);
    assertThat(commandLine).isNotNull();
  }

  private static Stream<Arguments> provideArgsForGetScenarioMethod() {
    return Stream.of(
        Arguments.of("-s create -r https://git.example.com:443/organization/repo.git -b main -c", Scenario.CREATE),
        Arguments.of("-s add-entry -i /tmp/CHANGELOG.md -d Add_new_entry -t Added -c", Scenario.ADD_ENTRY),
        Arguments.of("-s release -i /tmp/CHANGELOG.md -r https://git.example.com:443/organization/repo.git -b main -rt Major -c", Scenario.RELEASE),
        Arguments.of("-s auto-generate -i /tmp/CHANGELOG.md -c -g /tmp/git-log.txt", Scenario.AUTO_GENERATE),
        Arguments.of("-s auto-generate -i /tmp/CHANGELOG.md -c -g /tmp/git-log.txt -a -r https://git.example.com:443/organization/repo.git -b main", Scenario.AUTO_GENERATE)
    );
  }
}
