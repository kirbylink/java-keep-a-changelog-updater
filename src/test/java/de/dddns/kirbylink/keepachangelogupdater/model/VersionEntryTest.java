package de.dddns.kirbylink.keepachangelogupdater.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

class VersionEntryTest {

  @Test
  void test_WhenNewVersionEntryIsCreated_ThenTypeAndDescriptionIsSet() {

    // Given
    var description = "Add VersionEntry class and VersionEntryType class";

    // When
    var entry = VersionEntry.builder()
        .description(description)
        .build();

    // Then
    assertThat(entry.getDescription()).isEqualTo(description);
  }

}
