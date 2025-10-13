# Changelog

## [Unreleased]

## [0.1.0] - 2025-10-13
### Added

- Delegate parsing to the JetBrains JavaScript plugin. (#50)
- Comment block (`{{# }}`) support. (#47)
- Delegate parsing of the main body of a `.vto` file to the JetBrains HTML plugin. (#51)
- Variable block with (`{{ }}`) and wihtout trimming (`{{- -}}`) support. (#36) (#38)
- Variable pipe (`{{ expression |> call }}`) support (#39)
- For-block (`{{ for [value] of [collection] }}`) support (#43) (#77)
- import (`{{ import { foo, bar } from "baz"}}`) support (#49) (#92)
- export (`{{ export greeting = "Hello World!" }}`) support (#49) (#92)
- set (`{{ set greeting = "Hello World!" }}`) support (#49) (#92)

### Fixed

- Fixed the plugin project (#25)
- Fixed the GitHub workflow security issue (#55)
- Fixed the release process (#60)
