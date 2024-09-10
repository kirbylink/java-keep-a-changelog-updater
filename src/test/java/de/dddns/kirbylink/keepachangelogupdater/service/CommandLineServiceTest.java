package de.dddns.kirbylink.keepachangelogupdater.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import de.dddns.kirbylink.keepachangelogupdater.model.Scenario;

class CommandLineServiceTest {

  private CommandLineService commandLineService;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() {
    commandLineService = new CommandLineService();
  }

  @AfterEach
  void tearDown() throws Exception {}

  @Nested
  @DisplayName("Test for get options")
  class TestGetOptions {

    @Test
    void testGetScenarioOptions_WhenMethodWithScenarioIsCalled_ThenOptionsWithAdditionalHelpOptionIsReturned() {

      // Given

      // When
      var options = commandLineService.getScenarioOptions();

      // Then
      assertThat(options.getOptions()).hasSize(2);

      assertThat(options.hasOption("s")).isTrue();
      assertThat(options.getOption("s").isRequired()).isFalse();
      assertThat(options.getOption("s").hasArg()).isTrue();
      assertThat(options.getOption("s").getLongOpt()).isEqualTo("scenario");
      assertThat(options.getOption("s").getDescription()).isEqualTo("Scenario to execute: create, add-entry, release");

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
      assertThat(options.getOptions()).hasSize(6);

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
      assertThat(options.getOptions()).hasSize(6);

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
      assertThat(options.getOptions()).hasSize(6);

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
           -s,--scenario <arg>   Scenario to execute: create, add-entry, release
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
           -s,--scenario <arg>   Scenario to execute: create, add-entry, release
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
    void testGetCommandLine_WhenMethodWithScenarioCreateArgumentsIsCalled_ThenCommandLineWithRequiredOptionsIsReturned() throws URISyntaxException {

      // Given
      var args = new String[] {"-s", "create", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-c"};

      // When
      var commandLine = commandLineService.getCommandLine(args);

      assertThat(commandLine.getOptions()).hasSize(3);
      assertThat(commandLine.hasOption("r")).isTrue();
      assertThat(commandLine.hasOption("b")).isTrue();
      assertThat(commandLine.hasOption("c")).isTrue();
    }

    @Test
    void testGetCommandLine_WhenMethodWithScenarioAddEntryArgumentsIsCalled_ThenCommandLineWithRequiredOptionsIsReturned() throws URISyntaxException {

      // Given
      var args = new String[] {"-s", "add-entry", "-i", "/tmp/CHANGELOG.md", "-d", "Add new entry", "-t", "Added", "-c"};

      // When
      var commandLine = commandLineService.getCommandLine(args);

      assertThat(commandLine.getOptions()).hasSize(4);
      assertThat(commandLine.hasOption("i")).isTrue();
      assertThat(commandLine.hasOption("d")).isTrue();
      assertThat(commandLine.hasOption("t")).isTrue();
      assertThat(commandLine.hasOption("c")).isTrue();
    }

    @Test
    void testGetCommandLine_WhenMethodWithScenarioReleaseArgumentsIsCalled_ThenCommandLineWithRequiredOptionsIsReturned() throws URISyntaxException {

      // Given
      var args = new String[] {"-s", "release", "-i", "/tmp/CHANGELOG.md", "-r", "https://git.example.com:443/organization/repo.git", "-b", "main", "-rt", "Major", "-c"};

      // When
      var commandLine = commandLineService.getCommandLine(args);

      assertThat(commandLine.getOptions()).hasSize(5);
      assertThat(commandLine.hasOption("i")).isTrue();
      assertThat(commandLine.hasOption("r")).isTrue();
      assertThat(commandLine.hasOption("b")).isTrue();
      assertThat(commandLine.hasOption("rt")).isTrue();
      assertThat(commandLine.hasOption("c")).isTrue();
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
        assertThat(consoleOutput).isNotEmpty().isEqualTo(expectedOutput);
      } finally {
        System.setOut(originalOut);
      }
    }
  }

  @ParameterizedTest
  @MethodSource ("provideArgsForGetScenarioMethod")
  void testGetScenario_WhenArgumentsContainsAllRequiredOptionsForScenario_ThenWantedScenarioIsReturned(String args, Scenario expectedScenario) throws URISyntaxException {

    // Given
    commandLineService.getCommandLine(args.split("\\s+"));

    // When
    var actualScenario = commandLineService.getScenario();

    // Then
    assertThat(actualScenario).isEqualTo(expectedScenario);
  }

  private static Stream<Arguments> provideArgsForGetScenarioMethod() {
    return Stream.of(
        Arguments.of("-s create -r https://git.example.com:443/organization/repo.git -b main -c", Scenario.CREATE),
        Arguments.of("-s add-entry -i /tmp/CHANGELOG.md -d Add new entry -t Added -c", Scenario.ADD_ENTRY),
        Arguments.of("-s release -i /tmp/CHANGELOG.md -r https://git.example.com:443/organization/repo.git -b main -rt Major -c", Scenario.RELEASE)
    );
  }
}
