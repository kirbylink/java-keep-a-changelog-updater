package de.dddns.kirbylink.keepachangelogupdater.utility;

import java.util.Optional;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Entity;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Version;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VersionUtility {
  public static Optional<Version> getOptinalVersion(Entity entity, String version) {
    return entity.getVersions()
        .stream()
        .filter(currentVersion -> currentVersion.getReleaseVersion().equals(version))
        .findFirst();
  }
}
