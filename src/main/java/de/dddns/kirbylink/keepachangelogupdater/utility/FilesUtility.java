package de.dddns.kirbylink.keepachangelogupdater.utility;

import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_CONSOLE_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_INPUT_SHORT;
import static de.dddns.kirbylink.keepachangelogupdater.utility.CommandLineConstants.OPTION_OUTPUT_SHORT;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.cli.CommandLine;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class FilesUtility {

  public static String readFile(String path) throws IOException {
    return FilesUtility.readFileAsString(path);
  }

  public static void createOutput(CommandLine commandLine, String changelog) {
    if (commandLine.hasOption(OPTION_CONSOLE_SHORT)) {
      log.info(changelog);
    } else {
      var output = commandLine.getOptionValue(OPTION_OUTPUT_SHORT);
      var filePath = output == null ? commandLine.getOptionValue(OPTION_INPUT_SHORT) : output;
      try {
        writeStringAsFile(filePath, changelog);
        log.info("Changelog saved to {}", filePath);
      } catch (IOException e) {
        log.warn("Couldn't save content to {}", filePath, e);
        log.info(changelog);
      }
    }
  }

  protected static String readFileAsString(String filePath) throws IOException {
    var path = Paths.get(filePath);
    return new String(Files.readAllBytes(path));
  }

  protected static Path writeStringAsFile(String filePath, String content) throws IOException {
    var path = Paths.get(filePath);
    return Files.writeString(path, content);
  }

}
