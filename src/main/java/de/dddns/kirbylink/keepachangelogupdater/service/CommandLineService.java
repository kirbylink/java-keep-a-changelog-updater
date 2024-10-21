package de.dddns.kirbylink.keepachangelogupdater.service;

import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_AUTO_RELEASE_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_AUTO_RELEASE_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_AUTO_RELEASE_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CATEGORY_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CATEGORY_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CATEGORY_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CONSOLE_DESCRIPTION_CREATE;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CONSOLE_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CONSOLE_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CUTOM_PROPERTIES_PATH_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CUTOM_PROPERTIES_PATH_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CUTOM_PROPERTIES_PATH_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_DESCRIPTION_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_DESCRIPTION_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_DESCRIPTION_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_GIT_LOG_PATH_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_GIT_LOG_PATH_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_GIT_LOG_PATH_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_HELP_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_HELP_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_HELP_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_INPUT_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_INPUT_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_INPUT_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_MAIN_BRANCH_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_MAIN_BRANCH_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_MAIN_BRANCH_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_OUTPUT_DESCRIPTION_ADD_ENTRY_AND_RELEASE;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_OUTPUT_DESCRIPTION_CREATE;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_OUTPUT_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_OUTPUT_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_RELEASE_TYPE_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_RELEASE_TYPE_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_RELEASE_TYPE_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_REPOSITORY_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_REPOSITORY_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_REPOSITORY_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_SCENARIO_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_SCENARIO_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_SCENARIO_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_VERSION_DESCRIPTION;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_VERSION_LONG;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_VERSION_SHORT;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Scenario;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandLineService {

  @Getter
  private Scenario scenario;
  private final Options scenarioOptions = getScenarioOptions();

  public CommandLine getCommandLine(String[] arguments) throws URISyntaxException {
    var commandLine = parseCommandLine(scenarioOptions, arguments, null);

    if (commandLine == null) {
      return null;
    }

    if (commandLine.hasOption(OPTION_HELP_SHORT)) {
      printHelp(scenarioOptions, null);
      return null;
    }

    try {
      scenario = Scenario.fromValue(commandLine.getOptionValue(OPTION_SCENARIO_SHORT));
    } catch (IllegalArgumentException e) {
      log.info(e.getMessage());
      printHelp(scenarioOptions, null);
      return null;
    }

    Options options = null;
    switch (scenario) {
      case CREATE -> options = getScenarioCreateOptions();
      case ADD_ENTRY -> options = getScenarioAddEntryOptions();
      case RELEASE -> options = getScenarioReleaseOptions();
      case AUTO_GENERATE -> options = getScenarioAutoGenerateOptions(hasAutoRelease(arguments));
    }

    return Optional.ofNullable(options).isPresent() ? parseCommandLine(options, commandLine.getArgs(), scenario) : null;
  }

  public static Optional<String> getOption(CommandLine commandLine, String option) {
    return Optional.ofNullable(commandLine.getOptionValue(option));
  }

  protected Options getScenarioOptions() {

    var options = new Options();

    var optionGroup = new OptionGroup();
    optionGroup.addOption(Option.builder().option(OPTION_SCENARIO_SHORT).longOpt(OPTION_SCENARIO_LONG).desc(OPTION_SCENARIO_DESCRIPTION).hasArg(true).required().build());
    optionGroup.addOption(Option.builder().option(OPTION_HELP_SHORT).longOpt(OPTION_HELP_LONG).desc(OPTION_HELP_DESCRIPTION).hasArg(false).build());
    optionGroup.setRequired(true);

    options.addOptionGroup(optionGroup);

    return options;
  }

  protected Options getScenarioCreateOptions() {

    var options = new Options();

    getOutputOrConsoleOptions(options, OPTION_OUTPUT_DESCRIPTION_CREATE);
    getRequiredRepositoryAndBranchOptions(options);
    options.addOption(OPTION_DESCRIPTION_SHORT, OPTION_DESCRIPTION_LONG, true, OPTION_DESCRIPTION_DESCRIPTION);
    options.addOption(OPTION_CATEGORY_SHORT, OPTION_CATEGORY_LONG, true, OPTION_CATEGORY_DESCRIPTION);

    return options;
  }

  protected Options getScenarioAddEntryOptions() {

    var options = new Options();

    options.addRequiredOption(OPTION_INPUT_SHORT, OPTION_INPUT_LONG, true, OPTION_INPUT_DESCRIPTION);
    getOutputOrConsoleOptions(options, OPTION_OUTPUT_DESCRIPTION_ADD_ENTRY_AND_RELEASE);
    options.addRequiredOption(OPTION_DESCRIPTION_SHORT, OPTION_DESCRIPTION_LONG, true, OPTION_DESCRIPTION_DESCRIPTION);
    options.addRequiredOption(OPTION_CATEGORY_SHORT, OPTION_CATEGORY_LONG, true, OPTION_CATEGORY_DESCRIPTION);
    options.addOption(OPTION_VERSION_SHORT, OPTION_VERSION_LONG, true, OPTION_VERSION_DESCRIPTION);

    return options;
  }

  protected Options getScenarioReleaseOptions() {

    var options = new Options();

    options.addRequiredOption(OPTION_INPUT_SHORT, OPTION_INPUT_LONG, true, OPTION_INPUT_DESCRIPTION);
    getOutputOrConsoleOptions(options, OPTION_OUTPUT_DESCRIPTION_ADD_ENTRY_AND_RELEASE);
    getRequiredRepositoryAndBranchOptions(options);
    options.addRequiredOption(OPTION_RELEASE_TYPE_SHORT, OPTION_RELEASE_TYPE_LONG, true, OPTION_RELEASE_TYPE_DESCRIPTION);

    return options;
  }

  protected Options getScenarioAutoGenerateOptions(boolean hasAutoReleaseOption) {

    var options = new Options();

    options.addRequiredOption(OPTION_INPUT_SHORT, OPTION_INPUT_LONG, true, OPTION_INPUT_DESCRIPTION);
    options.addRequiredOption(OPTION_GIT_LOG_PATH_SHORT, OPTION_GIT_LOG_PATH_LONG, true, OPTION_GIT_LOG_PATH_DESCRIPTION);
    getOutputOrConsoleOptions(options, OPTION_OUTPUT_DESCRIPTION_ADD_ENTRY_AND_RELEASE);
    options.addOption(OPTION_VERSION_SHORT, OPTION_VERSION_LONG, true, OPTION_VERSION_DESCRIPTION);
    options.addOption(OPTION_AUTO_RELEASE_SHORT, OPTION_AUTO_RELEASE_LONG, false, OPTION_AUTO_RELEASE_DESCRIPTION);
    options.addOption(OPTION_CUTOM_PROPERTIES_PATH_SHORT, OPTION_CUTOM_PROPERTIES_PATH_LONG, true, OPTION_CUTOM_PROPERTIES_PATH_DESCRIPTION);

    if (hasAutoReleaseOption) {
      getRequiredRepositoryAndBranchOptions(options);
    } else {
      options.addOption(OPTION_REPOSITORY_SHORT, OPTION_REPOSITORY_LONG, true, OPTION_REPOSITORY_DESCRIPTION);
      options.addOption(OPTION_MAIN_BRANCH_SHORT, OPTION_MAIN_BRANCH_LONG, true, OPTION_MAIN_BRANCH_DESCRIPTION);
    }

    return options;
  }

  private boolean hasAutoRelease(String[] arguments) {
    return Stream.of(arguments).anyMatch(argument -> argument.equals("-" + OPTION_AUTO_RELEASE_SHORT) || argument.equals("-" + OPTION_AUTO_RELEASE_LONG));
  }

  private CommandLine parseCommandLine(Options options, String[] arguments, Scenario scenario) throws URISyntaxException {
    try {
      return new DefaultParser().parse(options, arguments, true);
    } catch (ParseException e) {
      log.info(e.getMessage());
      printHelp(options, scenario);
      return null;
    }
  }

  private void getOutputOrConsoleOptions(Options options, String optionOutputDescription) {

    var optionGroup = new OptionGroup();
    optionGroup.addOption(Option.builder().option(OPTION_OUTPUT_SHORT).longOpt(OPTION_OUTPUT_LONG).desc(optionOutputDescription).optionalArg(true).build());
    optionGroup.addOption(Option.builder().option(OPTION_CONSOLE_SHORT).longOpt(OPTION_CONSOLE_LONG).desc(OPTION_CONSOLE_DESCRIPTION_CREATE).hasArg(false).build());
    optionGroup.setRequired(true);

    options.addOptionGroup(optionGroup);
  }

  private void getRequiredRepositoryAndBranchOptions(Options options) {
    options.addRequiredOption(OPTION_REPOSITORY_SHORT, OPTION_REPOSITORY_LONG, true, OPTION_REPOSITORY_DESCRIPTION);
    options.addRequiredOption(OPTION_MAIN_BRANCH_SHORT, OPTION_MAIN_BRANCH_LONG, true, OPTION_MAIN_BRANCH_DESCRIPTION);
  }

  private void printHelp(Options options, Scenario scenario) {
    var optionalJarName = Optional.ofNullable(getJarName());
    optionalJarName.ifPresent(jarName -> printHelpWithJarName(options, scenario, jarName));
  }

  private void printHelpWithJarName(Options options, Scenario scenario, String jarName) {
    var program = jarName.endsWith(".jar") ? jarName : "keep-a-changelog-updater-x.y.z-jar-with-dependencies.jar";
    var cmdLineSyntax = scenario != null ? String.format("java -jar %s -s %s", program, scenario.getValue()) : String.format("java -jar %s", program);
    new HelpFormatter().printHelp(cmdLineSyntax, options, true);
  }

  private String getJarName() {
    try {
      var jarPath = CommandLineService.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
      return new File(jarPath).getName();
    } catch (URISyntaxException e) {
      return null;
    }
  }
}
