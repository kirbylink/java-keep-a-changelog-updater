package de.dddns.kirbylink.keepachangelogupdater.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VersionEntryTest {

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {}

  @AfterEach
  void tearDown() throws Exception {}

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
