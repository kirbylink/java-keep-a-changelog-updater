package de.dddns.kirbylink.keepachangelogupdater.model.changelog.category;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CategoryFixed extends Category {
  @Default
  private CategoryType type = CategoryType.FIXED;
}
