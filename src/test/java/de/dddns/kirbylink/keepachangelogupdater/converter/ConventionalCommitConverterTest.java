package de.dddns.kirbylink.keepachangelogupdater.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import de.dddns.kirbylink.keepachangelogupdater.config.ConventionalCommitConfiguration;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.ReleaseType;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Version;
import de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits.Commit;

@ExtendWith(MockitoExtension.class)
class ConventionalCommitConverterTest {

  @Mock
  private ConventionalCommitConfiguration conventionalCommitConfiguration;

  @InjectMocks
  private ConventionalCommitConverter conventionalCommitConverter;

  @Test
  void testConvertCommitsToChangelogEntries_WhenCommitsContainsTypesForChangelog_ThenVersionContainsCommitDescriptionInMappedCategories() {
      // Given
      List<Commit> commits = List.of(
          Commit.builder().type("feat").description("Implement a new feature").build(),
          Commit.builder().type("fix").description("Fix a bug").build(),
          Commit.builder().type("build(deps)").description("Update Maven dependencies").build(),
          Commit.builder().type("perf").description("Improve performance").build(),
          Commit.builder().type("feat").description("Another feature").breakingChange("BREAKING CHANGE: API was changed").build(),
          Commit.builder().type("chore(removed)").description("Remove API").breakingChange("BREAKING CHANGE: API was removed").build(),
          Commit.builder().type("fix(security)").description("Fix a bufferoverflow").build(),
          Commit.builder().type("chore(deprecated)").description("Deprecate API in future version").build()
      );
      var unreleasedVersion = Version.builder().releaseVersion("Unreleased").build();

      when(conventionalCommitConfiguration.getAddedTypes()).thenReturn(List.of("feat"));
      when(conventionalCommitConfiguration.getFixedTypes()).thenReturn(List.of("fix"));
      when(conventionalCommitConfiguration.getChangedTypes()).thenReturn(List.of("perf", "build(deps)"));
      when(conventionalCommitConfiguration.getRemovedTypes()).thenReturn(List.of("chore(removed)"));
      when(conventionalCommitConfiguration.getSecurityTypes()).thenReturn(List.of("fix(security)"));
      when(conventionalCommitConfiguration.getDeprecatedTypes()).thenReturn(List.of("chore(deprecated)"));

      // When
      var updatedVersion = conventionalCommitConverter.convertCommitsToChangelogEntries(commits, unreleasedVersion);

      // Then
      assertThat(updatedVersion).isNotNull();
      AssertionsForInterfaceTypes.assertThat(updatedVersion.getAdded().getEntries()).hasSize(2);
      AssertionsForInterfaceTypes.assertThat(updatedVersion.getFixed().getEntries()).hasSize(1);
      AssertionsForInterfaceTypes.assertThat(updatedVersion.getChanged().getEntries()).hasSize(2);
      AssertionsForInterfaceTypes.assertThat(updatedVersion.getSecurity().getEntries()).hasSize(1);
      AssertionsForInterfaceTypes.assertThat(updatedVersion.getDeprecated().getEntries()).hasSize(1);
      assertThat(updatedVersion.getBreakingChange()).contains("API was changed").contains("API was removed");
    }

  @Nested
  @DisplayName("Tests for convertToEntity method")
  class CalculateReleaseTypeTests {
    @ParameterizedTest(name = "{1} => {2}")
    @CsvSource(value = {
        "'feat', 'Add new API', MINOR",
        "'fix', 'Correct a bug', PATCH",
        "'perf', 'Improve performance', PATCH",
        "'build(deps)', 'Update dependencies', PATCH",
        "'chore', 'General maintenance', NULL",
        "'refactor', 'Code cleanup', NULL",
        "'docs', 'Update documentation', NULL",
        "'feat!', 'Introduce breaking change', MAJOR",
        "'fix(security)', 'Fix a bufferoverflow', PATCH",
        "'chore(deprecated)', 'Deprecate API', NULL",
        "'chore(removed)!', 'Remove existing API', MAJOR",
        "'chore(removed)', 'Remove existing API', NULL"
    }, nullValues = "NULL")
    void testDetermineReleaseType_WhenSingleCommit_ThenexpectedReleaseTypeIsReturned(String type, String description, ReleaseType expectedReleaseType) {
      // Given
      var commit = Commit.builder().type(type).description(description).build();
      List<Commit> commits = List.of(commit);

      lenient().when(conventionalCommitConfiguration.getAddedTypes()).thenReturn(List.of("feat"));
      lenient().when(conventionalCommitConfiguration.getFixedTypes()).thenReturn(List.of("fix"));
      lenient().when(conventionalCommitConfiguration.getChangedTypes()).thenReturn(List.of("perf", "build(deps)"));
      lenient().when(conventionalCommitConfiguration.getRemovedTypes()).thenReturn(List.of("chore(removed)"));
      lenient().when(conventionalCommitConfiguration.getSecurityTypes()).thenReturn(List.of("fix(security)"));
      lenient().when(conventionalCommitConfiguration.getDeprecatedTypes()).thenReturn(List.of("chore(deprecated)"));

      lenient().when(conventionalCommitConfiguration.getMajorTypes()).thenReturn(Collections.emptyList());
      lenient().when(conventionalCommitConfiguration.getMinorTypes()).thenReturn(List.of("feat"));
      lenient().when(conventionalCommitConfiguration.getPatchTypes()).thenReturn(List.of("fix", "fix(security)", "build(deps)", "perf"));

      // When
      var result = conventionalCommitConverter.determineReleaseType(commits);

      // Then
      assertThat(result).isEqualTo(expectedReleaseType);
    }

    @Test
    void testDetermineReleaseType_WhenMultipleCommitsWithMajorAndPatchChange_ThenReturnMajor() {
      // Given
      List<Commit> commits = List.of(Commit.builder().type("fix").description("Correct a bug").build(), Commit.builder().type("feat!").description("Add new API with breaking changes").build(),
          Commit.builder().type("perf").description("Improve performance").build());

      // When
      var result = conventionalCommitConverter.determineReleaseType(commits);

      // Then
      assertThat(result).isEqualTo(ReleaseType.MAJOR);
    }

    @Test
    void testDetermineReleaseType_WhenMultipleCommitsWithMinorAndPatchChange_ThenReturnMinor() {
      // Given
      List<Commit> commits = List.of(Commit.builder().type("fix").description("Correct a bug").build(), Commit.builder().type("feat").description("Add a minor feature").build(),
          Commit.builder().type("perf").description("Improve performance").build());

      when(conventionalCommitConfiguration.getMajorTypes()).thenReturn(Collections.emptyList());
      when(conventionalCommitConfiguration.getMinorTypes()).thenReturn(List.of("feat"));

      // When
      var result = conventionalCommitConverter.determineReleaseType(commits);

      // Then
      assertThat(result).isEqualTo(ReleaseType.MINOR);
    }

    @Test
    void testDetermineReleaseType_WhenMultipleCommitsWithOnlyPatchChange_ThenReturnPatch() {
      // Given
      List<Commit> commits = List.of(Commit.builder().type("fix").description("Correct a bug").build(), Commit.builder().type("build(deps)").description("Update dependencies").build());

      when(conventionalCommitConfiguration.getMajorTypes()).thenReturn(Collections.emptyList());
      when(conventionalCommitConfiguration.getMinorTypes()).thenReturn(List.of("feat"));
      when(conventionalCommitConfiguration.getPatchTypes()).thenReturn(List.of("fix", "build(deps)", "perf"));

      // When
      var result = conventionalCommitConverter.determineReleaseType(commits);

      // Then
      assertThat(result).isEqualTo(ReleaseType.PATCH);
    }

    @Test
    void testDetermineReleaseType_WhenNoDefinedTypeButBreakingChangeFilled_ThenReturnMajor() {
      // Given
      List<Commit> commits = List.of(Commit.builder().type("docs").description("Update documentation").build(),
          Commit.builder().type("chore(removed)").description("Remove existing API").breakingChange("User can't call the API anymore").build());

      // When
      var result = conventionalCommitConverter.determineReleaseType(commits);

      // Then
      assertThat(result).isEqualTo(ReleaseType.MAJOR);
    }

    @Test
    void testDetermineReleaseType_WhenNoRelevantCommits_ThenReturnNull() {
      // Given
      List<Commit> commits = List.of(Commit.builder().type("docs").description("Update documentation").build(), Commit.builder().type("chore").description("General maintenance").build());

      // When
      var result = conventionalCommitConverter.determineReleaseType(commits);

      // Then
      assertThat(result).isNull();
    }
  }
}
