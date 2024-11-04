package de.dddns.kirbylink.keepachangelogupdater.service;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Entity;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.EntityHeader;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.ReleaseType;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Version;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.VersionEntry;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryAdded;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryChanged;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryDeprecated;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryFixed;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryRemoved;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategorySecurity;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryType;

class UpdateServiceTest {

  private static final String UNRELEASED_VERSION = "Unreleased";

  private UpdateService updateService;

  @BeforeEach
  void setUp() {
    updateService = new UpdateService();
  }

  @Nested
  @DisplayName("Test for updateChangelog method")
  class TestUpdateChangelogMethod {

    @ParameterizedTest
    @EnumSource(value = CategoryType.class)
    void testUpdateChangelog_WhenEmptyChangelog_ThenNewVersionWithNewCategoryAndEntryIsCreated(CategoryType category) {

      // Given
      var entity = Entity.builder().build();

      // When
      updateService.updateChangelog(entity, "Add new entry", category, UNRELEASED_VERSION, "");

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(1);
      assertThat(entity.getVersions().get(0).getDate()).isEmpty();
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(0).getCategory(category).getEntries()).hasSize(1);
      assertThat(entity.getVersions().get(0).getCategory(category).getEntries().get(0).getDescription()).isEqualTo("Add new entry");
    }

    @Test
    void testUpdateChangelog_WhenVersionWithCategoryAlreadyExists_ThenNewEntryInExistingCategoryIsCreated() {

      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var version = Version.builder().releaseVersion(UNRELEASED_VERSION).added(added).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(version))).build();

      // When
      updateService.updateChangelog(entity, "Add new entry", CategoryType.ADDED, UNRELEASED_VERSION, "");

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(0).getAdded().getEntries()).hasSize(2);
      assertThat(entity.getVersions().get(0).getAdded().getEntries().get(0).getDescription()).isEqualTo("Add new entry");
      assertThat(entity.getVersions().get(0).getAdded().getEntries().get(1).getDescription()).isEqualTo("Add example entry");
    }

    @Test
    void testUpdateChangelog_WhenVersionWithCategoryAlreadyExists_ThenNewEntryInExistingCategoryIsCreatedAndDateIsUpdated() {

      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).build();
      var version = Version.builder().releaseVersion("1.0.1").date("2024-06-01").added(added).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased, version))).build();

      // When
      updateService.updateChangelog(entity, "Add new entry", CategoryType.ADDED, "1.0.1", "2024-07-22");

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(1).getAdded().getEntries()).hasSize(2);
      assertThat(entity.getVersions().get(1).getAdded().getEntries().get(0).getDescription()).isEqualTo("Add new entry");
      assertThat(entity.getVersions().get(1).getAdded().getEntries().get(1).getDescription()).isEqualTo("Add example entry");
      assertThat(entity.getVersions().get(1).getDate()).isEqualTo("2024-07-22");
    }

    @Test
    void testUpdateChangelog_WhenVersionAndAddedCategoryAlreadyExists_ThenNewEntryAndNewCategoryIsCreated() {

      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var version = Version.builder().releaseVersion(UNRELEASED_VERSION).added(added).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(version))).build();

      // When
      updateService.updateChangelog(entity, "Remove new entry", CategoryType.REMOVED, UNRELEASED_VERSION, "");

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(0).getAdded().getEntries()).hasSize(1);
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(0).getRemoved().getEntries()).hasSize(1);
      assertThat(entity.getVersions().get(0).getRemoved().getEntries().get(0).getDescription()).isEqualTo("Remove new entry");
    }

    @Test
    void testUpdateChangelog_WhenUnreleasedVersionAlreadyExists_ThenVersionBehindUnreleasedVersionIsCreated() {

      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var version = Version.builder().releaseVersion(UNRELEASED_VERSION).added(added).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(version))).build();

      // When
      updateService.updateChangelog(entity, "Remove new entry", CategoryType.REMOVED, "1.0.0", "2024-07-22");

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(2);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo("1.0.0");
      assertThat(entity.getVersions().get(1).getDate()).isEqualTo("2024-07-22");
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(1).getRemoved().getEntries()).hasSize(1);
      assertThat(entity.getVersions().get(1).getRemoved().getEntries().get(0).getDescription()).isEqualTo("Remove new entry");
    }

    @Test
    void testUpdateChangelog_WhenCategoryTypeIsInvalid_ThenInvalidExceptionIsThrown() {

      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var version = Version.builder().releaseVersion(UNRELEASED_VERSION).added(added).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(version))).build();

      // When
      var throwAbleMethod = catchThrowable(() -> {
        updateService.updateChangelog(entity, "Remove new entry", null, "1.0.0", "2024-07-22");
      });

      // Then
      assertThat(throwAbleMethod).isNotNull().isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("Test for createNewRelease method")
  class TestCreateNewReleaseMethod {

    @Test
    void testCreateNewRelease_WhenEntityHasNoUnreleaseVersion_ThenEntityWillNotChanged() {
      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var version = Version.builder().releaseVersion("1.0.1").date("2024-06-01").added(added).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(version))).build();

      // When
      updateService.createNewRelease(entity, ReleaseType.MAJOR);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(1);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo("1.0.1");
    }

    @Test
    void testCreateNewRelease_WhenUnreleaseHasNoEntries_ThenEntityWillNotChanged() {
      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).build();
      var version = Version.builder().releaseVersion("1.0.1").date("2024-06-01").added(added).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased, version))).build();

      // When
      updateService.createNewRelease(entity, ReleaseType.MAJOR);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(2);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo("1.0.1");
    }

    @ParameterizedTest
    @CsvSource(value = {"MAJOR,1.0.0", "MINOR,0.1.0", "PATCH,0.0.1"})
    void testCreateNewRelease_WhenUnreleaseHasEntries_ThenEntityWillHaveNewVersion(ReleaseType release, String expectedReleaseVersion) {
      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var breakingChange = "Description of breaking Change";
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).added(added).breakingChange(breakingChange).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased))).build();

      // When
      updateService.createNewRelease(entity, release);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(2);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(0).getAdded().getEntries()).isEmpty();
      AssertionsForClassTypes.assertThat(entity.getVersions().get(0).getBreakingChange()).isEmpty();

      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo(expectedReleaseVersion);
      assertThat(entity.getVersions().get(1).getAdded().getEntries().get(0)).isEqualTo(entry);
      assertThat(entity.getVersions().get(1).getBreakingChange()).isEqualTo(breakingChange);
    }

    @ParameterizedTest
    @CsvSource(
        value = {"MAJOR,v1.0.1,v2.0.0", "MINOR,v1.0.1,v1.1.0", "PATCH,v1.0.1,v1.0.2", "MAJOR,release-1.0.1,release-2.0.0", "MINOR,release-1.0.1,release-1.1.0", "PATCH,release-1.0.1,release-1.0.2"})
    void testCreateNewRelease_WhenReleaseVersionHasPrefix_ThenEntityWillHaveNewVersionWithPrefix(ReleaseType release, String oldReleaseVersion, String expectedReleaseVersion) {
      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).added(added).build();
      var version = Version.builder().releaseVersion(oldReleaseVersion).date("2024-06-01").added(added).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased, version))).build();

      // When
      updateService.createNewRelease(entity, release);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(3);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo(expectedReleaseVersion);
      assertThat(entity.getVersions().get(2).getReleaseVersion()).isEqualTo(oldReleaseVersion);
    }

    @ParameterizedTest
    @ValueSource(strings = {"non-conform-version", "1.0.0.0", "v1.0.0-697bc46"})
    void testCreateNewRelease_WhenReleaseVersionHasInvalidFormat_ThenInvalidExceptionWillBeThrown(String invalidVersion) {
      // Given
      var entry = VersionEntry.builder().description("Add example entry").build();
      var added = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entry))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).added(added).build();
      var version = Version.builder().releaseVersion(invalidVersion).date("2024-06-01").added(added).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased, version))).build();

      // When
      var throwAbleMethod = catchThrowable(() -> {
        updateService.createNewRelease(entity, ReleaseType.MAJOR);
      });

      // Then
      assertThat(throwAbleMethod).isInstanceOf(IllegalArgumentException.class).hasMessage("Invalid version format: " + invalidVersion);
    }

    @ParameterizedTest
    @CsvSource(value = {"MAJOR,2.0.0", "MINOR,1.1.0", "PATCH,1.0.2"})
    void testCreateNewRelease_WhenUnreleaseHasEntriesAndAdditionalVersionsExists_ThenEntityWillHaveNewVersionBetweenUnreleasedAndExistingVersions(ReleaseType release, String expectedReleaseVersion) {
      // Given
      var entryAdded = VersionEntry.builder().description("Add example entry").build();
      var categoryAdded = CategoryAdded.builder().entries(new ArrayList<>(Arrays.asList(entryAdded))).build();
      var entryChanged = VersionEntry.builder().description("Change structure of VersionEntity").build();
      var categoryChanged = CategoryChanged.builder().entries(new ArrayList<>(Arrays.asList(entryChanged))).build();
      var entryFixed = VersionEntry.builder().description("Replace application name in MAKE.md file").build();
      var categoryFixed = CategoryFixed.builder().entries(new ArrayList<>(Arrays.asList(entryFixed))).build();
      var entryRemoved01 = VersionEntry.builder().description("Remove unused method `parse()`").build();
      var entryRemoved02 = VersionEntry.builder().description("Delete check for NullpointerException in `main()` method").build();
      var categoryRemoved = CategoryRemoved.builder().entries(new ArrayList<>(Arrays.asList(entryRemoved01, entryRemoved02))).build();
      var entrySecurity = VersionEntry.builder().description("Fix a buffer overflow").build();
      var categorySecurity = CategorySecurity.builder().entries(new ArrayList<>(Arrays.asList(entrySecurity))).build();
      var entryDeprecated = VersionEntry.builder().description("Set api as deprecated").build();
      var categoryDeprecated = CategoryDeprecated.builder().entries(new ArrayList<>(Arrays.asList(entryDeprecated))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).added(categoryAdded).changed(categoryChanged).fixed(categoryFixed).removed(categoryRemoved).security(categorySecurity).deprecated(categoryDeprecated).build();
      var version = Version.builder().releaseVersion("1.0.1").date("2024-06-01").added(categoryAdded).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased, version))).build();

      // When
      updateService.createNewRelease(entity, release);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(3);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(0).getAdded().getEntries()).isEmpty();
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(0).getChanged().getEntries()).isEmpty();
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(0).getFixed().getEntries()).isEmpty();
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(0).getRemoved().getEntries()).isEmpty();

      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo(expectedReleaseVersion);
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(1).getAdded().getEntries()).hasSize(1);
      assertThat(entity.getVersions().get(1).getAdded().getEntries()).isEqualTo(Arrays.asList(entryAdded));
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(1).getChanged().getEntries()).hasSize(1);
      assertThat(entity.getVersions().get(1).getChanged().getEntries()).isEqualTo(Arrays.asList(entryChanged));
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(1).getFixed().getEntries()).hasSize(1);
      assertThat(entity.getVersions().get(1).getFixed().getEntries()).isEqualTo(Arrays.asList(entryFixed));
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(1).getRemoved().getEntries()).hasSize(2);
      assertThat(entity.getVersions().get(1).getRemoved().getEntries()).isEqualTo(Arrays.asList(entryRemoved01, entryRemoved02));
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(1).getSecurity().getEntries()).hasSize(1);
      assertThat(entity.getVersions().get(1).getSecurity().getEntries()).isEqualTo(Arrays.asList(entrySecurity));
      AssertionsForInterfaceTypes.assertThat(entity.getVersions().get(1).getDeprecated().getEntries()).hasSize(1);
      assertThat(entity.getVersions().get(1).getDeprecated().getEntries()).isEqualTo(Arrays.asList(entryDeprecated));

      assertThat(entity.getVersions().get(2).getReleaseVersion()).isEqualTo("1.0.1");
    }

    @Test
    void testCreateNewRelease_WhenUnreleaseHasOnlyChangedEntries_ThenEntityWillHaveNewVersion() {
      // Given
      var entryChanged = VersionEntry.builder().description("Change structure of VersionEntity").build();
      var categoryChanged = CategoryChanged.builder().entries(new ArrayList<>(Arrays.asList(entryChanged))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).changed(categoryChanged).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased))).build();

      // When
      updateService.createNewRelease(entity, ReleaseType.MINOR);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(2);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo("0.1.0");
    }

    @Test
    void testCreateNewRelease_WhenUnreleaseHasOnlyFixedEntries_ThenEntityWillHaveNewVersion() {
      // Given
      var entryFixed = VersionEntry.builder().description("Replace application name in MAKE.md file").build();
      var categoryFixed = CategoryFixed.builder().entries(new ArrayList<>(Arrays.asList(entryFixed))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).fixed(categoryFixed).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased))).build();

      // When
      updateService.createNewRelease(entity, ReleaseType.PATCH);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(2);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo("0.0.1");
    }

    @Test
    void testCreateNewRelease_WhenUnreleaseHasOnlyRemovedEntries_ThenEntityWillHaveNewVersion() {
      // Given
      var entryRemoved = VersionEntry.builder().description("Remove unused method `parse()`").build();
      var categoryRemoved = CategoryRemoved.builder().entries(new ArrayList<>(Arrays.asList(entryRemoved))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).removed(categoryRemoved).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased))).build();

      // When
      updateService.createNewRelease(entity, ReleaseType.MINOR);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(2);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo("0.1.0");
    }

    @Test
    void testCreateNewRelease_WhenUnreleaseHasOnlySecurityEntries_ThenEntityWillHaveNewVersion() {
      // Given
      var entrySecurity = VersionEntry.builder().description("Remove unused method `parse()`").build();
      var categorySecurity = CategorySecurity.builder().entries(new ArrayList<>(Arrays.asList(entrySecurity))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).security(categorySecurity).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased))).build();

      // When
      updateService.createNewRelease(entity, ReleaseType.MINOR);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(2);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo("0.1.0");
    }

    @Test
    void testCreateNewRelease_WhenUnreleaseHasOnlyDeprecatedEntries_ThenEntityWillHaveNewVersion() {
      // Given
      var entryDeprecated = VersionEntry.builder().description("Remove unused method `parse()`").build();
      var categoryDeprecated = CategoryDeprecated.builder().entries(new ArrayList<>(Arrays.asList(entryDeprecated))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).deprecated(categoryDeprecated).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased))).build();

      // When
      updateService.createNewRelease(entity, ReleaseType.MINOR);

      // Then
      AssertionsForInterfaceTypes.assertThat(entity.getVersions()).hasSize(2);
      assertThat(entity.getVersions().get(0).getReleaseVersion()).isEqualTo(UNRELEASED_VERSION);
      assertThat(entity.getVersions().get(1).getReleaseVersion()).isEqualTo("0.1.0");
    }

    @Test
    void testCreateNewRelease_WhenReleaseTypeIsNull_ThenIllegalArgumentExceptionIsThrown() {
      // Given
      var entryRemoved = VersionEntry.builder().description("Remove unused method `parse()`").build();
      var categoryRemoved = CategoryRemoved.builder().entries(new ArrayList<>(Arrays.asList(entryRemoved))).build();
      var versionUnreleased = Version.builder().releaseVersion(UNRELEASED_VERSION).removed(categoryRemoved).build();
      var entity = Entity.builder().header(EntityHeader.builder().build()).versions(new ArrayList<>(Arrays.asList(versionUnreleased))).build();

      // When
      var throwAbleMethod = catchThrowable(() -> {
        updateService.createNewRelease(entity, null);
      });

      // Then
      assertThat(throwAbleMethod).isNotNull().isInstanceOf(IllegalArgumentException.class);
    }
  }

}
