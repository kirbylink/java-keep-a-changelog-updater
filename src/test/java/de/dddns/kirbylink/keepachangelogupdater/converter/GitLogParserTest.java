package de.dddns.kirbylink.keepachangelogupdater.converter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits.Commit;

class GitLogParserTest {

  @Test
  void testSplitGitLog_WhenStringContainsTwoValidCommitsFromGitLog_ThenListWithTwoStringsIsReturned() {
    // Given
    var gitLog = """
        commit 91b9a52a7c655d356289c2607d32a2e1a97277c2
        Author: kirbylink <kirbylink@github.com>
        Date:   Thu Oct 3 13:43:47 2024 +0200

            feat: Implement git log parsing

        commit 31e22d05c93b4e2c7ab1b98c6beec4e6ac86ed72
        Author: kirbylink <kirbylink@github.com>
        Date:   Thu Oct 3 13:42:47 2024 +0200

            fix: Correct minor bug in parsing logic
        """;

    // When
    var gitLogParser = new GitLogParser();
    var commits = gitLogParser.splitGitLog(gitLog);

    // Then
    AssertionsForInterfaceTypes.assertThat(commits).hasSize(2);
    assertThat(commits.get(0)).isEqualTo("commit 91b9a52a7c655d356289c2607d32a2e1a97277c2\nAuthor: kirbylink <kirbylink@github.com>\nDate:   Thu Oct 3 13:43:47 2024 +0200\n\n    feat: Implement git log parsing\n\n");
    assertThat(commits.get(1)).isEqualTo("commit 31e22d05c93b4e2c7ab1b98c6beec4e6ac86ed72\nAuthor: kirbylink <kirbylink@github.com>\nDate:   Thu Oct 3 13:42:47 2024 +0200\n\n    fix: Correct minor bug in parsing logic\n");
  }

  @Nested
  @DisplayName("Tests for parseGitLog method")
  class TestParseGitLogTests {

    @Test
    void testParseGitLog_WhenGitLogContainsConventionalCommit_ThenCommitObjectIsReturned() {
        // Given
        var gitCommit = """
                commit 91b9a52a7c655d356289c2607d32a2e1a97277c2
                Author: kirbylink <kirbylink@github.com>
                Date:   Thu Oct 3 13:43:47 2024 +0200

                    feat: Implement git log parsing

                """;

        // When
        var gitLogParser = new GitLogParser();
        var commit = gitLogParser.parseGitCommit(gitCommit);

        // Then
        assertThat(commit.getHashCode()).isEqualTo("91b9a52a7c655d356289c2607d32a2e1a97277c2");
        assertThat(commit.getType()).isEqualTo("feat");
        assertThat(commit.getDescription()).isEqualTo("Implement git log parsing");
    }

    @Test
    void testParseGitLog_WhenGitLogContainsConventionalCommitWithScope_ThenCommitObjectWithScopeInTypeIsReturned() {
      // Given
      var gitCommit = """
                commit 1adc832ebc22aee29f468c21e4b0ba1c7b868216
                Author: david <david@phoenix.ipv64.de>
                Date:   Sun Oct 20 17:30:31 2024 +0200

                    build(deps): Update Maven dependencies

                """;

      // When
      var gitLogParser = new GitLogParser();
      var commit = gitLogParser.parseGitCommit(gitCommit);

      // Then
      assertThat(commit.getHashCode()).isEqualTo("1adc832ebc22aee29f468c21e4b0ba1c7b868216");
      assertThat(commit.getType()).isEqualTo("build(deps)");
      assertThat(commit.getDescription()).isEqualTo("Update Maven dependencies");
    }

    @Test
    void testParseGitLog_WhenGitCommitDoesNotContainConventionalCommit_ThenNullIsReturned() {
      // Given
      var gitCommit = """
                commit 6672e54d7d685591348314ae9f568b5509d58406
                Author: kirbylink <kirbylink@github.com>
                Date:   Tue Oct 1 06:34:56 2024 +0000

                    Update for next development version

                """;

      // When
      var gitLogParser = new GitLogParser();
      var commit = gitLogParser.parseGitCommit(gitCommit);

      // Then
      assertThat(commit).isNull();
    }

    @Test
    void testParseGitLog_WhenMessageContainsBreakingChange_ThenBreakingChangeIsSavedInCommit() {
        // Given
        var gitCommit = """
                commit 91b9a52a7c655d356289c2607d32a2e1a97277c2
                Author: kirbylink <kirbylink@github.com>
                Date:   Thu Oct 3 13:43:47 2024 +0200

                    feat: Implement git log parsing

                    BREAKING CHANGE: This changes the API

                """;

        // When
        var gitLogParser = new GitLogParser();
        var commit = gitLogParser.parseGitCommit(gitCommit);

        // Then
        assertThat(commit.getBreakingChange()).isEqualTo("This changes the API");
    }

    @Test
    void testParseGitLog_WhenMessageContainsScopeAndBreakingChange_ThenBreakingChangeIsSavedInCommit() {
      // Given
      var gitCommit = """
                commit 91b9a52a7c655d356289c2607d32a2e1a97277c2
                Author: kirbylink <kirbylink@github.com>
                Date:   Thu Oct 3 13:43:47 2024 +0200

                    feat(api)!: Implement git log parsing

                """;

      // When
      var gitLogParser = new GitLogParser();
      var commit = gitLogParser.parseGitCommit(gitCommit);

      // Then
      assertThat(commit.hasBreakingChange()).isTrue();
    }

    @Test
    void testParseGitLog_WhenMessageContainsNoBreakingChangesButMultipleLines_ThenBreakingChangesIsNullAndBodyIsNotEmptyInCommit() {
      // Given
      var gitCommit = """
                commit 91b9a52a7c655d356289c2607d32a2e1a97277c2
                Author: kirbylink <kirbylink@github.com>
                Date:   Thu Oct 3 13:43:47 2024 +0200

                    feat: Implement git log parsing

                    Some amazing description how it is done.
                    And another information.
                """;

      // When
      var gitLogParser = new GitLogParser();
      var commit = gitLogParser.parseGitCommit(gitCommit);

      // Then
      assertThat(commit.getBreakingChange()).isNull();
      assertThat(commit.getBody()).isEqualTo("Some amazing description how it is done.\nAnd another information.");
    }

    @Test
    void testParseGitLog_WhenMessageContainsBreakingChangesWithMultipleLinesBeforeAndInBreakingChanges_ThenBodyIsNotEmptyAndBreakingChangesIsNotNullAndDoesNotContainTheWholeBodyInCommit() {
      // Given
      var gitCommit = """
                commit 91b9a52a7c655d356289c2607d32a2e1a97277c2
                Author: kirbylink <kirbylink@github.com>
                Date:   Thu Oct 3 13:43:47 2024 +0200

                    feat: Implement git log parsing

                    Some amazing description how it is done.
                    And another information.

                    BREAKING CHANGE: Some Multiple lines
                    that shows that there are breaking changes.
                """;

      // When
      var gitLogParser = new GitLogParser();
      var commit = gitLogParser.parseGitCommit(gitCommit);

      // Then
      assertThat(commit.getBody()).isEqualTo("Some amazing description how it is done.\nAnd another information.\n\nBREAKING CHANGE: Some Multiple lines\nthat shows that there are breaking changes.");
      assertThat(commit.getBreakingChange()).isEqualTo("Some Multiple lines\nthat shows that there are breaking changes.");
    }

    @Test
    void testParseGitLog_WhenMessageDoesNotFollowTheConventionalCommitsButContainsBreakingChanges_ThenNullIsReturned() {
      // Given
      var gitCommit = """
                commit 91b9a52a7c655d356289c2607d32a2e1a97277c2
                Author: kirbylink <kirbylink@github.com>
                Date:   Thu Oct 3 13:43:47 2024 +0200

                    Implement git log parsing

                    Some amazing description how it is done.
                    And another information.

                    BREAKING CHANGE: Some Multiple lines
                    that shows that there are breaking changes.
                """;

      // When
      var gitLogParser = new GitLogParser();
      var commit = gitLogParser.parseGitCommit(gitCommit);

      // Then
      assertThat(commit).isNull();
    }
  }

  @Nested
  @DisplayName("Tests for filterCommitsAndMergeOptionalBreakingChange method")
  class TestFilterCommitsAndMergeOptionalBreakingChangesTests {

    @Test
    void test_WhenDuplicatedCommitsWithoutBreakingChanges_ThenOnlySingleCommitsAreCollected() {

      // Given
      var commit01 = Commit.builder()
          .hashCode("8043beb1155c19611e6e262ef3076f73c8c9fe3f")
          .type("Fix")
          .description("Filtering existing tags and pushing only new tags to github")
          .body("Adding first implementation of method")
          .build();

      var commitDuplicate = Commit.builder()
          .hashCode("6672e54d7d685591348314ae9f568b5509d58406")
          .type("Fix")
          .description("Filtering existing tags and pushing only new tags to github")
          .body("Changing method structure")
          .build();

      var commit03 = Commit.builder()
          .hashCode("91b9a52a7c655d356289c2607d32a2e1a97277c2")
          .type("feat")
          .description("Implement git log parsing")
          .build();

      var commit04 = Commit.builder()
          .hashCode("31e22d05c93b4e2c7ab1b98c6beec4e6ac86ed72")
          .type("fix")
          .description("Correct minor bug in parsing logic")
          .build();

      var immutableListOfCommits = List.of(
          commit01,
          commitDuplicate,
          commit03,
          commit04);

      var commits = new ArrayList<Commit>();
      commits.addAll(immutableListOfCommits);

      // When
      var gitLogParser = new GitLogParser();
      var filteredCommits = gitLogParser.filterCommitsAndMergeOptionalBreakingChanges(commits);

      // Then
      AssertionsForInterfaceTypes.assertThat(filteredCommits).hasSize(3).containsExactlyInAnyOrder(commit01, commit03, commit04);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "'API a broken!','API b broken!','API a broken!\nAPI b broken!'",
        "'API a broken!',NULL,'API a broken!'",
        "NULL,'API b broken!','API b broken!'",
        "NULL,NULL,NULL"
    }, nullValues = "NULL")
    void test_WhenDuplicatedCommitsWithBreakingChanges_ThenSingleCommitWithMergedBreakingChangesCollected(String breakingChangeCommit01, String breakingChangeCommit02, String expectedMergedBreakingChange) {

      // Given
      var commit01 = Commit.builder()
          .hashCode("8043beb1155c19611e6e262ef3076f73c8c9fe3f")
          .type("fix")
          .description("Filtering existing tags and pushing only new tags to github")
          .body("Adding first implementation of method")
          .breakingChange(breakingChangeCommit01)
          .build();

      var commit02 = Commit.builder()
          .hashCode("6672e54d7d685591348314ae9f568b5509d58406")
          .type("fix")
          .description("Filtering existing tags and pushing only new tags to github")
          .body("Changing method structure")
          .breakingChange(breakingChangeCommit02)
          .build();

      var immutableListOfCommits = List.of(
          commit01,
          commit02);

      var commits = new ArrayList<Commit>();
      commits.addAll(immutableListOfCommits);

      // When
      var gitLogParser = new GitLogParser();
      var filteredCommits = gitLogParser.filterCommitsAndMergeOptionalBreakingChanges(commits);

      // Then
      AssertionsForInterfaceTypes.assertThat(filteredCommits).hasSize(1);
      var commit = filteredCommits.get(0);
      assertThat(commit.getBreakingChange()).isEqualTo(expectedMergedBreakingChange);
    }
  }

}
