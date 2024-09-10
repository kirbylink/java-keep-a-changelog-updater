package de.dddns.kirbylink.keepachangelogupdater;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import org.apache.commons.cli.CommandLine;
import de.dddns.kirbylink.keepachangelogupdater.converter.ChangelogConverter;
import de.dddns.kirbylink.keepachangelogupdater.model.ReleaseType;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryType;
import de.dddns.kirbylink.keepachangelogupdater.service.CommandLineService;
import de.dddns.kirbylink.keepachangelogupdater.service.UpdateService;
import de.dddns.kirbylink.keepachangelogupdater.utility.FilesUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeepAChangelogUpdaterApplication {

  public static void main(String[] args) throws URISyntaxException {
    var commandLineService = new CommandLineService();
    var commandLine = commandLineService.getCommandLine(args);

    if (commandLine == null) {
      return;
    }

    var scenario = commandLineService.getScenario();
    var updateService = new UpdateService();
    switch (scenario) {
      case CREATE -> handleCreateScenario(commandLine, updateService);
      case ADD_ENTRY -> handleAddEntryScenario(commandLine, updateService);
      case RELEASE -> handleReleaseVersionScenario(commandLine, updateService);
      default -> throw new IllegalArgumentException("Unexpected value: " + scenario);
    }
  }

  private static void handleCreateScenario(CommandLine commandLine, UpdateService updateService) {
    var baseRepositoryUrl = commandLine.getOptionValue("r");
    var mainBranch = commandLine.getOptionValue("b");
    var changelogConverter = new ChangelogConverter(baseRepositoryUrl, mainBranch);
    var entity = changelogConverter.createDefaultEntity();

    if (commandLine.hasOption("d")) {
      var description = commandLine.getOptionValue("d");
      var category = CategoryType.fromValue(commandLine.getOptionValue("t"));
      var releaseVersion = "Unreleased";
      var date = "";
      updateService.updateChangelog(entity, description, category, releaseVersion, date);
    }

    createOutput(commandLine, changelogConverter.convertToString(entity));
  }

  private static void handleAddEntryScenario(CommandLine commandLine, UpdateService updateService) {
    var input = commandLine.getOptionValue("i");
    var changelogString = readFile(input);

    if (changelogString != null) {
      var changelogConverter = new ChangelogConverter("", "");
      var entity = changelogConverter.convertToEntity(changelogString);
      var description = commandLine.getOptionValue("d");
      var category = CategoryType.fromValue(commandLine.getOptionValue("t"));
      var releaseVersion = commandLine.hasOption("v") ? commandLine.getOptionValue("v") : "Unreleased";
      var date = commandLine.hasOption("v") ? LocalDate.now().toString() : "";
      updateService.updateChangelog(entity, description, category, releaseVersion, date);
      createOutput(commandLine, changelogConverter.convertToString(entity));
    }
  }

  private static void handleReleaseVersionScenario(CommandLine commandLine, UpdateService updateService) {
    var input = commandLine.getOptionValue("i");
    var changelogString = readFile(input);

    if (changelogString != null) {
      var baseRepositoryUrl = commandLine.getOptionValue("r");
      var mainBranch = commandLine.getOptionValue("b");
      var changelogConverter = new ChangelogConverter(baseRepositoryUrl, mainBranch);
      var entity = changelogConverter.convertToEntity(changelogString);
      var releaseType = ReleaseType.fromValue(commandLine.getOptionValue("rt"));
      updateService.createNewRelease(entity, releaseType);
      createOutput(commandLine, changelogConverter.convertToString(entity));
    }
  }

  private static String readFile(String path) {
    try {
      return FilesUtility.readFileAsString(path);
    } catch (IOException e) {
      log.warn("Could not read file: {}", path, e);
      return null;
    }
  }

  private static void createOutput(CommandLine commandLine, String changelog) {
    if (commandLine.hasOption("c")) {
      log.info(changelog);
    } else {
      var output = commandLine.getOptionValue("o");
      var filePath = output == null ? commandLine.getOptionValue("i") : output;
      try {
        FilesUtility.writeStringAsFile(filePath, changelog);
        log.info("Changelog saved to {}", filePath);
      } catch (IOException e) {
        log.warn("Couldn't save content to {}", filePath, e);
        log.info(changelog);
      }
    }
  }
}
