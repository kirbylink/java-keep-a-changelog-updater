package de.dddns.kirbylink.keepachangelogupdater.model.changelog;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ScenarioTest {

  @ParameterizedTest
  @CsvSource(value = {"create", "add-entry", "release", "Create", "ADD-ENTRY", "reLeaSe"})
  void testFromValue_WhenValidValuesAreGiven_ThenNoExceptionWillBeThrown(String value) {
    // Given

    // When
    var throwAbleMethod = catchThrowable(() -> {
      Scenario.fromValue(value);
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
      Scenario.fromValue(value);
    });

    // Then
    assertThat(throwAbleMethod).isNotNull().isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @MethodSource ("provideArgumentsForGetValueMethod")
  void testGetValue(Scenario scenario, String expectedValue) {
    // Given

    // When
    var actualValue = scenario.getValue();

    // Then
    assertThat(actualValue).isEqualTo(expectedValue);
  }

  private static Stream<Arguments> provideArgumentsForGetValueMethod() {
    return Stream.of(
            Arguments.of(Scenario.CREATE, "create"),
            Arguments.of(Scenario.ADD_ENTRY, "add-entry"),
            Arguments.of(Scenario.RELEASE, "release")
    );
  }
}
