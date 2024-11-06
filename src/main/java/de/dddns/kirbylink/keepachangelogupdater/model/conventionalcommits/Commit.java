package de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Commit {
  private final String hashCode;
  private final String type;
  private final String description;
  private final String body;
  @Accessors(fluent = true)
  private final boolean hasBreakingChange;
  @Setter
  private String breakingChange;
}
