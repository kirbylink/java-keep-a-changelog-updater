package de.dddns.kirbylink.keepachangelogupdater.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class VersionEntry {
  private final String description;

  @Override
  public String toString() {
    return "- " + description;
  }
}
