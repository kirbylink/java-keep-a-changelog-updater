package de.dddns.kirbylink.keepachangelogupdater.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import de.dddns.kirbylink.keepachangelogupdater.model.Entity;
import de.dddns.kirbylink.keepachangelogupdater.model.ReleaseType;
import de.dddns.kirbylink.keepachangelogupdater.model.Version;
import de.dddns.kirbylink.keepachangelogupdater.model.VersionEntry;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryAdded;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryChanged;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryFixed;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryRemoved;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryType;

public class UpdateService {

  private static final String UNRELEASED = "Unreleased";

  public void updateChangelog(Entity entity, String description, CategoryType category, String releaseVersion, String date) {

    var entry = VersionEntry.builder().description(description).build();

    var existingVersion = entity.getVersions().stream().filter(version -> version.getReleaseVersion().equals(releaseVersion)).findFirst();

    if (existingVersion.isPresent()) {
      existingVersion.get().getCategory(category).getEntries().add(0, entry);
      existingVersion.get().setDate(date);
    } else {
      var index = hasUnreleasedVersion(entity) ? 1 : 0;
      var version = createVersion(entry, category, releaseVersion, date);
      entity.getVersions().add(index, version);
    }
  }

  public void createNewRelease(Entity entity, ReleaseType release) {

    if (!hasUnreleasedVersion(entity)) {
      return;
    }

    var versions = entity.getVersions();

    if (versions.get(0).getAdded().getEntries().isEmpty() &&
        versions.get(0).getChanged().getEntries().isEmpty() &&
        versions.get(0).getFixed().getEntries().isEmpty() &&
        versions.get(0).getRemoved().getEntries().isEmpty()) {
      return;
    }

    var releaseVersionLatest = versions.size() > 1 ? versions.get(1).getReleaseVersion() : "0.0.0";

    var releaseVersionNew = generateNewVersion(releaseVersionLatest, release);

    var versionNew = createVersion(versions.get(0), releaseVersionNew);

    entity.getVersions().add(1, versionNew);
  }

  private String generateNewVersion(String releaseVersionOld, ReleaseType release) {

    if (release == null) {
      throw new IllegalArgumentException("Category cannot be null");
    }

    var pattern = Pattern.compile("^(\\D*?)(\\d+\\.\\d+\\.\\d+)$");
    var matcher = pattern.matcher(releaseVersionOld);

    if (!matcher.matches()) {
        throw new IllegalArgumentException("Invalid version format: " + releaseVersionOld);
    }

    var prefix = matcher.group(1);
    var version = matcher.group(2);

    var parts = version.split("\\.");

    switch (release) {
      case MAJOR:
        parts[0] = String.valueOf(Integer.parseInt(parts[0]) + 1);
        parts[1] = "0";
        parts[2] = "0";
        break;
      case MINOR:
        parts[1] = String.valueOf(Integer.parseInt(parts[1]) + 1);
        parts[2] = "0";
        break;
      case PATCH:
        parts[2] = String.valueOf(Integer.parseInt(parts[2]) + 1);
        break;
    }

    return prefix + parts[0] + "." + parts[1] + "." + parts[2];
  }

  private Version createVersion(Version unreleasedVersion, String releaseVersion) {
    var version = Version.builder()
      .releaseVersion(releaseVersion)
      .date(LocalDate.now().toString())
      .added(CategoryAdded.builder()
          .entries(new ArrayList<>(unreleasedVersion.getAdded().getEntries()))
          .build())
      .changed(CategoryChanged.builder()
          .entries(new ArrayList<>(unreleasedVersion.getChanged().getEntries()))
          .build())
      .fixed(CategoryFixed.builder()
          .entries(new ArrayList<>(unreleasedVersion.getFixed().getEntries()))
          .build())
      .removed(CategoryRemoved.builder()
          .entries(new ArrayList<>(unreleasedVersion.getRemoved().getEntries()))
          .build())
      .build();

    unreleasedVersion.getAdded().getEntries().clear();
    unreleasedVersion.getChanged().getEntries().clear();
    unreleasedVersion.getFixed().getEntries().clear();
    unreleasedVersion.getRemoved().getEntries().clear();

    return version;
  }

  private Version createVersion(VersionEntry entry, CategoryType category, String releaseVersion, String date) {

    if (category == null) {
      throw new IllegalArgumentException("Category cannot be null");
    }

    var versionBuilder = Version.builder().releaseVersion(releaseVersion).date(date);

    switch (category) {
      case ADDED -> versionBuilder.added(CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build());
      case CHANGED -> versionBuilder.changed(CategoryChanged.builder().entries(new ArrayList<>(Arrays.asList(entry))).build());
      case FIXED -> versionBuilder.fixed(CategoryFixed.builder().entries(new ArrayList<>(Arrays.asList(entry))).build());
      case REMOVED -> versionBuilder.removed(CategoryRemoved.builder().entries(new ArrayList<>(Arrays.asList(entry))).build());
    }
    return versionBuilder.build();
  }

  private boolean hasUnreleasedVersion(Entity entity) {
    return entity.getVersions().stream().anyMatch(version -> version.getReleaseVersion().equals(UNRELEASED));
  }
}
