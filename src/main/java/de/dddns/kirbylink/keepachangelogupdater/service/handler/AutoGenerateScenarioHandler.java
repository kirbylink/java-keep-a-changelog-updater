package de.dddns.kirbylink.keepachangelogupdater.service.handler;

import static de.dddns.kirbylink.keepachangelogupdater.service.CommandLineService.getOption;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_AUTO_RELEASE_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CUTOM_PROPERTIES_PATH_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_GIT_LOG_PATH_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_INPUT_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_MAIN_BRANCH_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_REPOSITORY_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_VERSION_SHORT;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import de.dddns.kirbylink.keepachangelogupdater.config.ConventionalCommitConfiguration;
import de.dddns.kirbylink.keepachangelogupdater.converter.ChangelogConverter;
import de.dddns.kirbylink.keepachangelogupdater.converter.ConventionalCommitConverter;
import de.dddns.kirbylink.keepachangelogupdater.converter.GitLogParser;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Entity;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Version;
import de.dddns.kirbylink.keepachangelogupdater.service.ConventionalCommitParserService;
import de.dddns.kirbylink.keepachangelogupdater.service.UpdateService;
import de.dddns.kirbylink.keepachangelogupdater.utility.FilesUtility;
import de.dddns.kirbylink.keepachangelogupdater.utility.VersionUtility;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutoGenerateScenarioHandler implements ScenarioHandler {

  private final UpdateService updateService;

  @Override
  public void handle(CommandLine commandLine) throws IOException {
    var input = commandLine.getOptionValue(OPTION_INPUT_SHORT);
    var changelogString = FilesUtility.readFile(input);

    var gitLogPath = commandLine.getOptionValue(OPTION_GIT_LOG_PATH_SHORT);
    var gitLog = FilesUtility.readFile(gitLogPath);

    var pathToConfig = getOption(commandLine, OPTION_CUTOM_PROPERTIES_PATH_SHORT).orElse(null);
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(pathToConfig);

    var changelogConverter = getChangelogConverter(commandLine);

    var entity = changelogConverter.convertToEntity(changelogString);
    var version = getVersion(commandLine, entity);

    var conventionalCommitParserService = new ConventionalCommitParserService(new GitLogParser());

    var commits = conventionalCommitParserService.parseGitLog(gitLog);

    var conventionalCommitConverter = new ConventionalCommitConverter(conventionalCommitConfiguration);

    conventionalCommitConverter.convertCommitsToChangelogEntries(commits, version);

    if (commandLine.hasOption(OPTION_AUTO_RELEASE_SHORT)) {
      var optionalReleaseType = Optional.ofNullable(conventionalCommitConverter.determineReleaseType(commits));
      optionalReleaseType.ifPresent(releaseType -> updateService.createNewRelease(entity, releaseType));
    }

    FilesUtility.createOutput(commandLine, changelogConverter.convertToString(entity));
  }

  private ChangelogConverter getChangelogConverter(CommandLine commandLine) {
    var baseRepositoryUrl = getOptionValueIfPresent(commandLine, OPTION_REPOSITORY_SHORT);
    var mainBranch = getOptionValueIfPresent(commandLine, OPTION_MAIN_BRANCH_SHORT);

    return new ChangelogConverter(baseRepositoryUrl, mainBranch);
  }

  private String getOptionValueIfPresent(CommandLine commandLine, String option) {
    return getOption(commandLine, option).orElse("");
  }

  private Version getVersion(CommandLine commandLine, Entity entity) {
    var version = getOption(commandLine, OPTION_VERSION_SHORT).orElse("Unreleased");
    return VersionUtility.getOptinalVersion(entity, version).orElse(entity.getVersions().get(0));
  }
}

