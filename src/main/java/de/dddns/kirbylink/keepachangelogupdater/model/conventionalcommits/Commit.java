package de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Commit {
  private final String hashCode;
  private final String type;
  private final String description;
  private final String body;
  @Setter
  private String breakingChange;

  public boolean hasBreakingChange() {
    return type.endsWith("!") || (breakingChange != null && !breakingChange.isBlank());
  }
}
