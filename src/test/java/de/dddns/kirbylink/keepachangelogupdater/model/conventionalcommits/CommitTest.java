package de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CommitTest {

  @ParameterizedTest(name = "{0} && {1} => has breaking change: {2}")
  @CsvSource(value = {
      "'feat', NULL, false",
      "'feat', '', false",
      "'feat', ' ', false",
      "'feat!', NULL, true",
      "'feat!', '', true",
      "'feat!', ' ', true",
      "'feat!', 'Introduce breaking change', true",
      "'feat', 'Introduce breaking change', true"
  }, nullValues = "NULL")
  void testHasBreakingChange(String type, String breakingChange, boolean expectedHasBreakingChange) {
    // Given

    // When
    var commit = Commit.builder().type(type).breakingChange(breakingChange).build();

    // Then
    assertThat(commit.hasBreakingChange()).isEqualTo(expectedHasBreakingChange);
  }

}
