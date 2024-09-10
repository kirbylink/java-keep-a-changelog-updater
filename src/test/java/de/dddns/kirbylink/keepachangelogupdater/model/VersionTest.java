package de.dddns.kirbylink.keepachangelogupdater.model;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Collections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryAdded;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryChanged;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryFixed;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryRemoved;

class VersionTest {

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {}

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void test_WhenVersionIsCreatedWithAllCategories_ThenVersionContainsCategoriesAndEntries() {

    // Given
    var entryAdded = VersionEntry.builder()
        .description("Add first item")
        .build();
    var categoryAdded = CategoryAdded.builder()
        .entries(Collections.singletonList(entryAdded))
        .build();

    var entryChanged = VersionEntry.builder()
        .description("Change second item")
        .build();
    var categoryChanged = CategoryChanged.builder()
        .entries(Collections.singletonList(entryChanged))
        .build();

    var entryFixed = VersionEntry.builder()
        .description("Fix third item")
        .build();
    var categoryFixed = CategoryFixed.builder()
        .entries(Collections.singletonList(entryFixed))
        .build();

    var entryRemoved = VersionEntry.builder()
        .description("Remove fourth item")
        .build();
    var categoryRemoved = CategoryRemoved.builder()
        .entries(Collections.singletonList(entryRemoved))
        .build();

    // When
    var changelogVersion = Version.builder()
        .releaseVersion("1.0.0")
        .date("2024-07-20")
        .added(categoryAdded)
        .changed(categoryChanged)
        .fixed(categoryFixed)
        .removed(categoryRemoved)
        .build();

    // Then
    assertThat(changelogVersion.getAdded()).isNotNull();
    assertThat(changelogVersion.getAdded().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getAdded().getEntries().get(0).getDescription()).isEqualTo("Add first item");

    assertThat(changelogVersion.getChanged()).isNotNull();
    assertThat(changelogVersion.getChanged().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getChanged().getEntries().get(0).getDescription()).isEqualTo("Change second item");

    assertThat(changelogVersion.getFixed()).isNotNull();
    assertThat(changelogVersion.getFixed().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getFixed().getEntries().get(0).getDescription()).isEqualTo("Fix third item");

    assertThat(changelogVersion.getRemoved()).isNotNull();
    assertThat(changelogVersion.getRemoved().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getRemoved().getEntries().get(0).getDescription()).isEqualTo("Remove fourth item");
  }
}
