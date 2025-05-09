package de.dddns.kirbylink.keepachangelogupdater.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits.Commit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GitLogParser {

  private static final Pattern COMMIT_PATTERN = Pattern.compile("""
      commit\\s+([a-f0-9]{40})\\n\
      Author:.*?\\n\
      Date:.*?\\n\\n\
      \\s*(.*?)(?=\\n\\ncommit|$)
      """, Pattern.DOTALL);

  private static final Pattern CONVENTIONAL_COMMIT_MESSAGE_PATTERN = Pattern.compile("^(\\w+)(?:\\(([^)]+)\\))?(!)?:\\s+(.*)$");

  public List<String> splitGitLog(String gitLog) {
    return Arrays.stream(gitLog.split("(?m)(?=^commit\\s+[a-f0-9]{40}\\b)"))
        .map(commitString -> commitString.startsWith("commit") ? commitString : "commit " + commitString)
        .toList();
  }


  public Commit parseGitCommit(String individualCommit) {
    var matcher = COMMIT_PATTERN.matcher(individualCommit);
    if (matcher.find()) {
      var hashCode = matcher.group(1).trim();
      var fullMessage = matcher.group(2).trim();

      var messageLines = fullMessage.split(System.lineSeparator(), 2);
      var firstLine = messageLines[0].trim();

      var conventionalCommitMessageMatcher = CONVENTIONAL_COMMIT_MESSAGE_PATTERN.matcher(firstLine);
      if (conventionalCommitMessageMatcher.find()) {
        return parseConventionalCommitMessage(conventionalCommitMessageMatcher, messageLines, hashCode);
      }
      log.debug("Skipping commit: {} - does not follow Conventional Commits", hashCode);
      return null;
    }
    log.debug("Skipping commit: {} - is not a git commit", individualCommit);
    return null;
  }

  public List<Commit> filterCommitsAndMergeOptionalBreakingChanges(List<Commit> commits) {
    log.debug("Filtering duplicated commits and merging optional breaking Change in body");
    Map<String, Commit> uniqueCommits = new HashMap<>();

    for (Commit commit : commits) {
      var key = commit.getType() + ":" + commit.getDescription();
      if (uniqueCommits.containsKey(key)) {
        var existingCommit = uniqueCommits.get(key);
        if (commit.hasBreakingChange()) {
          var mergedBreakingChange = mergeBreakingChange(existingCommit, commit);
          existingCommit.setBreakingChange(mergedBreakingChange);
        }
      } else {
        uniqueCommits.put(key, commit);
      }
    }
    log.debug("Filtered commits: {}", uniqueCommits);
    return new ArrayList<>(uniqueCommits.values());
  }

  private Commit parseConventionalCommitMessage(Matcher conventionalCommitMessageMatcher, String[] messageLines, String hashCode) {
    var type = new StringBuilder();
    type.append(conventionalCommitMessageMatcher.group(1));
    var optionalScope = Optional.ofNullable(conventionalCommitMessageMatcher.group(2));
    var breakingChangeFlag = conventionalCommitMessageMatcher.group(3);
    var description = conventionalCommitMessageMatcher.group(4);

    var body = messageLines.length > 1 ? trimBodyLines(messageLines[1].trim()) : null;

    var breakingChange = extractBreakingChange(body);

    log.trace("Hash: {}", hashCode);
    log.trace("Type: {}", type);
    log.trace("Optional Scope: {}", optionalScope);
    log.trace("BreakingChange Flag: {}", breakingChangeFlag);
    log.trace("Description: {}", description);
    log.trace("Body: {}", body);
    log.trace("BreakingChange: {}", breakingChange);
    log.trace("\"!\".equals(breakingChangeFlag: {}", "!".equals(breakingChangeFlag));
    log.trace("breakingChange != null: {}", breakingChange != null);

    if (optionalScope.isPresent()) {
      type = type.append("(").append(optionalScope.get()).append(")");
    }

    return Commit.builder()
        .hashCode(hashCode)
        .type(type.toString())
        .description(description)
        .body(body)
        .breakingChange(breakingChange)
        .hasBreakingChange("!".equals(breakingChangeFlag) || breakingChange != null)
        .build();
  }

  private String trimBodyLines(String unformattedBody) {
    return Arrays.stream(unformattedBody.split("\\n")).map(String::trim).collect(Collectors.joining("\n"));
  }

  private String extractBreakingChange(String body) {
    if (body != null && body.contains("BREAKING CHANGE:")) {
      return body.split("BREAKING CHANGE:", 2)[1].trim();
    }
    return null;
  }

  private String mergeBreakingChange(Commit existingCommit, Commit newCommitWithBreakingChange) {
    if (!existingCommit.hasBreakingChange()) {
      return newCommitWithBreakingChange.getBreakingChange();
    }
    return existingCommit.getBreakingChange() + System.lineSeparator() + newCommitWithBreakingChange.getBreakingChange();
  }
}
