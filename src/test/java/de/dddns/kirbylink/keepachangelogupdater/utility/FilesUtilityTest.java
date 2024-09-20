package de.dddns.kirbylink.keepachangelogupdater.utility;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.io.IOException;
import java.nio.file.Files;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;

class FilesUtilityTest {

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
      [0.0.1]: https://git.example.com:443/organization/repo.git/releases/tag/0.0.1""";

  @Test
  void testReadFileAsString_WhenFileIsRead_ThenContentIsEqualToChanglogString() throws IOException {

    // Given
    var filePath = "src/test/resources/CHANGELOG.md";

    // When
    var actualChangelog = FilesUtility.readFileAsString(filePath);

    // Then
    assertThat(actualChangelog).isEqualTo(CHANGELOG);
  }

  @Test
  void testWriteStringAsFile_WhenStringAndPathToOutputIsGiven_ThenStringWillBeWrittenToFile() throws IOException {

    // Given
    var temporaryFolder = Files.createTempDirectory("target");
    var targetPath = temporaryFolder.resolve("CHANGELOG.md");
    var filePath = targetPath.toFile().getAbsolutePath();

    // When
    var actualPath = FilesUtility.writeStringAsFile(filePath, CHANGELOG);

    // Then
    AssertionsForInterfaceTypes.assertThat(actualPath).exists()
      .binaryContent().isEqualTo(CHANGELOG.getBytes());
  }
}
