package de.dddns.kirbylink.keepachangelogupdater.model;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
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

class EntityTest {

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {}

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void test_WhenEntityIsCreatedWithAllFields_ThenEntityContainsHeaderVersionsAndFooter() {

    // Given
    var header = EntityHeader.builder().build();

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

    var versionFirst = Version.builder()
        .releaseVersion("1.0.0")
        .date("2024-07-20")
        .added(categoryAdded)
        .changed(categoryChanged)
        .fixed(categoryFixed)
        .removed(categoryRemoved)
        .build();
    var versionSecond = Version.builder()
        .releaseVersion("1.1.0")
        .date("2024-07-21")
        .added(categoryAdded)
        .changed(categoryChanged)
        .fixed(categoryFixed)
        .removed(categoryRemoved)
        .build();

    var changelogEntityFooter = EntityFooter.builder()
        .urls(Arrays.asList("first/url/", "second/url/"))
        .build();

    // When
    var entity = Entity.builder()
        .header(header)
        .versions(Arrays.asList(versionFirst, versionSecond))
        .footer(changelogEntityFooter)
        .build();

    // Then
    assertThat(entity.getHeader()).isNotNull();
    assertThat(entity.getHeader().getTitle()).isEqualTo("Changelog");

    assertThat(entity.getVersions()).isNotEmpty().hasSize(2);

    assertThat(entity.getFooter()).isNotNull();
    assertThat(entity.getFooter().getUrls()).isNotNull().hasSize(2);
  }
}
