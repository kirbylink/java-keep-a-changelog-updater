package de.dddns.kirbylink.keepachangelogupdater.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import com.vladsch.flexmark.ast.BulletList;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.Reference;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Entity;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.EntityFooter;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.EntityHeader;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.Version;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.VersionEntry;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryAdded;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryChanged;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryFixed;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryRemoved;
import de.dddns.kirbylink.keepachangelogupdater.model.changelog.category.CategoryType;
import de.dddns.kirbylink.keepachangelogupdater.model.conventionalcommits.HeadingResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChangelogConverter {

  private final String baseRepositoryUrl;
  private final String mainBranch;

  public String convertToString(Entity entity) {
    if (entity.getVersions().size() != entity.getFooter().getUrls().size()) {
      entity.getFooter().getUrls().clear();
      entity.getFooter().getUrls().addAll(convertToEntityFooterUrls(entity.getVersions()));
    }
    return entity.toString();
  }

  public Entity createDefaultEntity() {
    return convertToEntity("");
  }

  public Entity convertToEntity(String changelog) {
    var options = new MutableDataSet();
    var parser = Parser.builder(options).build();
    var document = parser.parse(changelog);

    var headerBuilder = EntityHeader.builder();
    String title = null;
    var descriptionBuilder = new StringBuilder();
    var breakingChangeBuilder = new StringBuilder();

    Version.VersionBuilder currentVersionBuilder = null;
    List<Version> versions = new ArrayList<>();
    String currentType = null;

    var footerLinks = new ArrayList<String>();

    for (var node : document.getChildren()) {
      if (node instanceof Heading heading) {
        var result = handleHeading(heading, currentVersionBuilder, versions, breakingChangeBuilder, currentType);
        title = Optional.ofNullable(result.getTitle()).orElse(title);
        currentVersionBuilder = result.getCurrentVersionBuilder();
        breakingChangeBuilder = result.getBreakingChangeBuilder();
        currentType = result.getCurrentType();
      } else if (node instanceof Paragraph paragraph) {
        handleParagraph(paragraph, descriptionBuilder, breakingChangeBuilder);
      } else if (node instanceof BulletList) {
        convertToEntry(currentVersionBuilder, currentType, node);
      } else if (node instanceof Reference reference) {
        footerLinks.add("[" + reference.getReference() + "]: " + reference.getUrl());
      }
    }

    return buildEntity(title, descriptionBuilder, versions, breakingChangeBuilder, footerLinks, headerBuilder, currentVersionBuilder);
  }

  private HeadingResult handleHeading(Heading heading, Version.VersionBuilder currentVersionBuilder, List<Version> versions, StringBuilder breakingChangeBuilder, String currentType) {
    String title = null;
    String newCurrentType = null;
    switch (heading.getLevel()) {
      case 1:
        title = heading.getText().toString();
        break;
      case 2:
        currentVersionBuilder = convertToVersionAndDate(currentVersionBuilder, versions, heading, breakingChangeBuilder);
        breakingChangeBuilder.setLength(0);
        break;
      case 3:
        newCurrentType = heading.getText().toString();
        break;
      default:
        break;
    }
    return HeadingResult.builder()
        .title(title)
        .currentVersionBuilder(currentVersionBuilder)
        .breakingChangeBuilder(breakingChangeBuilder)
        .currentType(newCurrentType != null  ? newCurrentType : currentType)
        .build();
  }

  private void handleParagraph(Paragraph paragraph, StringBuilder descriptionBuilder, StringBuilder breakingChangeBuilder) {
    var paragraphText = getNodeText(paragraph).trim();
    if (paragraphText.startsWith("BREAKING CHANGE:")) {
      breakingChangeBuilder.append(paragraphText.substring("BREAKING CHANGE: ".length()).trim());
    } else if (!breakingChangeBuilder.isEmpty()) {
      breakingChangeBuilder.append("\n\n").append(paragraphText);
    } else {
      descriptionBuilder.append(paragraphText).append("\n\n");
    }
  }

  private Entity buildEntity(String title, StringBuilder descriptionBuilder, List<Version> versions, StringBuilder breakingChangeBuilder, List<String> footerLinks,
      EntityHeader.EntityHeaderBuilder headerBuilder, Version.VersionBuilder currentVersionBuilder) {
    if (title != null) {
      headerBuilder.title(title).description(descriptionBuilder.toString().trim());
    }

    if (currentVersionBuilder != null) {
      if (!breakingChangeBuilder.isEmpty()) {
        currentVersionBuilder.breakingChange(breakingChangeBuilder.toString().trim());
      }
      versions.add(currentVersionBuilder.build());
    }

    if (versions.isEmpty()) {
      versions.add(Version.builder().releaseVersion("Unreleased").build());
    }

    var footer = EntityFooter.builder().urls(footerLinks).build();
    return Entity.builder().header(headerBuilder.build()).versions(versions).footer(footer).build();
  }

  private List<String> convertToEntityFooterUrls(List<Version> versions) {
    List<String> urls = new ArrayList<>();

    urls.add(String.format("[%s]: %s/compare/%s...HEAD", versions.get(0).getReleaseVersion().toLowerCase(), baseRepositoryUrl, mainBranch));

    IntStream.range(1, versions.size() - 1).forEach(integer -> generateDiffUrl(versions, urls, integer));

    var lastVersion = versions.get(versions.size() - 1).getReleaseVersion();
    if (!lastVersion.equals("Unreleased")) {
      urls.add(String.format("[%s]: %s/releases/tag/%s", lastVersion, baseRepositoryUrl, lastVersion));
    }

    return urls;
  }

  private void generateDiffUrl(List<Version> versions, List<String> urls, int integer) {
    var previousVersion = versions.get(integer + 1).getReleaseVersion();
    var newVersion = versions.get(integer).getReleaseVersion();
    urls.add(String.format("[%s]: %s/compare/%s...%s", newVersion, baseRepositoryUrl, previousVersion, newVersion));
  }

  private Version.VersionBuilder convertToVersionAndDate(Version.VersionBuilder currentVersionBuilder, List<Version> versions, Heading heading, StringBuilder breakingChange) {
    if (currentVersionBuilder != null) {
      versions.add(currentVersionBuilder.breakingChange(breakingChange.toString()).build());
    }
    return Version.builder().releaseVersion(heading.getText().toString().split(" - ", 2)[0].replace("[", "").replace("]", ""))
        .date(heading.getText().toString().split(" - ", 2).length > 1 ? heading.getText().toString().split(" - ", 2)[1] : "");
  }

  private void convertToEntry(Version.VersionBuilder currentVersionBuilder, String currentType, Node node) {
    if (currentVersionBuilder == null) {
      currentVersionBuilder = Version.builder();
    }

    switch (CategoryType.fromValue(currentType)) {
      case ADDED -> currentVersionBuilder.added(CategoryAdded.builder().entries(getVersionEntries(node)).build());
      case CHANGED -> currentVersionBuilder.changed(CategoryChanged.builder().entries(getVersionEntries(node)).build());
      case FIXED -> currentVersionBuilder.fixed(CategoryFixed.builder().entries(getVersionEntries(node)).build());
      case REMOVED -> currentVersionBuilder.removed(CategoryRemoved.builder().entries(getVersionEntries(node)).build());
    }
  }

  private List<VersionEntry> getVersionEntries(Node node) {
    var entries = new ArrayList<VersionEntry>();
    for (Node item : node.getChildren()) {
      entries.add(VersionEntry.builder().description(getNodeTextWithoutNewLine(item)).build());
    }
    return entries;
  }

  private String getNodeText(Node node) {
    var stringBuilder = new StringBuilder();
    for (var child : node.getChildren()) {
      stringBuilder.append(child.getChars());
    }
    return stringBuilder.toString();
  }

  private String getNodeTextWithoutNewLine(Node node) {
    return getNodeText(node).replace("\n", "").replace("\r", "").trim();
  }
}
