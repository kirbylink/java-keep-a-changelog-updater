package de.dddns.kirbylink.keepachangelogupdater.service;

import java.util.List;
import java.util.Objects;
import de.dddns.kirbylink.keepachangelogupdater.converter.GitLogParser;
import de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits.Commit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ConventionalCommitParserService {

  private final GitLogParser gitLogParser;

  public List<Commit> parseGitLog(String gitLog) {
    log.debug("parsing git log");
    var individualGitCommits = gitLogParser.splitGitLog(gitLog);

    var conventionalCommits = individualGitCommits.stream()
        .map(gitLogParser::parseGitCommit)
        .filter(Objects::nonNull)
        .toList();

    log.debug("Conventional commits collected: {}", conventionalCommits);
    log.debug("Merging commits with similar type and description and merging optional breaking change informations.");
    return gitLogParser.filterCommitsAndMergeOptionalBreakingChanges(conventionalCommits);
  }
}
