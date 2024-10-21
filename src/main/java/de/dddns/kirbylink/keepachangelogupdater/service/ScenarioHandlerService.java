package de.dddns.kirbylink.keepachangelogupdater.service;

import java.util.EnumMap;
import java.util.Map;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Scenario;
import de.dddns.kirbylink.keepachangelogupdater.service.handler.AddEntryScenarioHandler;
import de.dddns.kirbylink.keepachangelogupdater.service.handler.AutoGenerateScenarioHandler;
import de.dddns.kirbylink.keepachangelogupdater.service.handler.CreateScenarioHandler;
import de.dddns.kirbylink.keepachangelogupdater.service.handler.ReleaseScenarioHandler;
import de.dddns.kirbylink.keepachangelogupdater.service.handler.ScenarioHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScenarioHandlerService {

  private final Map<Scenario, ScenarioHandler> scenarioHandlers = initializeScenarioHandlers();

  public ScenarioHandler getScenarioHandler(Scenario scenario) {
    return scenarioHandlers.get(scenario);
  }

  private Map<Scenario, ScenarioHandler> initializeScenarioHandlers() {
    var updateService = new UpdateService();
    Map<Scenario, ScenarioHandler> handlers = new EnumMap<>(Scenario.class);
    handlers.put(Scenario.CREATE, new CreateScenarioHandler(updateService));
    handlers.put(Scenario.ADD_ENTRY, new AddEntryScenarioHandler(updateService));
    handlers.put(Scenario.RELEASE, new ReleaseScenarioHandler(updateService));
    handlers.put(Scenario.AUTO_GENERATE, new AutoGenerateScenarioHandler(updateService));
    return handlers;
  }
}

