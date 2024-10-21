package de.dddns.kirbylink.keepachangelogupdater.model.changelog;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Scenario {
  CREATE("create"),
  ADD_ENTRY("add-entry"),
  RELEASE("release"),
  AUTO_GENERATE("auto-generate");

  @Getter
  private final String value;

  public static Scenario fromValue(String value) {
    return Arrays.stream(Scenario.values())
      .filter(scenario -> scenario.value.equalsIgnoreCase(value))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Unknown scenario value: " + value));
  }
}
