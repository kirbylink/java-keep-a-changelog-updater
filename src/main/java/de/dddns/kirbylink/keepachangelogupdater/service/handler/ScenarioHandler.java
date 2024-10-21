package de.dddns.kirbylink.keepachangelogupdater.service.handler;

import java.io.IOException;
import org.apache.commons.cli.CommandLine;

public interface ScenarioHandler {
  void handle(CommandLine commandLine) throws IOException;
}
