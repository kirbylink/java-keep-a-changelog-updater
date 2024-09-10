package de.dddns.kirbylink.keepachangelogupdater.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FilesUtility {

  public static String readFileAsString(String filePath) throws IOException {
    var path = Paths.get(filePath);
    return new String(Files.readAllBytes(path));
  }

  public static Path writeStringAsFile(String filePath, String content) throws IOException {
    var path = Paths.get(filePath);
    return Files.writeString(path, content);
  }

}
