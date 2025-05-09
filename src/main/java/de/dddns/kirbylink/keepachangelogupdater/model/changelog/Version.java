package de.dddns.kirbylink.keepachangelogupdater.model.changelog;

import java.util.Arrays;
import java.util.Objects;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.Category;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryAdded;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryChanged;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryDeprecated;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryFixed;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryRemoved;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategorySecurity;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class Version {
  private final String releaseVersion;
  @Setter
  @Default
  private String date = "";
  @Default
  private final CategoryAdded added = CategoryAdded.builder().build();
  @Default
  private final CategoryChanged changed = CategoryChanged.builder().build();
  @Default
  private final CategoryFixed fixed = CategoryFixed.builder().build();
  @Default
  private final CategoryRemoved removed = CategoryRemoved.builder().build();
  @Default
  private final CategorySecurity security = CategorySecurity.builder().build();
  @Default
  private final CategoryDeprecated deprecated = CategoryDeprecated.builder().build();
  @Setter
  @Default
  private String breakingChange = "";

  public Category getCategory(CategoryType type) {
    return Arrays.asList(added, changed, fixed, removed, security, deprecated).stream()
      .filter(Objects::nonNull)
      .filter(category -> category.getType().equals(type))
      .findFirst()
      .orElse(null);
  }

  public boolean hasBreakingChange() {
    return breakingChange != null && !breakingChange.isBlank();
  }

  @Override
  public String toString() {
    var stringBuilder = new StringBuilder();
    if (date.isBlank()) {
      stringBuilder.append("## [").append(releaseVersion).append("]").append("\n");
    } else {
      stringBuilder.append("## [").append(releaseVersion).append("] - ").append(date).append("\n");
    }
    if (!added.getEntries().isEmpty()) {
      stringBuilder.append(added).append("\n");
    }
    if (!changed.getEntries().isEmpty()) {
      stringBuilder.append(changed).append("\n");
    }
    if (!fixed.getEntries().isEmpty()) {
      stringBuilder.append(fixed).append("\n");
    }
    if (!removed.getEntries().isEmpty()) {
      stringBuilder.append(removed).append("\n");
    }
    if (!security.getEntries().isEmpty()) {
      stringBuilder.append(security).append("\n");
    }
    if (!deprecated.getEntries().isEmpty()) {
      stringBuilder.append(deprecated).append("\n");
    }

    if (added.getEntries().isEmpty() &&
        changed.getEntries().isEmpty() &&
        fixed.getEntries().isEmpty() &&
        removed.getEntries().isEmpty() &&
        security.getEntries().isEmpty() &&
        deprecated.getEntries().isEmpty()) {
      stringBuilder.append("\n");
    }

    if (hasBreakingChange()) {
      stringBuilder.append("BREAKING CHANGE: " + breakingChange + "\n\n");
    }
    return stringBuilder.toString();
  }
}
