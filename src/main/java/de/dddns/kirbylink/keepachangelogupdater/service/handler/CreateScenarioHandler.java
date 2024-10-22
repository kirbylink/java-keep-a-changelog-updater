package de.dddns.kirbylink.keepachangelogupdater.service.handler;

import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CATEGORY_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_DESCRIPTION_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_MAIN_BRANCH_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_REPOSITORY_SHORT;
import org.apache.commons.cli.CommandLine;
import de.dddns.kirbylink.keepachangelogupdater.converter.ChangelogConverter;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Entity;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryType;
import de.dddns.kirbylink.keepachangelogupdater.service.UpdateService;
import de.dddns.kirbylink.keepachangelogupdater.utility.FilesUtility;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateScenarioHandler implements ScenarioHandler {

  private final UpdateService updateService;

  @Override
  public void handle(CommandLine commandLine) {
      var baseRepositoryUrl = commandLine.getOptionValue(OPTION_REPOSITORY_SHORT);
      var mainBranch = commandLine.getOptionValue(OPTION_MAIN_BRANCH_SHORT);

      var changelogConverter = new ChangelogConverter(baseRepositoryUrl, mainBranch);

      var entity = changelogConverter.createDefaultEntity();

      if (commandLine.hasOption(OPTION_DESCRIPTION_SHORT)) {
        addEntryToUnreleaseVersion(commandLine, entity);
      }

      FilesUtility.createOutput(commandLine, changelogConverter.convertToString(entity));
  }

  private void addEntryToUnreleaseVersion(CommandLine commandLine, Entity entity) {
    var description = commandLine.getOptionValue(OPTION_DESCRIPTION_SHORT);
    var category = CategoryType.fromValue(commandLine.getOptionValue(OPTION_CATEGORY_SHORT));
    var releaseVersion = "Unreleased";
    var date = "";
    updateService.updateChangelog(entity, description, category, releaseVersion, date);
  }
}

