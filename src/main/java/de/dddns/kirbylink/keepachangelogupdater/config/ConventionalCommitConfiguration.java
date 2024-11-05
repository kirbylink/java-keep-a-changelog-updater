package de.dddns.kirbylink.keepachangelogupdater.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ConventionalCommitConfiguration {

  private static final String DEFAULT_CONFIG = "/properties.yaml";

  private final Map<String, List<String>> config;

  public ConventionalCommitConfiguration(String configPath) throws IOException {
    var mapper = new ObjectMapper(new YAMLFactory());
    var inputStream = configPath != null ? new FileInputStream(configPath) : getClass().getResourceAsStream(DEFAULT_CONFIG);
    this.config = mapper.readValue(inputStream, new TypeReference<Map<String, List<String>>>() {});
  }

  public List<String> getMajorTypes() {
    return config.getOrDefault("majorTypes", Collections.emptyList());
  }

  public List<String> getMinorTypes() {
    return config.getOrDefault("minorTypes", Collections.emptyList());
  }

  public List<String> getPatchTypes() {
    return config.getOrDefault("patchTypes", Collections.emptyList());
  }

  public List<String> getAddedTypes() {
    return config.getOrDefault("addedTypes", Collections.emptyList());
  }

  public List<String> getFixedTypes() {
    return config.getOrDefault("fixedTypes", Collections.emptyList());
  }

  public List<String> getChangedTypes() {
    return config.getOrDefault("changedTypes", Collections.emptyList());
  }

  public List<String> getRemovedTypes() {
    return config.getOrDefault("removedTypes", Collections.emptyList());
  }
  
  public List<String> getSecurityTypes() {
    return config.getOrDefault("securityTypes", Collections.emptyList());
  }
  
  public List<String> getDeprecatedTypes() {
    return config.getOrDefault("deprecatedTypes", Collections.emptyList());
  }
}
