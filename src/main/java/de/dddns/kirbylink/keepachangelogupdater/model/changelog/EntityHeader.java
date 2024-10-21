package de.dddns.kirbylink.keepachangelogupdater.model.changelog;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class EntityHeader {

  private static final String DEFAULT_TITLE = "Changelog";

  private static final String DEFAULT_DESCRIPTION = """
      All notable changes to this project will be documented in this file.

      The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
      and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).""";

  @Default
  private final String title = DEFAULT_TITLE;
  @Default
  private final String description = DEFAULT_DESCRIPTION;

  @Override
  public String toString() {
    return "# " + title + "\n\n" + description + "\n";
  }
}
