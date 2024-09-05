package de.dddns.kirbylink.keepachangelogupdater.model.category;

import java.util.ArrayList;
import java.util.List;
import de.dddns.kirbylink.keepachangelogupdater.model.VersionEntry;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@RequiredArgsConstructor
public abstract class Category {
  @Default
  private final List<VersionEntry> entries = new ArrayList<>();

  public abstract CategoryType getType();

  @Override
  public String toString() {
    var stringBuilder = new StringBuilder();
    stringBuilder.append("### ").append(getType().getValue()).append("\n");
    entries.stream().forEach(entry -> stringBuilder.append(entry).append("\n"));
    return stringBuilder.toString();
  }
}
