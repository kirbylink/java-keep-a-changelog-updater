package de.dddns.kirbylink.keepachangelogupdater.service.handler;

import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_INPUT_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_MAIN_BRANCH_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_RELEASE_TYPE_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_REPOSITORY_SHORT;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import de.dddns.kirbylink.keepachangelogupdater.converter.ChangelogConverter;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.ReleaseType;
import de.dddns.kirbylink.keepachangelogupdater.service.UpdateService;
import de.dddns.kirbylink.keepachangelogupdater.utility.FilesUtility;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReleaseScenarioHandler implements ScenarioHandler {

  private final UpdateService updateService;

  @Override
  public void handle(CommandLine commandLine) throws IOException {
    var input = commandLine.getOptionValue(OPTION_INPUT_SHORT);
    var changelogString = FilesUtility.readFile(input);

    var baseRepositoryUrl = commandLine.getOptionValue(OPTION_REPOSITORY_SHORT);
    var mainBranch = commandLine.getOptionValue(OPTION_MAIN_BRANCH_SHORT);

    var changelogConverter = new ChangelogConverter(baseRepositoryUrl, mainBranch);

    var entity = changelogConverter.convertToEntity(changelogString);

    var releaseType = ReleaseType.fromValue(commandLine.getOptionValue(OPTION_RELEASE_TYPE_SHORT));

    updateService.createNewRelease(entity, releaseType);

    FilesUtility.createOutput(commandLine, changelogConverter.convertToString(entity));
  }
}

