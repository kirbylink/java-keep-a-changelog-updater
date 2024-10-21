package de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits;

import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Version;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class HeadingResult {
  private final String title;
  private final Version.VersionBuilder currentVersionBuilder;
  private final StringBuilder breakingChangeBuilder;
  private final String currentType;
}
