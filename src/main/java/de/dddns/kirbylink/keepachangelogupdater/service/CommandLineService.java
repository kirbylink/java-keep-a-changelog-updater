package de.dddns.kirbylink.keepachangelogupdater.service;

import java.io.File;
import java.net.URISyntaxException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import de.dddns.kirbylink.keepachangelogupdater.model.Scenario;
import de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandLineService {

  @Getter
  private Scenario scenario;

  public CommandLine getCommandLine(String[] arguments) throws URISyntaxException {
    var options = getScenarioOptions();
    var commandLine = parseCommandLine(options, arguments, null);

    if (commandLine == null) {
      return null;
    }

    if (commandLine.hasOption(CommandLineConstants.OPTION_HELP_LONG)) {
      printHelp(options, null);
      return null;
    }

    try {
      scenario = Scenario.fromValue(commandLine.getOptionValue(CommandLineConstants.OPTION_SCENARIO_SHORT));
    } catch (IllegalArgumentException e) {
      log.info(e.getMessage());
      printHelp(options, null);
      return null;
    }
    switch (scenario) {
      case CREATE -> options = getScenarioCreateOptions();
      case ADD_ENTRY -> options = getScenarioAddEntryOptions();
      case RELEASE -> options = getScenarioReleaseOptions();
    }

    return parseCommandLine(options, commandLine.getArgs(), scenario);
  }

  protected Options getScenarioOptions() {

    var options = new Options();

    var optionGroup = new OptionGroup();
    optionGroup.addOption(Option.builder()
        .option(CommandLineConstants.OPTION_SCENARIO_SHORT)
        .longOpt(CommandLineConstants.OPTION_SCENARIO_LONG)
        .desc(CommandLineConstants.OPTION_SCENARIO_DESCRIPTION)
        .hasArg(true)
        .required()
        .build());
    optionGroup.addOption(
        Option.builder().option(CommandLineConstants.OPTION_HELP_SHORT)
        .longOpt(CommandLineConstants.OPTION_HELP_LONG)
        .desc(CommandLineConstants.OPTION_HELP_DESCRIPTION)
        .hasArg(false)
        .build());
    optionGroup.setRequired(true);

    options.addOptionGroup(optionGroup);

    return options;
  }

  protected Options getScenarioCreateOptions() {

    var options = new Options();

    getOutputOrConsoleOptions(options, CommandLineConstants.OPTION_OUTPUT_DESCRIPTION_CREATE);
    getRequiredRepositoryAndBranchOptions(options);
    options.addOption(CommandLineConstants.OPTION_DESCRIPTION_SHORT, CommandLineConstants.OPTION_DESCRIPTION_LONG, true, CommandLineConstants.OPTION_DESCRIPTION_DESCRIPTION);
    options.addOption(CommandLineConstants.OPTION_CATEGORY_SHORT, CommandLineConstants.OPTION_CATEGORY_LONG, true, CommandLineConstants.OPTION_CATEGORY_DESCRIPTION);

    return options;
  }

  protected Options getScenarioAddEntryOptions() {

    var options = new Options();

    options.addRequiredOption(CommandLineConstants.OPTION_INPUT_SHORT, CommandLineConstants.OPTION_INPUT_LONG, true, CommandLineConstants.OPTION_INPUT_DESCRIPTION);
    getOutputOrConsoleOptions(options, CommandLineConstants.OPTION_OUTPUT_DESCRIPTION_ADD_ENTRY_AND_RELEASE);
    options.addRequiredOption(CommandLineConstants.OPTION_DESCRIPTION_SHORT, CommandLineConstants.OPTION_DESCRIPTION_LONG, true, CommandLineConstants.OPTION_DESCRIPTION_DESCRIPTION);
    options.addRequiredOption(CommandLineConstants.OPTION_CATEGORY_SHORT, CommandLineConstants.OPTION_CATEGORY_LONG, true, CommandLineConstants.OPTION_CATEGORY_DESCRIPTION);
    options.addOption(CommandLineConstants.OPTION_VERSION_SHORT, CommandLineConstants.OPTION_VERSION_LONG, true, CommandLineConstants.OPTION_VERSION_DESCRIPTION);

    return options;
  }

  protected Options getScenarioReleaseOptions() {

    var options = new Options();

    options.addRequiredOption(CommandLineConstants.OPTION_INPUT_SHORT, CommandLineConstants.OPTION_INPUT_LONG, true, CommandLineConstants.OPTION_INPUT_DESCRIPTION);
    getOutputOrConsoleOptions(options, CommandLineConstants.OPTION_OUTPUT_DESCRIPTION_ADD_ENTRY_AND_RELEASE);
    getRequiredRepositoryAndBranchOptions(options);
    options.addRequiredOption(CommandLineConstants.OPTION_RELEASE_TYPE_SHORT, CommandLineConstants.OPTION_RELEASE_TYPE_LONG, true, CommandLineConstants.OPTION_RELEASE_TYPE_DESCRIPTION);

    return options;
  }

  private CommandLine parseCommandLine(Options options, String[] arguments, Scenario scenario) throws URISyntaxException {
    try {
      return new DefaultParser().parse(options, arguments, true);
    } catch (ParseException e) {
      printHelp(options, scenario);
      return null;
    }
  }

  private void getOutputOrConsoleOptions(Options options, String optionOutputDescription) {

    var optionGroup = new OptionGroup();
    optionGroup.addOption(Option.builder()
        .option(CommandLineConstants.OPTION_OUTPUT_SHORT)
        .longOpt(CommandLineConstants.OPTION_OUTPUT_LONG)
        .desc(optionOutputDescription)
        .optionalArg(true)
        .build());
    optionGroup.addOption(Option.builder()
        .option(CommandLineConstants.OPTION_CONSOLE_SHORT)
        .longOpt(CommandLineConstants.OPTION_CONSOLE_LONG)
        .desc(CommandLineConstants.OPTION_CONSOLE_DESCRIPTION_CREATE)
        .hasArg(false)
        .build());
    optionGroup.setRequired(true);

    options.addOptionGroup(optionGroup);
  }

  private void getRequiredRepositoryAndBranchOptions(Options options) {
    options.addRequiredOption(CommandLineConstants.OPTION_REPOSITORY_SHORT, CommandLineConstants.OPTION_REPOSITORY_LONG, true, CommandLineConstants.OPTION_REPOSITORY_DESCRIPTION);
    options.addRequiredOption(CommandLineConstants.OPTION_MAIN_BRANCH_SHORT, CommandLineConstants.OPTION_MAIN_BRANCH_LONG, true, CommandLineConstants.OPTION_MAIN_BRANCH_DESCRIPTION);
  }

  private void printHelp(Options options, Scenario scenario) throws URISyntaxException {
    var optionalJarName = getJarName();
    var program = optionalJarName.endsWith(".jar") ? optionalJarName : "keep-a-changelog-updater-x.y.z-jar-with-dependencies.jar";
    var cmdLineSyntax = scenario != null ? String.format("java -jar %s -s %s", program, scenario.getValue()) : String.format("java -jar %s", program);
    new HelpFormatter().printHelp(cmdLineSyntax, options, true);
  }

  private String getJarName() throws URISyntaxException {
    var jarPath = CommandLineService.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
    return new File(jarPath).getName();
  }
}
