package de.dddns.kirbylink.keepachangelogupdater.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.vladsch.flexmark.ast.BulletList;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.Reference;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import de.dddns.kirbylink.keepachangelogupdater.model.Entity;
import de.dddns.kirbylink.keepachangelogupdater.model.EntityFooter;
import de.dddns.kirbylink.keepachangelogupdater.model.EntityHeader;
import de.dddns.kirbylink.keepachangelogupdater.model.Version;
import de.dddns.kirbylink.keepachangelogupdater.model.VersionEntry;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryAdded;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryChanged;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryFixed;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryRemoved;
import de.dddns.kirbylink.keepachangelogupdater.model.category.CategoryType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChangelogConverter {

  private final String baseRepositoryUrl;
  private final String mainBranch;

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

    Version.VersionBuilder currentVersionBuilder = null;
    List<Version> versions = new ArrayList<>();
    String currentType = null;

    EntityFooter footer;
    var footerLinks = new ArrayList<String>();

    for (var node : document.getChildren()) {
      if (node instanceof Heading heading) {
        switch (heading.getLevel()) {
          case 1:
            title = heading.getText().toString();
            break;
          case 2:
            currentVersionBuilder = convertToVersionAndDate(currentVersionBuilder, versions, heading);
            break;
          case 3:
            currentType = heading.getText().toString();
            break;
          default:
            break;
        }
      } else if (node instanceof Paragraph paragraph) {
        descriptionBuilder.append(getNodeText(paragraph)).append("\n\n");
      } else if (node instanceof BulletList) {
        convertToEntry(currentVersionBuilder, currentType, node);
      } else if (node instanceof Reference reference) {
        footerLinks.add("[" + reference.getReference() + "]: " + reference.getUrl());
      }
    }

    if (title != null) {
      headerBuilder.title(title).description(descriptionBuilder.toString().trim());
    }

    if (currentVersionBuilder != null) {
      versions.add(currentVersionBuilder.build());
    }

    if (versions.isEmpty()) {
      versions.add(Version.builder().releaseVersion("Unreleased").build());
    }

    footer = EntityFooter.builder().urls(footerLinks).build();
    return Entity.builder().header(headerBuilder.build()).versions(versions).footer(footer).build();
  }

  public String convertToString(Entity entity) {
    if (entity.getVersions().size() != entity.getFooter().getUrls().size()) {
      entity.getFooter().getUrls().clear();
      entity.getFooter().getUrls().addAll(convertToEntityFooterUrls(entity.getVersions()));
    }
    return entity.toString();
  }

  public List<String> convertToEntityFooterUrls(List<Version> versions) {
    List<String> urls = new ArrayList<>();

    if (versions.isEmpty()) {
      return urls;
    }

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

  private Version.VersionBuilder convertToVersionAndDate(Version.VersionBuilder currentVersionBuilder, List<Version> versions, Heading heading) {
    if (currentVersionBuilder != null) {
      versions.add(currentVersionBuilder.build());
    }
    return Version.builder()
        .releaseVersion(heading.getText().toString().split(" - ", 2)[0].replace("[", "").replace("]", ""))
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
