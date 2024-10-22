package de.dddns.kirbylink.keepachangelogupdater.model.changelog;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Entity {
  @Default
  private final EntityHeader header = EntityHeader.builder().build();
  @Default
  private final List<Version> versions = new ArrayList<>();
  @Default
  private final EntityFooter footer = EntityFooter.builder().build();

  @Override
  public String toString() {
    var stringBuilder = new StringBuilder();
    stringBuilder.append(header);
    stringBuilder.append("\n");
    versions.stream().forEach(stringBuilder::append);
    stringBuilder.append(footer);
    return stringBuilder.toString();
  }
}
