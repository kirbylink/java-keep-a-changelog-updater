package de.dddns.kirbylink.keepachangelogupdater.converter;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import de.dddns.kirbylink.keepachangelogupdater.config.ConventionalCommitConfiguration;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.ReleaseType;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Version;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.VersionEntry;
import de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits.Commit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConventionalCommitConverter {

  private final ConventionalCommitConfiguration conventionalCommitConfiguration;

  public Version convertCommitsToChangelogEntries(List<Commit> commits, Version version) {
    commits.forEach(commit -> addCommitToCategory(version, commit));
    return version;
  }

  public ReleaseType determineReleaseType(List<Commit> commits) {
    return Stream
        .of(new AbstractMap.SimpleEntry<>(ReleaseType.MAJOR, hasMajorChange(commits)),
            new AbstractMap.SimpleEntry<>(ReleaseType.MINOR, hasMinorChange(commits)),
            new AbstractMap.SimpleEntry<>(ReleaseType.PATCH, hasPatchChange(commits)))
        .filter(Map.Entry::getValue)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);
  }


  private void addCommitToCategory(Version version, Commit commit) {
    var type = commit.getType();
    var description = commit.getDescription();
    var breakingChange = commit.getBreakingChange();

    Map<List<String>, Consumer<Version>> categoryMapping = Map.of(
      conventionalCommitConfiguration.getAddedTypes(), v -> v.getAdded().getEntries().add(VersionEntry.builder().description(description).build()),
      conventionalCommitConfiguration.getFixedTypes(), v -> v.getFixed().getEntries().add(VersionEntry.builder().description(description).build()),
      conventionalCommitConfiguration.getChangedTypes(), v -> v.getChanged().getEntries().add(VersionEntry.builder().description(description).build()),
      conventionalCommitConfiguration.getRemovedTypes(), v -> v.getRemoved().getEntries().add(VersionEntry.builder().description(description).build())
    );

    categoryMapping.forEach((types, consumer) -> {
      if (types.contains(type)) {
        consumer.accept(version);
      }
    });

    if (commit.hasBreakingChange()) {
      mergeBreakingChanges(version, breakingChange);
    }
  }

  private void mergeBreakingChanges(Version version, String breakingChange) {
    if (version.hasBreakingChange()) {
      var existingBreakingChange = version.getBreakingChange();
      version.setBreakingChange(existingBreakingChange + "\n\n" + breakingChange);
    } else {
      version.setBreakingChange(breakingChange);
    }
  }

  private boolean hasMajorChange(List<Commit> commits) {
    return commits.stream().anyMatch(commit -> commit.hasBreakingChange() || conventionalCommitConfiguration.getMajorTypes().contains(commit.getType()));
  }

  private boolean hasMinorChange(List<Commit> commits) {
    return commits.stream().anyMatch(commit -> conventionalCommitConfiguration.getMinorTypes().contains(commit.getType()));
  }

  private boolean hasPatchChange(List<Commit> commits) {
    return commits.stream().anyMatch(commit -> conventionalCommitConfiguration.getPatchTypes().contains(commit.getType()));
  }
}


