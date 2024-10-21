package de.dddns.kirbylink.keepachangelogupdater.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.List;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import de.dddns.kirbylink.keepachangelogupdater.converter.GitLogParser;
import de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits.Commit;

@ExtendWith(MockitoExtension.class)
class ConventionalCommitParserServiceTest {

  @Mock
  GitLogParser gitLogParser;

  @InjectMocks
  ConventionalCommitParserService conventionalCommitParserService;

  @Test
  void testParseGitLog_WhenGitLogContainsTwoConventionalCommits_ThenListWithTwoCommitsIsReturned() {
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

    var commit01String = "commit 91b9a52a7c655d356289c2607d32a2e1a97277c2\nAuthor: kirbylink <kirbylink@github.com>\nDate:   Thu Oct 3 13:43:47 2024 +0200\n\n    feat: Implement git log parsing\n\n";
    var commit02String = "commit 31e22d05c93b4e2c7ab1b98c6beec4e6ac86ed72\nAuthor: kirbylink <kirbylink@github.com>\nDate:   Thu Oct 3 13:42:47 2024 +0200\n\n    fix: Correct minor bug in parsing logic\n";

    var commit01 = Commit.builder().description("commit 01").build();
    var commit02 = Commit.builder().description("commit 02").build();

    when(gitLogParser.splitGitLog(anyString())).thenReturn(List.of(commit01String, commit02String));
    when(gitLogParser.parseGitCommit(commit01String)).thenReturn(commit01);
    when(gitLogParser.parseGitCommit(commit02String)).thenReturn(commit02);
    when(gitLogParser.filterCommitsAndMergeOptionalBreakingChanges(anyList())).thenReturn(List.of(commit01, commit02));

    // When
    var commits = conventionalCommitParserService.parseGitLog(gitLog);

    // Then
    AssertionsForInterfaceTypes.assertThat(commits).hasSize(2);
    assertThat(commits.get(0)).isEqualTo(commit01);
    assertThat(commits.get(1)).isEqualTo(commit02);
  }

  @Test
  void testParseGitLog_WhenGitLogContainsOneNonConventionalCommit_ThenListWithSingleCommitIsReturned() {
    // Given
    var gitLog = """
        commit 91b9a52a7c655d356289c2607d32a2e1a97277c2
        Author: kirbylink <kirbylink@github.com>
        Date:   Thu Oct 3 13:43:47 2024 +0200

            feat: Implement git log parsing

        commit 31e22d05c93b4e2c7ab1b98c6beec4e6ac86ed72
        Author: kirbylink <kirbylink@github.com>
        Date:   Thu Oct 3 13:42:47 2024 +0200

            Correct minor bug in parsing logic
        """;

    var commit01String = "commit 91b9a52a7c655d356289c2607d32a2e1a97277c2\nAuthor: kirbylink <kirbylink@github.com>\nDate:   Thu Oct 3 13:43:47 2024 +0200\n\n    feat: Implement git log parsing\n\n";
    var commit02String = "commit 31e22d05c93b4e2c7ab1b98c6beec4e6ac86ed72\nAuthor: kirbylink <kirbylink@github.com>\nDate:   Thu Oct 3 13:42:47 2024 +0200\n\n    Correct minor bug in parsing logic\n";

    var commit01 = Commit.builder().description("commit 01").build();

    when(gitLogParser.splitGitLog(anyString())).thenReturn(List.of(commit01String, commit02String));
    when(gitLogParser.parseGitCommit(commit01String)).thenReturn(commit01);
    when(gitLogParser.parseGitCommit(commit02String)).thenReturn(null);
    when(gitLogParser.filterCommitsAndMergeOptionalBreakingChanges(anyList())).thenReturn(List.of(commit01));

    // When
    var commits = conventionalCommitParserService.parseGitLog(gitLog);

    // Then
    AssertionsForInterfaceTypes.assertThat(commits).hasSize(1);
    assertThat(commits.get(0)).isEqualTo(commit01);
  }

}
