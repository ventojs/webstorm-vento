<h1 align="center"> <a href="https://vento.js.org/"> <img src="assets/favicon.svg" alt="VentoJS Logo" width="200"></a> </h1>
<div align="center">

[![Contributors][contributors_shield_url]][contributors_url]
[![Issues][issues_shield_url]][issues_url]

</div>

<!-- Plugin description -->
<h2> Vento plugin</h2>
<h3>for JetBrain's WebStorm & IntelliJ IDEA Ultimate IDE </h3>
<p>
Provides support for the <a href="https://vento.js.org/">VentoJs</a> template engine. Vento is used in the SSG  <a href="https://lume.land/">Lume</a> framework to generate HTML sites.
</p>
<h3>key features:</h3>
<ol>
    <li>`*.vto` file type</li>
    <li>Syntax highlighting</li>
    <li>Syntax checking</li>
    <li>auto-completion</li>
    <li>support for front-matter</li>
    <li>support for the fragment plugin</li>
    <li>formatting</li>
    <li>filepath auto-completion and validity checking</li>
    <li>comment toggling</li>
</ol>
For more information visit <a href="https://vento.js.org/">Vento</a>.

<p>It's still early days with this plugin. auto-complete and formatting support and a few other features are still to come. Check the <a href="https://github.com/ventojs/webstorm-vento/issues">issues</a> for details</p>

<!-- Plugin description end -->

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

### Features

#### Implemented
* syntax highlighting
* syntax checking
* auto-completion (experimental and needs improvement)
* front-matter support (disableable in settings if not wanted)
* fragments support
* formatting (basic indenting support)
* filepath auto-completion and validity verification for layout, include, and import blocks
* comment toggling vento blocks. Use ` ⌥`+`⇧`+`⌘`+`/` on OSX, or `⌥`+`⇧`+`ctrl`+`/` on Windows.

[//]: # (#### Planned)


> [!IMPORTANT]
> ### Note to plugin developers:
>
> * You can use IntelliJ Community or Ultimate edition for development.
> * Installing the built plugin in IntelliJ Ultimate edition or Webstorm.
> * The plugin is tested with Webstorm 2024 and 2025 and IntelliJ IDEA Ultimate 2024 and 2025.

## Installation

* Using JetBrains Marketplace:
    * In the IDE go to <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>
      Marketplace</kbd> > <kbd>Search for "WebStorm-Vento"</kbd> > <kbd>Install</kbd>

* Manually:
    * Download from GitHub Releases, or from the [JetBrains Marketplace](https://plugins.jetbrains.com/), or build it yourself.
    * In your IDE go to <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from
      disk...</kbd>

## Usage

### Design

The plugin is based on the [JetBrains Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template). The [Jetbrains Plugin SDK](https://plugins.jetbrains.com/docs/intellij/developing-plugins.html) is a valuable place to understand how to build a plugin for IntelliJ IDEA.

Thanks to the opensource [Handlebars](https://github.com/JetBrains/intellij-plugins/tree/master/handlebars) plugin, I was able to learn and resolve some issues with the syntax highlighting. To be honest, I shamelessly coppied some code from the plugin before I understood how it worked!

The best entry point into the implementation is the [plugin.xml](src/main/resources/META-INF/plugin.xml).

### Lexer

The JFlex implementation of the lexer is located in the `src/main/jflex` directory. The Lexer gets generated at build time in `src/main/gen`. It is based on [JFlex](https://jflex.de/). Be careful to understand how Jetbrains uses the lexer that is generated. There are some significant differences. See [Lexer Implementation](https://plugins.jetbrains.com/docs/intellij/implementing-lexer.html#lexer-implementation) in SDK docs.

Vento lexing [design diagrams](statemachine-v2.md)

### Parsing

Conceptually, parsing is split into two parts. The base of a vento file is assumed to be an HTML document. All processing is handed over to the built-in Jetbrains HTML support. The Vento plugin takes over when the vento specific syntax is encountered.


#### Dependencies

Most of the following dependencies are provided automatically when Gradle is used to build the plugin.

* Vento <code> >= v2</code>
* Deno <code> >= v2.3</code>
* IntelliJ IDEA Community or Ultimate (for now only tested with <code>2025.2.*</code>)
* JDK <code>v21</code>
* Gradle <code>v9</code>
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
> JetBrains SDK in `build.gradle.kts`.

```bash
./gradlew --stop          ## stop the gradle daemon
rm -rf ~/.gradle/caches/  ## delete all caches
```
## AI Disclosure

Development of this project incorporated assistance from AI tools including OpenAI, and JetBrains AI tooling. 

These tools are used for:
* Prototyping and generating initial drafts of code
* Refactoring and optimization suggestions
* Writing or improving documentation
* Exploring design patterns and solving technical issues

Humans perform all final decisions, implementations, and code validations.

## Built With

<div align="center">

[![Built With][built_with_kotlin]][kotlin]
[![Built With][built_with_gradle]][gradle]
[![Built With][built_with_github]][github]
[![Built With][built_with_idea]][idea]
[![Built With][built_with_webstorm]][webstorm]
</div>

<p align="right"><a href="#readme-top">▲</a></p>

[built_with_kotlin]: https://skillicons.dev/icons?i=kotlin
[kotlin]: https://kotlinlang.org/

[built_with_gradle]: https://skillicons.dev/icons?i=gradle
[gradle]: https://gradle.org/

[built_with_github]: https://skillicons.dev/icons?i=github
[github]: https://github.com/

[built_with_idea]: https://skillicons.dev/icons?i=idea
[idea]: https://www.jetbrains.com/idea/

[built_with_webstorm]: https://skillicons.dev/icons?i=webstorm
[webstorm]: https://www.jetbrains.com/webstorm/


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
