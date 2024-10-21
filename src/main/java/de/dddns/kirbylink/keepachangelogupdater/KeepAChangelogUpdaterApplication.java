package de.dddns.kirbylink.keepachangelogupdater;

import java.net.URISyntaxException;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import de.dddns.kirbylink.keepachangelogupdater.service.CommandLineService;
import de.dddns.kirbylink.keepachangelogupdater.service.ScenarioHandlerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KeepAChangelogUpdaterApplication {

  public static void main(String[] args) throws URISyntaxException {
    var commandLineService = new CommandLineService();
    var optionalCommandLine = Optional.ofNullable(commandLineService.getCommandLine(args));

    optionalCommandLine.ifPresent(commandLine -> handleScenario(commandLineService, commandLine));
  }

  private static void handleScenario(CommandLineService commandLineService, CommandLine commandLine) {
    try {
      var scenario = commandLineService.getScenario();
      var scenarioHandlerService = new ScenarioHandlerService();

      var scenarioHandler = scenarioHandlerService.getScenarioHandler(scenario);
      scenarioHandler.handle(commandLine);
    } catch (Exception e) {
      log.error("Error occurred during execution: {}", e.getMessage());
      log.debug("Stacktrace:", e);
    }
  }
}
