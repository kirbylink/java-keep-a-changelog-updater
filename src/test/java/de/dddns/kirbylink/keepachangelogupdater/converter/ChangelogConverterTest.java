package de.dddns.kirbylink.keepachangelogupdater.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Entity;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.EntityFooter;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.EntityHeader;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Version;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.VersionEntry;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryAdded;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryChanged;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryFixed;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryRemoved;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryType;

class ChangelogConverterTest {

  private ChangelogConverter changelogConverter;

  private static final String CHANGELOG = """
      # Changelog of some application

      All notable changes to this project will be documented in this file.

      The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
      and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

      And there is some additional description.

      ## [Unreleased]
      ### Added
      - Document the `convert()` method

      ## [0.1.0] - 2024-07-20
      ### Changed
      - Change structure of VersionEntity

      ### Fixed
      - Replace application name in MAKE.md file

      ### Removed
      - Remove unused method `parse()`
      - Delete check for NullpointerException in `main()` method

      ## [0.0.1] - 2024-06.30
      ### Added
      - Add initial files

      [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
      [0.1.0]: https://git.example.com:443/organization/repo.git/compare/0.0.1...0.1.0
      [0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/0.0.1
      """;

  @BeforeEach
  void setUp() {
    changelogConverter = new ChangelogConverter("https://git.example.com:443/organization/repo.git", "main");
  }

  @Nested
  @DisplayName("Tests for convertToEntity method")
  class ConvertToEntityTests {

    @Test
    void testConvertToEntity_WhenMethodIsCalled_ThenEntityIsReturned() {

      // Given

      // When
      var entity = changelogConverter.convertToEntity(CHANGELOG);

      // Then
      assertThat(entity).isNotNull();

      assertThat(entity.getHeader()).isNotNull();
      var header = entity.getHeader();
      assertThat(header.getTitle()).isEqualTo("Changelog of some application");
      assertThat(header.getDescription()).contains("All notable changes to this project will be documented in this file.").contains("And there is some additional description.");

      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).isNotEmpty().hasSize(3);
      var versions = entity.getVersions();
      var versionUnreleased = versions.get(0);
      assertThat(versionUnreleased.getReleaseVersion()).isEqualTo("Unreleased");
      assertThat(versionUnreleased.getDate()).isEmpty();
      assertThat(versionUnreleased.getAdded()).isNotNull();

      var versionSecond = versions.get(1);
      assertThat(versionSecond.getReleaseVersion()).isEqualTo("0.1.0");
      assertThat(versionSecond.getDate()).isEqualTo("2024-07-20");
      AssertionsForInterfaceTypes.assertThat(versionSecond.getAdded().getEntries()).isEmpty();
      AssertionsForInterfaceTypes.assertThat(versionSecond.getChanged().getEntries()).isNotEmpty();
      AssertionsForInterfaceTypes.assertThat(versionSecond.getFixed().getEntries()).isNotEmpty();
      AssertionsForInterfaceTypes.assertThat(versionSecond.getRemoved().getEntries()).isNotEmpty();

      var versionThird = versions.get(2);
      assertThat(versionThird.getReleaseVersion()).isEqualTo("0.0.1");
      assertThat(versionThird.getDate()).isEqualTo("2024-06.30");
      AssertionsForInterfaceTypes.assertThat(versionThird.getAdded().getEntries()).isNotEmpty();
      AssertionsForInterfaceTypes.assertThat(versionThird.getChanged().getEntries()).isEmpty();
      AssertionsForInterfaceTypes.assertThat(versionThird.getFixed().getEntries()).isEmpty();
      AssertionsForInterfaceTypes.assertThat(versionThird.getRemoved().getEntries()).isEmpty();

      assertThat(entity.getFooter()).isNotNull();
      AssertionsForInterfaceTypes.assertThat(entity.getFooter().getUrls()).isNotEmpty().hasSize(3);
    }

    @Test
    void testConvertToEntity_WhenChangelogContainsBreakingChange_ThenEntityWithVersionAndBreakingChangeIsReturned() {

      // Given
      var changelog = """
          # Changelog

          All notable changes to this project will be documented in this file.

          The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
          and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

          ## [Unreleased]

          ## [v1.0.0] - 2024-06-03
          ### Added
          - Initial release of the Application.

          BREAKING CHANGE: Description of breaking change.
          Another line of description

          ## [v0.0.1] - 2024-06-01
          ### Added
          - Add initial files

          BREAKING CHANGE: Some breaking changes.
          Some description of the first breaking change.

          Another breaking change information.

          [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
          [v1.0.0]: https://git.example.com:443/organization/repo.git/releases/tag/v1.0.0
          """;

      // When
      var entity = changelogConverter.convertToEntity(changelog);

      // Then
      assertThat(entity).isNotNull();

      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).isNotEmpty().hasSize(3);
      var versions = entity.getVersions();
      var versionSecond = versions.get(1);
      assertThat(versionSecond.getBreakingChange()).isEqualTo("Description of breaking change.\nAnother line of description");

      var versionThird = versions.get(2);
      assertThat(versionThird.getBreakingChange()).isEqualTo("Some breaking changes.\nSome description of the first breaking change.\n\nAnother breaking change information.");

    }
  }

  @Nested
  @DisplayName("Tests for convertToString method")
  class ConvertToStringTests {

    @ParameterizedTest
    @EnumSource(value = CategoryType.class)
    void testConvertToString_WhenUnreleasedIsEmpty_ThenNewLineIsBetweenUnreleasedAndReleaseVersion(CategoryType type) {

      // Given
      var expectedVersionsString = """
              ## [Unreleased]

              ## [v0.0.1] - 2024-07-26
              """;

      var versions = new ArrayList<Version>();

      var versionUnreleased = Version.builder().releaseVersion("Unreleased").date("").build();
      versions.add(versionUnreleased);
      var versionRelease = Version.builder().releaseVersion("v0.0.1").date("2024-07-26").build();
      var entry = VersionEntry.builder().description("Add new entry").build();
      versionRelease.getCategory(type).getEntries().add(entry);
      versions.add(versionRelease);
      var entity = Entity.builder().versions(versions).build();

      // When
      var changelogString = changelogConverter.convertToString(entity);

      // Then
      assertThat(changelogString).contains(expectedVersionsString);
    }

    @Test
    void testConvertToString_WhenMethodWithOnlyUnreleasedVersionIsCalled_ThenChangelogStringWithOnlyUnreleasedFooterUrlIsReturned() {

      // Given
      var expectedChangelog = """
              # Changelog

              All notable changes to this project will be documented in this file.

              The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
              and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

              ## [Unreleased]

              [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
              """;

      var versions = new ArrayList<Version>();

      var version = Version.builder().releaseVersion("Unreleased").date("").build();
      versions.add(version);

      var entity = Entity.builder().versions(versions).build();

      // When
      var changelogString = changelogConverter.convertToString(entity);

      // Then
      assertThat(changelogString).isEqualTo(expectedChangelog);
    }

    @Test
    void testConvertToString_WhenMethodIsCalled_ThenChangelogStringIsReturned() {

      // Given
      var title = "Changelog of some application";
      var description = """
         All notable changes to this project will be documented in this file.

         The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
         and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

         And there is some additional description.""";
      var header = EntityHeader.builder().title(title).description(description).build();

      var versions = new ArrayList<Version>();

      var entry = VersionEntry.builder().description("Document the `convert()` method").build();
      var categoryAdded = CategoryAdded.builder().entries(Collections.singletonList(entry)).build();
      var version = Version.builder().added(categoryAdded).releaseVersion("Unreleased").date("").build();
      versions.add(version);

      var entryChanged = VersionEntry.builder().description("Change structure of VersionEntity").build();
      var categoryChanged = CategoryChanged.builder().entries(Collections.singletonList(entryChanged)).build();
      var entryFixed = VersionEntry.builder().description("Replace application name in MAKE.md file").build();
      var categoryFixed = CategoryFixed.builder().entries(Collections.singletonList(entryFixed)).build();
      var entryRemoved01 = VersionEntry.builder().description("Remove unused method `parse()`").build();
      var entryRemoved02 = VersionEntry.builder().description("Delete check for NullpointerException in `main()` method").build();
      var categoryRemoved = CategoryRemoved.builder().entries(Arrays.asList(entryRemoved01, entryRemoved02)).build();
      version = Version.builder().changed(categoryChanged).fixed(categoryFixed).removed(categoryRemoved).releaseVersion("0.1.0").date("2024-07-20").build();
      versions.add(version);

      entry = VersionEntry.builder().description("Add initial files").build();
      categoryAdded = CategoryAdded.builder().entries(Collections.singletonList(entry)).build();
      version = Version.builder().added(categoryAdded).releaseVersion("0.0.1").date("2024-06.30").build();
      versions.add(version);

      var urls = new ArrayList<>(Arrays.asList(
          "[unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD",
          "[0.1.0]: https://git.example.com:443/organization/repo.git/compare/0.0.1...0.1.0",
          "[0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/0.0.1"));

      var entity = Entity.builder().header(header).versions(versions).build();

      // When
      var changelogString = changelogConverter.convertToString(entity);

      // Then
      assertThat(changelogString).isEqualTo(CHANGELOG).contains(urls);
    }

    @Test
    void testConvertToString_WhenStringConvertedToEntityAndBackToString_ThenChangelogStringIsNotChanged() {

      // Given
      var expectedChangelog = """
          # Changelog

          All notable changes to this project will be documented in this file.

          The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
          and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

          ## [Unreleased]

          ## [v1.0.2] - 2024-06-19
          ### Changed
          - Updated Maven dependencies to the latest versions.

          ## [v1.0.1] - 2024-06-01
          ### Added
          - Console parameter verification
          - Detailed description about wrong input

          ### Changed
          - Updated Maven dependencies to the latest versions.

          ## [v1.0.0] - 2024-06-03
          ### Added
          - Initial release of the Application.
          - Support for both terminal and GUI usage.
          - Platform-independent: supports Windows, macOS, and Linux.
          - Localization support for different languages and country settings.
          - Example commands and usage documentation.
          - Detailed help output for command-line parameters.

          [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
          [v1.0.2]: https://git.example.com:443/organization/repo.git/compare/v1.0.1...v1.0.2
          [v1.0.1]: https://git.example.com:443/organization/repo.git/compare/v1.0.0...v1.0.1
          [v1.0.0]: https://git.example.com:443/organization/repo.git/releases/tag/v1.0.0
          """;

      // When
      var entity = changelogConverter.convertToEntity(expectedChangelog);
      var actualChangelog = changelogConverter.convertToString(entity);

      // Then
      assertThat(actualChangelog).isEqualTo(expectedChangelog);
    }

    @Test
    void testConvertToString_WhenInvalidChangelog_ThenEntityHasOnlyPartielContent() {

      // Given
      var unexpectedChangelog = """
          # Changelog

          All notable changes to this project will be documented in this file.

          The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
          and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

          ### Added
          - A bullet list item

          ## [Unreleased]

          ## [v1.0.2] - 2024-06-19
          ### Changed
          #### Invalid entry

          ## [v1.0.1] - 2024-06-01
          ### Added
          - Console parameter verification
          > Detailed description about wrong input

          > Invalid url
          """;

      // When
      var entity = changelogConverter.convertToEntity(unexpectedChangelog);
      var actualChangelog = changelogConverter.convertToString(entity);

      // Then
      assertThat(actualChangelog).isNotEqualTo(unexpectedChangelog);
    }

    @Test
    void testConvertToString_WhenChangelogContainsBreakingChange_ThenChangelogStringHasParagraphWithBreakingChange() {
      // Given
      var expectedChangelog = """
          # Changelog

          All notable changes to this project will be documented in this file.

          The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
          and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

          ## [Unreleased]

          ## [v1.0.0] - 2024-06-03
          ### Added
          - Initial release of the Application.

          BREAKING CHANGE: Some breaking changes.
          Some description of the first breaking change.

          Another breaking change information.

          ## [v0.0.1] - 2024-06-01
          ### Added
          - Add initial files

          [unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD
          [v1.0.0]: https://git.example.com:443/organization/repo.git/releases/tag/v0.0.1...v1.0.0
          [v0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/v0.0.1
          """;

      var versions = new ArrayList<Version>();

      var version = Version.builder().releaseVersion("Unreleased").date("").build();
      versions.add(version);

      var entry = VersionEntry.builder().description("Initial release of the Application.").build();
      var categoryAdded = CategoryAdded.builder().entries(Collections.singletonList(entry)).build();
      var breakingChange = "Some breaking changes.\nSome description of the first breaking change.\n\nAnother breaking change information.";
      version = Version.builder().added(categoryAdded).breakingChange(breakingChange).releaseVersion("v1.0.0").date("2024-06-03").build();
      versions.add(version);

      entry = VersionEntry.builder().description("Add initial files").build();
      categoryAdded = CategoryAdded.builder().entries(Collections.singletonList(entry)).build();
      version = Version.builder().added(categoryAdded).releaseVersion("v0.0.1").date("2024-06-01").build();
      versions.add(version);

      var urls = new ArrayList<>(Arrays.asList(
          "[unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD",
          "[v1.0.0]: https://git.example.com:443/organization/repo.git/releases/tag/v0.0.1...v1.0.0",
          "[v0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/v0.0.1"));
      var footer = EntityFooter.builder().urls(urls).build();

      var entity = Entity.builder().versions(versions).footer(footer).build();

      // When
      var actualChangelog = changelogConverter.convertToString(entity);

      // Then
      assertThat(actualChangelog).isEqualTo(expectedChangelog);
    }

  }

  @Test
  void testCreateDefaultEntity_WhenMethodIsCalled_ThenDefaultEmptyEntityIsReturned() {
    // Given

    // When
    var entity = changelogConverter.createDefaultEntity();

    // Then
    assertThat(entity.getHeader()).isNotNull();
    var header = entity.getHeader();
    assertThat(header.getTitle()).isEqualTo("Changelog");
    assertThat(header.getDescription()).contains("All notable changes to this project will be documented in this file.");

    AssertionsForInterfaceTypes.assertThat(entity.getVersions()).isNotEmpty().hasSize(1);
    var versionUnreleased = entity.getVersions().get(0);
    assertThat(versionUnreleased.getReleaseVersion()).isEqualTo("Unreleased");
    assertThat(versionUnreleased.getDate()).isEmpty();
    assertThat(versionUnreleased.getAdded()).isNotNull();

    assertThat(entity.getFooter()).isNotNull();
    AssertionsForInterfaceTypes.assertThat(entity.getFooter().getUrls()).isEmpty();
  }
}
