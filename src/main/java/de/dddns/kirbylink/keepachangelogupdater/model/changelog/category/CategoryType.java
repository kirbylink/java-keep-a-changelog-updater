package de.dddns.kirbylink.keepachangelogupdater.model.changelog.category;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryType {
  ADDED("Added"),
  CHANGED("Changed"),
  FIXED("Fixed"),
  REMOVED("Removed"),
  SECURITY("Security");

  @Getter
  private final String value;

  public static CategoryType fromValue(String value) {
    return Arrays.stream(CategoryType.values())
      .filter(type -> type.value. equalsIgnoreCase(value))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Unknown CategoryType value: " + value));
  }
}
