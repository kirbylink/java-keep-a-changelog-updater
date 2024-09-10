package de.dddns.kirbylink.keepachangelogupdater.model;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReleaseType {
  MAJOR("major"),
  MINOR("minor"),
  PATCH("patch");

  @Getter
  private final String value;

  public static ReleaseType fromValue(String value) {
    return Arrays.stream(ReleaseType.values())
      .filter(releaseType -> releaseType.value.equalsIgnoreCase(value))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Unknown scenario value: " + value));
  }
}
