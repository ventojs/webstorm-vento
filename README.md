<div id="readme-top"></div>

<!-- Plugin description -->
<h1 align="center">
  <br />
    <a href="https://vento.js.org/">
      <img src="https://vento.js.org/favicon.svg" alt="VentoJS Logo" width="200">
    </a>
  <br /><br />
  Vento for JetBrain's WebStorm & IntelliJ IDEA Ultimate IDE
  <br /><br />
</h1>
<div align="center">
Provides support for the <a href="https://vento.js.org/">VentoJs</a> template engine.
The support includes:

<h4>syntax highlighting</h4>
<h4>syntax checking</h4>

For more information visit the
<a href="https://vento.js.org/">Vento</a>.
</div>
<!-- Plugin description end -->
<div align="center">

[![Contributors][contributors_shield_url]][contributors_url]
[![Issues][issues_shield_url]][issues_url]
</div>

## Table of Contents

- [About The Project](#about-the-project)
- [Installation](#installation)
- [Usage](#usage)
- [Built With](#built-with)
- [Code of Conduct][code_of_conduct_url]
- [Contributing][contributing_url]
- [License][license_url]

## About The Project

`webstorm-vento` is the plugin which integrates the [Vento Template Engine](https://vento.js.org/) with JetBrain's
IntelliJ Ultimate and WebStorm IDEs.

> [!IMPORTANT]
> ### Note to plugin users
>
> * This plugin is in the early stages of development.
> * The plugin is not yet available on the JetBrains Marketplace, but you can use the provided GitHub releases or build
    it yourself. (see: [Installation](#installation) below)
> * Using the plugin depends on the presence of the Jetbrains JavaScript & TypeScript plug-in in your IDE. It is
    available by default in Webstorm (including with the free none-commercial license) but **not** in the community
    edition of IntelliJ IDEA.

> [!IMPORTANT]
> ### Note to plugin developers:
>
> * You can use IntelliJ Community or Ultimate edition for development.
> * Installing the built plugin in Webstorm works.
> * Targeting Webstorm at build and testing time is not yet working.

## Installation

* Using JetBrains Marketplace (Not yet available):
    * In the IDE go to <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>
      Marketplace</kbd> > <kbd>Search for "WebStorm-Vento"</kbd> > <kbd>Install</kbd>
    * On the Go to [JetBrains Marketplace](https://plugins.jetbrains.com/) search for "WebStorm-Vento", then download
      and
      install it.

> [!WARNING]
> **NOT YET RELEASED TO MARKETPLACE**

* Installing from a download:
    * Download from GitHub Releases, or from the Marketplace, or build it yourself.
    * In your IDE go to <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from
      disk...</kbd>

## Usage

#### Dependencies

Most of the following dependencies are provided automatically when Gradle is used to build the plugin.

* Vento <code> >= v2</code>
* Deno <code> >= v2.3</code>
* IntelliJ IDEA Community or Ultimate (for now only tested with <code>2025.2.*</code>)
* JDK <code>v21</code>
* Gradle <code>v8.12</code>
* Jetbrains JavaScript & TypeScript plug-in <code>v251.27812.49</code>

#### Recommended

* install [ktlint](https://pinterest.github.io/ktlint/latest/install/setup/) and the prePushHook to make sure your code
  is formatted correctly.

#### Build

* Clone the repository
* Run `./gradlew buildPlugin`
* The plugin will be built in `build/distributions`
* Install the plugin archive found in the `build/distributions` directory

#### run tests

```bash
./gradlew test
```

##### verify Jetbrains platform compatibility

```bash
./gradlew verifyPlugin
```

##### build without caches

```bash
./gradlew clean buildPLugin --no-build-cache --no-configuration-cache
```

##### clearing caches

> [!TIP]
> The Jetbrains SDK depends on a lot of magical dependencies to be able to run its own IDE's in development mode. So
> sometimes there is no choice but to use the nuclear option to get a clean slate. This is likely to happen if you start
> switching the platform being targeted by the plugin in `gradle.properties` or if you change the version of the
> Jetbrains
> SDK in `build.gradle.kts`.

```bash
./gradlew --stop          ## stop the gradle daemon
rm -rf ~/.gradle/caches/  ## delete all caches
```

## Built With

<div align="center">

[![Built With][built_with_shield_url]][built_with_url]
</div>

<p align="right"><a href="#readme-top">▲</a></p>

[built_with_shield_url]: https://skillicons.dev/icons?i=kotlin,gradle,github,githubactions

[built_with_url]: https://skillicons.dev

[code_of_conduct_url]: https://github.com/ventojs/webstorm-vento?tab=coc-ov-file

[contributing_url]: https://github.com/ventojs/webstorm-vento/blob/main/CONTRIBUTING.md

[contributors_shield_url]: https://img.shields.io/github/contributors/ventojs/webstorm-vento?style=for-the-badge&color=blue

[contributors_url]: https://github.com/ventojs/webstorm-vento/graphs/contributors

[deps_shield_url]: https://deps.rs/repo/github/ventojs/webstorm-vento/status.svg?style=for-the-badge

[deps_url]: https://deps.rs/repo/github/ventojs/webstorm-vento

[issues_shield_url]: https://img.shields.io/github/issues/ventojs/webstorm-vento?style=for-the-badge&color=yellow

[issues_url]: https://github.com/ventojs/webstorm-vento/issues

[license_url]: https://github.com/ventojs/webstorm-vento?tab=AGPL-3.0-1-ov-file

[roadmap_shield_url]: https://img.shields.io/badge/Roadmap-Click%20Me!-purple.svg?style=for-the-badge

[roadmap_url]: https://github.com/orgs/ventojs/projects/4

[template]: https://github.com/JetBrains/intellij-platform-plugin-template

[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
