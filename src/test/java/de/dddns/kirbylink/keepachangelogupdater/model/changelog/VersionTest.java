package de.dddns.kirbylink.keepachangelogupdater.model.changelog;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Collections;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryAdded;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryChanged;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryDeprecated;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryFixed;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryRemoved;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategorySecurity;

class VersionTest {

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

    var entrySecurity = VersionEntry.builder()
        .description("Security fifth item")
        .build();
    var categorySecurity = CategorySecurity.builder()
        .entries(Collections.singletonList(entrySecurity))
        .build();

    var entryDeprecated = VersionEntry.builder()
        .description("Deprecated sixth item")
        .build();
    var categoryDeprecated = CategoryDeprecated.builder()
        .entries(Collections.singletonList(entryDeprecated))
        .build();

    // When
    var changelogVersion = Version.builder()
        .releaseVersion("1.0.0")
        .date("2024-07-20")
        .added(categoryAdded)
        .changed(categoryChanged)
        .fixed(categoryFixed)
        .removed(categoryRemoved)
        .security(categorySecurity)
        .deprecated(categoryDeprecated)
        .build();

    // Then
    assertThat(changelogVersion.getAdded()).isNotNull();
    AssertionsForInterfaceTypes.assertThat(changelogVersion.getAdded().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getAdded().getEntries().get(0).getDescription()).isEqualTo("Add first item");

    assertThat(changelogVersion.getChanged()).isNotNull();
    AssertionsForInterfaceTypes.assertThat(changelogVersion.getChanged().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getChanged().getEntries().get(0).getDescription()).isEqualTo("Change second item");

    assertThat(changelogVersion.getFixed()).isNotNull();
    AssertionsForInterfaceTypes.assertThat(changelogVersion.getFixed().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getFixed().getEntries().get(0).getDescription()).isEqualTo("Fix third item");

    assertThat(changelogVersion.getRemoved()).isNotNull();
    AssertionsForInterfaceTypes.assertThat(changelogVersion.getRemoved().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getRemoved().getEntries().get(0).getDescription()).isEqualTo("Remove fourth item");

    assertThat(changelogVersion.getSecurity()).isNotNull();
    AssertionsForInterfaceTypes.assertThat(changelogVersion.getSecurity().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getSecurity().getEntries().get(0).getDescription()).isEqualTo("Security fifth item");

    assertThat(changelogVersion.getDeprecated()).isNotNull();
    AssertionsForInterfaceTypes.assertThat(changelogVersion.getDeprecated().getEntries()).isNotEmpty().hasSize(1);
    assertThat(changelogVersion.getDeprecated().getEntries().get(0).getDescription()).isEqualTo("Deprecated sixth item");
  }

  @Test
  void test_WhenVersionIsCreatedWithBreakingChange_ThenVersionContainsBreakingChangeParagraph() {
    // Given
    var breakingChange = "Description of breaking change.\nAnother line of description";
    var expectedToStringOfVersion = """
            ## [1.0.0] - 2024-07-20
            ### Added
            - Add first item

            BREAKING CHANGE: Description of breaking change.
            Another line of description

            """;
    var entryAdded = VersionEntry.builder()
        .description("Add first item")
        .build();
    var categoryAdded = CategoryAdded.builder()
        .entries(Collections.singletonList(entryAdded))
        .build();

    // When
    var changelogVersion = Version.builder()
        .releaseVersion("1.0.0")
        .date("2024-07-20")
        .added(categoryAdded)
        .breakingChange(breakingChange)
        .build();

    // Then
    assertThat(changelogVersion.getBreakingChange()).isNotEmpty();
    assertThat(changelogVersion.getBreakingChange()).isEqualTo(breakingChange);
    assertThat(changelogVersion).hasToString(expectedToStringOfVersion);
  }

  @ParameterizedTest(name = "{0} => has breaking change: {1}")
  @CsvSource(value = {
      "NULL, false",
      "'', false",
      "' ', false",
      "'Introduce breaking change', true",
  }, nullValues = "NULL")
  void testHasBreakingChange(String breakingChange, boolean expectedHasBreakingChange) {
    // Given

    // When
    var version = Version.builder().breakingChange(breakingChange).build();

    // Then
    assertThat(version.hasBreakingChange()).isEqualTo(expectedHasBreakingChange);
  }

  @Test
  void test_ToString_WhenCategoriesAreFilled_ThenToStringCreatesVersionInformation() {
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

    var entrySecurity = VersionEntry.builder()
        .description("Security fifth item")
        .build();
    var categorySecurity = CategorySecurity.builder()
        .entries(Collections.singletonList(entrySecurity))
        .build();

    var entryDeprecated = VersionEntry.builder()
        .description("Deprecated sixth item")
        .build();
    var categoryDeprecated = CategoryDeprecated.builder()
        .entries(Collections.singletonList(entryDeprecated))
        .build();

    var changelogVersion = Version.builder()
        .releaseVersion("1.0.0")
        .date("2024-07-20")
        .added(categoryAdded)
        .changed(categoryChanged)
        .fixed(categoryFixed)
        .removed(categoryRemoved)
        .security(categorySecurity)
        .deprecated(categoryDeprecated)
        .build();

    var expectedOutput = """
        ## [1.0.0] - 2024-07-20
        ### Added
        - Add first item

        ### Changed
        - Change second item

        ### Fixed
        - Fix third item

        ### Removed
        - Remove fourth item

        ### Security
        - Security fifth item

        ### Deprecated
        - Deprecated sixth item

        """;

    //When
    var result = changelogVersion.toString();

    //Then
    assertThat(result).isEqualTo(expectedOutput);
  }
}
