package de.dddns.kirbylink.keepachangelogupdater.converter;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import de.dddns.kirbylink.keepachangelogupdater.model.Entity;
import de.dddns.kirbylink.keepachangelogupdater.model.EntityHeader;
import de.dddns.kirbylink.keepachangelogupdater.model.Version;
import de.dddns.kirbylink.keepachangelogupdater.model.VersionEntry;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryAdded;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryChanged;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryFixed;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryRemoved;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryType;

class ChangelogConverterTest {

  private ChangelogConverter changelogConverter;

  private final static String CHANGELOG = """
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

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() {
    changelogConverter = new ChangelogConverter("https://git.example.com:443/organization/repo.git", "main");
  }

  @AfterEach
  void tearDown() throws Exception {}

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

    assertThat(entity.getVersions()).isNotEmpty().hasSize(1);
    var versionUnreleased = entity.getVersions().get(0);
    assertThat(versionUnreleased.getReleaseVersion()).isEqualTo("Unreleased");
    assertThat(versionUnreleased.getDate()).isEmpty();
    assertThat(versionUnreleased.getAdded()).isNotNull();

    assertThat(entity.getFooter()).isNotNull();
    assertThat(entity.getFooter().getUrls()).isEmpty();

  }

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

    assertThat(entity.getVersions()).isNotEmpty().hasSize(3);
    var versions = entity.getVersions();
    var versionUnreleased = versions.get(0);
    assertThat(versionUnreleased.getReleaseVersion()).isEqualTo("Unreleased");
    assertThat(versionUnreleased.getDate()).isEmpty();
    assertThat(versionUnreleased.getAdded()).isNotNull();

    var versionSecond = versions.get(1);
    assertThat(versionSecond.getReleaseVersion()).isEqualTo("0.1.0");
    assertThat(versionSecond.getDate()).isEqualTo("2024-07-20");
    assertThat(versionSecond.getAdded().getEntries()).isEmpty();
    assertThat(versionSecond.getChanged().getEntries()).isNotEmpty();
    assertThat(versionSecond.getFixed().getEntries()).isNotEmpty();
    assertThat(versionSecond.getRemoved().getEntries()).isNotEmpty();

    var versionThird = versions.get(2);
    assertThat(versionThird.getReleaseVersion()).isEqualTo("0.0.1");
    assertThat(versionThird.getDate()).isEqualTo("2024-06.30");
    assertThat(versionThird.getAdded().getEntries()).isNotEmpty();
    assertThat(versionThird.getChanged().getEntries()).isEmpty();
    assertThat(versionThird.getFixed().getEntries()).isEmpty();
    assertThat(versionThird.getRemoved().getEntries()).isEmpty();

    assertThat(entity.getFooter()).isNotNull();
    assertThat(entity.getFooter().getUrls()).isNotEmpty().hasSize(3);
  }

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
  void testCreateEntityFooter_WhenEmptyListOfVersionIsGiven_ThenFooterWithEmptyListOfUrlIsCreated() {

    // Given
    var versions = new ArrayList<Version>();

    // When
    var urls = changelogConverter.convertToEntityFooterUrls(versions);

    // Then
    assertThat(urls).isEmpty();
  }

  @Test
  void testCreateEntityFooter_WhenListOfVersionIsGiven_ThenFooterWithListOfUrlIsCreated() {

    // Given
    var versions = new ArrayList<Version>();

    var version = Version.builder().releaseVersion("Unreleased").date("").build();
    versions.add(version);

    version = Version.builder().releaseVersion("0.1.1").date("2024-07-24").build();
    versions.add(version);

    version = Version.builder().releaseVersion("0.1.0").date("2024-07-20").build();
    versions.add(version);

    version = Version.builder().releaseVersion("0.0.1").date("2024-06.30").build();
    versions.add(version);

    // When
    var urls = changelogConverter.convertToEntityFooterUrls(versions);

    // Then
    assertThat(urls).isNotEmpty().hasSize(4);
    assertThat(urls.get(0)).isEqualTo("[unreleased]: https://git.example.com:443/organization/repo.git/compare/main...HEAD");
    assertThat(urls.get(1)).isEqualTo("[0.1.1]: https://git.example.com:443/organization/repo.git/compare/0.1.0...0.1.1");
    assertThat(urls.get(2)).isEqualTo("[0.1.0]: https://git.example.com:443/organization/repo.git/compare/0.0.1...0.1.0");
    assertThat(urls.get(3)).isEqualTo("[0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/0.0.1");
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
}
