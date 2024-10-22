package de.dddns.kirbylink.keepachangelogupdater.service.handler;

import static de.dddns.kirbylink.keepachangelogupdater.service.CommandLineService.getOption;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CATEGORY_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_DESCRIPTION_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_INPUT_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_VERSION_SHORT;
import java.io.IOException;
import java.time.LocalDate;
import org.apache.commons.cli.CommandLine;
import de.dddns.kirbylink.keepachangelogupdater.converter.ChangelogConverter;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryType;
import de.dddns.kirbylink.keepachangelogupdater.service.UpdateService;
import de.dddns.kirbylink.keepachangelogupdater.utility.FilesUtility;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddEntryScenarioHandler implements ScenarioHandler {

  private final UpdateService updateService;

  @Override
  public void handle(CommandLine commandLine) throws IOException {
    var input = commandLine.getOptionValue(OPTION_INPUT_SHORT);
    var changelogString = FilesUtility.readFile(input);

    var changelogConverter = new ChangelogConverter("", "");

    var entity = changelogConverter.convertToEntity(changelogString);

    var description = commandLine.getOptionValue(OPTION_DESCRIPTION_SHORT);
    var category = CategoryType.fromValue(commandLine.getOptionValue(OPTION_CATEGORY_SHORT));
    var releaseVersion = getOption(commandLine, OPTION_VERSION_SHORT).orElse("Unreleased");
    var date = commandLine.hasOption(OPTION_VERSION_SHORT) ? LocalDate.now().toString() : "";

    updateService.updateChangelog(entity, description, category, releaseVersion, date);

    FilesUtility.createOutput(commandLine, changelogConverter.convertToString(entity));
  }
}

