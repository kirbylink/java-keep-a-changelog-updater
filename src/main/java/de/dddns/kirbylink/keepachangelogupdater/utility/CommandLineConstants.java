package de.dddns.kirbylink.keepachangelogupdater.utility;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandLineConstants {
  public static final String OPTION_SCENARIO_DESCRIPTION = "Scenario to execute: create, add-entry, release, auto-generate";
  public static final String OPTION_SCENARIO_LONG = "scenario";
  public static final String OPTION_SCENARIO_SHORT = "s";
  public static final String OPTION_HELP_SHORT = "h";
  public static final String OPTION_HELP_LONG = "help";
  public static final String OPTION_HELP_DESCRIPTION = "Print this help message";
  public static final String OPTION_INPUT_SHORT = "i";
  public static final String OPTION_INPUT_LONG = "input";
  public static final String OPTION_INPUT_DESCRIPTION = "Path to the existing CHANGELOG.md file";
  public static final String OPTION_OUTPUT_SHORT = "o";
  public static final String OPTION_OUTPUT_LONG = "output";
  public static final String OPTION_OUTPUT_DESCRIPTION_CREATE = "Path to the output file";
  public static final String OPTION_OUTPUT_DESCRIPTION_ADD_ENTRY_AND_RELEASE = "Path to the output file (default: input path)";
  public static final String OPTION_CONSOLE_SHORT = "c";
  public static final String OPTION_CONSOLE_LONG = "console";
  public static final String OPTION_CONSOLE_DESCRIPTION_CREATE = "Output result to console instead of a file";
  public static final String OPTION_REPOSITORY_SHORT = "r";
  public static final String OPTION_REPOSITORY_LONG = "repository";
  public static final String OPTION_REPOSITORY_DESCRIPTION = "Repository URL for link generation";
  public static final String OPTION_MAIN_BRANCH_SHORT = "b";
  public static final String OPTION_MAIN_BRANCH_LONG = "branch";
  public static final String OPTION_MAIN_BRANCH_DESCRIPTION = "Main branch for link generation";
  public static final String OPTION_DESCRIPTION_SHORT = "d";
  public static final String OPTION_DESCRIPTION_LONG = "description";
  public static final String OPTION_DESCRIPTION_DESCRIPTION = "Description for a new entry";
  public static final String OPTION_CATEGORY_SHORT = "t";
  public static final String OPTION_CATEGORY_LONG = "category";
  public static final String OPTION_CATEGORY_DESCRIPTION = "Category for the new entry (Added, Changed, Fixed, Removed, Security, Deprecated)";
  public static final String OPTION_VERSION_SHORT = "v";
  public static final String OPTION_VERSION_LONG = "version";
  public static final String OPTION_VERSION_DESCRIPTION = "Existing release version (default: Unreleased)";
  public static final String OPTION_RELEASE_TYPE_LONG = "release-type";
  public static final String OPTION_RELEASE_TYPE_SHORT = "rt";
  public static final String OPTION_RELEASE_TYPE_DESCRIPTION = "Release type: major, minor, patch";

  public static final String OPTION_SCENARIO_AUTO_GENERATE = "auto-generate";
  public static final String OPTION_GIT_LOG_PATH_SHORT = "g";
  public static final String OPTION_GIT_LOG_PATH_LONG = "git-log-path";
  public static final String OPTION_GIT_LOG_PATH_DESCRIPTION = "Path to the git log file";
  public static final String OPTION_CUTOM_PROPERTIES_PATH_SHORT = "p";
  public static final String OPTION_CUTOM_PROPERTIES_PATH_LONG = "properties-path";
  public static final String OPTION_CUTOM_PROPERTIES_PATH_DESCRIPTION = "Path to the custom properties file";

  public static final String OPTION_AUTO_RELEASE_SHORT = "a";
  public static final String OPTION_AUTO_RELEASE_LONG = "auto-release";
  public static final String OPTION_AUTO_RELEASE_DESCRIPTION = "Create automatically a Release after log analysis. Ignores 'version' if set.";

}
