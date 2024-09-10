package de.dddns.kirbylink.keepachangelogupdater.model.category;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CategoryRemoved extends Category {
  @Default
  private CategoryType type = CategoryType.REMOVED;
}