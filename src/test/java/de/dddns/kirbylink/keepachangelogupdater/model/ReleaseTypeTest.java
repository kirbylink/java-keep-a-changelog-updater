package de.dddns.kirbylink.keepachangelogupdater.model;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ReleaseTypeTest {

  @ParameterizedTest
  @CsvSource(value = {"major", "minor", "patch", "Major", "MINOR", "paTCh"})
  void testFromValue_WhenValidValuesAreGiven_ThenNoExceptionWillBeThrown(String value) {

    // Given

    // When
    var throwAbleMethod = catchThrowable(() -> {
      ReleaseType.fromValue(value);
    });

    // Then
    assertThat(throwAbleMethod).isNull();
  }

  @Test
  void testFromValue_WhenInvalidValuesAreGiven_ThenIllegalArgumentExceptionWillBeThrown() {

    // Given
    var value = "invalidValue";

    // When
    var throwAbleMethod = catchThrowable(() -> {
      ReleaseType.fromValue(value);
    });

    // Then
    assertThat(throwAbleMethod).isNotNull().isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @MethodSource ("provideArgumentsForGetValueMethod")
  void testGetValue(ReleaseType releaseType, String expectedValue) {

    // Given

    // When
    var actualValue = releaseType.getValue();

    // Then
    assertThat(actualValue).isEqualTo(expectedValue);
  }

  private static Stream<Arguments> provideArgumentsForGetValueMethod() {
    return Stream.of(
            Arguments.of(ReleaseType.MAJOR, "major"),
            Arguments.of(ReleaseType.MINOR, "minor"),
            Arguments.of(ReleaseType.PATCH, "patch")
    );
  }
}
