# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [v2.0.4] - 2025-02-01
### Changed
- Update Maven dependencies

## [v2.0.3] - 2025-01-01
### Changed
- Update Maven dependencies

## [v2.0.2] - 2024-12-01
### Changed
- Update Maven dependencies

## [v2.0.1] - 2024-11-29
### Fixed
- Add null check if breaking changes should be printed

## [v2.0.0] - 2024-11-06
### Added
- Support `Security` and `Deprecated` for conventional commits

### Fixed
- GitLogParser.splitGitLog() method and type detection fixed

BREAKING CHANGE: Conventional commits starting with `fix(security)` now
appear in the `Security` category instead of `Fixed`.

## [v1.2.0] - 2024-11-04
### Added
- Add Deprecated as additional Category
- Add Security as additional Category

### Fixed
- An exception occurred during auto release without changes

## [v1.1.1] - 2024-11-01
### Changed
- Update Maven dependencies

## [v1.1.0] - 2024-10-21
### Added
- Auto-generated entries when using Conventional Commits

### Changed
- Update Maven dependencies

## [v1.0.1] - 2024-10-01
### Changed
- Update Maven dependencies

## [v1.0.0] - 2024-09-10
### Added
- Initial release of the Java Keep-A-Changelog Updater.
- Automatic updating of CHANGELOG.md based on commit messages.
- Command-line interface (CLI) for easy integration into CI/CD pipelines.
- Ability to filter commits by type (e.g., feature, bugfix) and scope.
- Detailed usage documentation and example configurations.
- Support for semantic versioning.

[unreleased]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/main...HEAD
[v2.0.4]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/v2.0.3...v2.0.4
[v2.0.3]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/v2.0.2...v2.0.3
[v2.0.2]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/v2.0.1...v2.0.2
[v2.0.1]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/v2.0.0...v2.0.1
[v2.0.0]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/v1.2.0...v2.0.0
[v1.2.0]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/v1.1.1...v1.2.0
[v1.1.1]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/v1.1.0...v1.1.1
[v1.1.0]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/v1.0.1...v1.1.0
[v1.0.1]: https://github.com/kirbylink/java-keep-a-changelog-updater/compare/v1.0.0...v1.0.1
[v1.0.0]: https://github.com/kirbylink/java-keep-a-changelog-updater/releases/tag/v1.0.0
