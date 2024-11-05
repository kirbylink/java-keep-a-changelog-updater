package de.dddns.kirbylink.keepachangelogupdater.config;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;

class ConventionalCommitConfigurationTest {

  @Test
  void testConventionalCommitConfiguration_WhenNoConfigurationPathIsSet_ThenNoExceptionWillBeThrown() {
    // Given

    // When
    var throwAbleMethod = catchThrowable(() -> {
      new ConventionalCommitConfiguration(null);
    });

    // Then
    assertThat(throwAbleMethod).isNull();
  }

  @Test
  void testConventionalCommitConfiguration_WhenInvalidCOnfigurationPathIsSet_ThenFileNotFoundExceptionIsThrown() {
    // Given

    // When
    var throwAbleMethod = catchThrowable(() -> {
      new ConventionalCommitConfiguration("not/existent");
    });

    // Then
    assertThat(throwAbleMethod).isInstanceOf(FileNotFoundException.class);
  }

  @Test
  void testGetMajorTypes() throws IOException {
    // Given

    // When
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(null);
    var actualList = conventionalCommitConfiguration.getMajorTypes();

    // Then
    AssertionsForInterfaceTypes.assertThat(actualList).isEmpty();
  }

  @Test
  void testGetMinorTypes() throws IOException {
    // Given

    // When
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(null);
    var actualList = conventionalCommitConfiguration.getMinorTypes();

    // Then
    AssertionsForInterfaceTypes.assertThat(actualList).contains("feat");
  }

  @Test
  void testGetPatchTypes() throws IOException {
    // Given

    // When
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(null);
    var actualList = conventionalCommitConfiguration.getPatchTypes();

    // Then
    AssertionsForInterfaceTypes.assertThat(actualList).contains("fix", "build(deps)", "perf");
  }

  @Test
  void testGetAddedTypes() throws IOException {
    // Given

    // When
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(null);
    var actualList = conventionalCommitConfiguration.getAddedTypes();

    // Then
    AssertionsForInterfaceTypes.assertThat(actualList).contains("feat");
  }

  @Test
  void testGetFixedTypes() throws IOException {
    // Given

    // When
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(null);
    var actualList = conventionalCommitConfiguration.getFixedTypes();

    // Then
    AssertionsForInterfaceTypes.assertThat(actualList).contains("fix");
  }

  @Test
  void testGetChangedTypes() throws IOException {
    // Given

    // When
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(null);
    var actualList = conventionalCommitConfiguration.getChangedTypes();

    // Then
    AssertionsForInterfaceTypes.assertThat(actualList).contains("perf", "build(deps)");
  }

  @Test
  void testGetRemovedTypes() throws IOException {
    // Given

    // When
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(null);
    var actualList = conventionalCommitConfiguration.getRemovedTypes();

    // Then
    AssertionsForInterfaceTypes.assertThat(actualList).contains("chore(removed)");
  }

  @Test
  void testGetSecurityTypes() throws IOException {
    // Given

    // When
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(null);
    var actualList = conventionalCommitConfiguration.getSecurityTypes();

    // Then
    AssertionsForInterfaceTypes.assertThat(actualList).contains("fix(security)");
  }

  @Test
  void testGetDeprecatedTypes() throws IOException {
    // Given

    // When
    var conventionalCommitConfiguration = new ConventionalCommitConfiguration(null);
    var actualList = conventionalCommitConfiguration.getDeprecatedTypes();

    // Then
    AssertionsForInterfaceTypes.assertThat(actualList).contains("chore(deprecated)");
  }

}
