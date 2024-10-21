package de.dddns.kirbylink.keepachangelogupdater.model.changelog;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class EntityFooter {
  @Default
  private final List<String> urls = new ArrayList<>();

  @Override
  public String toString() {
    var stringBuilder = new StringBuilder();
    urls.stream().forEach(url -> stringBuilder.append(url).append("\n"));
    return stringBuilder.toString();
  }
}
