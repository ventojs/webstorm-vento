# Changelog

## [0.8.0](https://github.com/ventojs/webstorm-vento/compare/v0.7.1...v0.8.0) (2026-09-07)


### Features

* **editing:** add missing completion snippets and break/continue support ([#142](https://github.com/ventojs/webstorm-vento/issues/142)) ([#244](https://github.com/ventojs/webstorm-vento/issues/244)) ([6742f59](https://github.com/ventojs/webstorm-vento/commit/6742f59b0e40fce17aff587367e57ce97481c15c))
* **editing:** auto-insert closing tag when typing an opening block ([#238](https://github.com/ventojs/webstorm-vento/issues/238)) ([#243](https://github.com/ventojs/webstorm-vento/issues/243)) ([563da36](https://github.com/ventojs/webstorm-vento/commit/563da367060efdd3d444b9d8cea5bf0029e10e48))
* **parser:** declare real variable names in the shared JS context ([#245](https://github.com/ventojs/webstorm-vento/issues/245)) ([d2c6b3c](https://github.com/ventojs/webstorm-vento/commit/d2c6b3ca0b4278d43c208ae02231584aed7e0625)), closes [#241](https://github.com/ventojs/webstorm-vento/issues/241)
* **parser:** detect unclosed and mismatched if/for closing tags ([#110](https://github.com/ventojs/webstorm-vento/issues/110)) ([4340b1b](https://github.com/ventojs/webstorm-vento/commit/4340b1bb7794d56cb7e97e6f9d95a4a662295bbc))
* **settings:** add setting to disable html highlighting ([#188](https://github.com/ventojs/webstorm-vento/issues/188)) ([618aec2](https://github.com/ventojs/webstorm-vento/commit/618aec2ceb006043541eb4a76511ede507df2503))


### Bug Fixes

* **editing:** fix small issues found while testing for 0.8.0 release ([#252](https://github.com/ventojs/webstorm-vento/issues/252)) ([8d88613](https://github.com/ventojs/webstorm-vento/commit/8d88613367c4666177c221c19238450a5d1d0e5a))
* **lexer:** stop matching keywords as prefixes of longer identifiers ([#239](https://github.com/ventojs/webstorm-vento/issues/239)) ([#246](https://github.com/ventojs/webstorm-vento/issues/246)) ([14faa58](https://github.com/ventojs/webstorm-vento/commit/14faa58afcc3f125387a0e67edcf5f89eecdf32d))
* **lexer:** support single quotes in include/import/layout paths ([#220](https://github.com/ventojs/webstorm-vento/issues/220)) ([8aa4a23](https://github.com/ventojs/webstorm-vento/commit/8aa4a23e1005f857c62da48506d92d3d650bb012))
* **lexer:** support single-character frontmatter values ([#219](https://github.com/ventojs/webstorm-vento/issues/219)) ([3b3c89c](https://github.com/ventojs/webstorm-vento/commit/3b3c89cf45bd1aa9728b62d56cffeaf3b028287d))
* **parser:** false positive syntax error for parenthesized set expressions ([#224](https://github.com/ventojs/webstorm-vento/issues/224)) ([84b041b](https://github.com/ventojs/webstorm-vento/commit/84b041b094fbc07aedb638f0655eddfae7baee86))
* **parser:** report all unclosed blocks at EOF, not just the innermost ([#110](https://github.com/ventojs/webstorm-vento/issues/110)) ([646bf66](https://github.com/ventojs/webstorm-vento/commit/646bf6621f913f8822fe52ec1cb035b0b0a42b6c))
* **parser:** validate close tags for function/fragment/slot, resync malformed closes ([#242](https://github.com/ventojs/webstorm-vento/issues/242)) ([dbc2cd4](https://github.com/ventojs/webstorm-vento/commit/dbc2cd45f1280b0c59a42505e80fb2ad9b4959ba))
* **psi:** implement updateText() for JavaScriptElement injection host ([#170](https://github.com/ventojs/webstorm-vento/issues/170)) ([a2c49d6](https://github.com/ventojs/webstorm-vento/commit/a2c49d635b828395c031be270782bc38cd1ffd65))
* **psi:** implement updateText() for JavaScriptElement injection host ([#170](https://github.com/ventojs/webstorm-vento/issues/170)) ([728fe17](https://github.com/ventojs/webstorm-vento/commit/728fe17b13e509e4f9b347e4d81fe9d3c123652e))
* **test:** run FrontmatterHighlightToggleTest in its own JVM ([#166](https://github.com/ventojs/webstorm-vento/issues/166)) ([e2447c3](https://github.com/ventojs/webstorm-vento/commit/e2447c368f961a301c2e53bf61dde9786004fe53))

## [0.7.1](https://github.com/ventojs/webstorm-vento/compare/v0.7.0...v0.7.1) (2026-01-17)


### Bug Fixes

* **deps:** update junit-framework monorepo to v6.0.2 ([#193](https://github.com/ventojs/webstorm-vento/issues/193)) ([#194](https://github.com/ventojs/webstorm-vento/issues/194)) ([1074e83](https://github.com/ventojs/webstorm-vento/commit/1074e8320713fec912ef68b867675860153bea64))
* **lexer:** 0.7.0  hanging ([#196](https://github.com/ventojs/webstorm-vento/issues/196)) ([#198](https://github.com/ventojs/webstorm-vento/issues/198)) ([df8031a](https://github.com/ventojs/webstorm-vento/commit/df8031adc14d92c0feabc1d4320dc3bf6745ad8a))

## [0.7.0](https://github.com/ventojs/webstorm-vento/compare/v0.6.1...v0.7.0) (2026-01-14)


### Features

* **parser:** add support for new features in Vento 2.2 and 2.3 ([#189](https://github.com/ventojs/webstorm-vento/issues/189)) ([#191](https://github.com/ventojs/webstorm-vento/issues/191)) ([a407bde](https://github.com/ventojs/webstorm-vento/commit/a407bdeb39db4d136f72e50bf147f5d8afe6a72f))


### Bug Fixes

* **editing:** fix formatting corner cases with `set` and `if` blocks ([#190](https://github.com/ventojs/webstorm-vento/issues/190)) ([71229af](https://github.com/ventojs/webstorm-vento/commit/71229aff5e1213367d22c04109db0d4790a2d977))

## [0.6.1](https://github.com/ventojs/webstorm-vento/compare/v0.6.0...v0.6.1) (2026-01-03)


### Bug Fixes

* **editing:** enhance file reference handling ([#184](https://github.com/ventojs/webstorm-vento/issues/184)) ([#172](https://github.com/ventojs/webstorm-vento/issues/172)) ([9b6f8df](https://github.com/ventojs/webstorm-vento/commit/9b6f8dfe19cca841ada92c473416cb8f39866014))
* **editing:** fix comment toggling for Vento blocks ([#187](https://github.com/ventojs/webstorm-vento/issues/187)) ([#155](https://github.com/ventojs/webstorm-vento/issues/155)) ([d72473c](https://github.com/ventojs/webstorm-vento/commit/d72473c7ce4929d8e0f7e2c28625ea83a65f33f8))
* **editing:** fix indenting of nested content ([#186](https://github.com/ventojs/webstorm-vento/issues/186)) ([#183](https://github.com/ventojs/webstorm-vento/issues/183)) ([c320939](https://github.com/ventojs/webstorm-vento/commit/c32093960302807c822f1ea9ee0628d07161a6ff))

## [0.6.0](https://github.com/ventojs/webstorm-vento/compare/v0.5.0...v0.6.0) (2025-12-31)


### Features

* **editing:** introduce navigation to file for `layout` and `include` elements ([#180](https://github.com/ventojs/webstorm-vento/issues/180)) ([#173](https://github.com/ventojs/webstorm-vento/issues/173)) ([d75b805](https://github.com/ventojs/webstorm-vento/commit/d75b8057e2d6d978b0ff51dd66f3609398083096))


### Bug Fixes

* **editing:** add action to create new Vento file from "New" menu ([#179](https://github.com/ventojs/webstorm-vento/issues/179)) ([#178](https://github.com/ventojs/webstorm-vento/issues/178)) ([6ce5d6d](https://github.com/ventojs/webstorm-vento/commit/6ce5d6daed9874fdc602f003931d91ff95e424e4))
* **editing:** add refactoring support for Vento files ([#182](https://github.com/ventojs/webstorm-vento/issues/182)) ([721a311](https://github.com/ventojs/webstorm-vento/commit/721a311def76ff08b56c3af8857ad75ab7d877d3))
* **editing:** fix formatting ([#177](https://github.com/ventojs/webstorm-vento/issues/177)) ([#148](https://github.com/ventojs/webstorm-vento/issues/148)) ([a0a3ae1](https://github.com/ventojs/webstorm-vento/commit/a0a3ae12e25655e8504d60078d12e3b87293f0dc))
* **lexer:** support layout without space formatting ([#175](https://github.com/ventojs/webstorm-vento/issues/175)) ([#171](https://github.com/ventojs/webstorm-vento/issues/171)) ([02028a9](https://github.com/ventojs/webstorm-vento/commit/02028a99229c768a88e639cf3307f7f6f95ad932))

## [0.5.0](https://github.com/ventojs/webstorm-vento/compare/v0.4.0...v0.5.0) (2025-12-13)


### Features

* **parser:** add support for `fragment` keyword ([#52](https://github.com/ventojs/webstorm-vento/issues/52)) ([#165](https://github.com/ventojs/webstorm-vento/issues/165)) ([e6d6bb5](https://github.com/ventojs/webstorm-vento/commit/e6d6bb5194afd907071d60da046e4997f289ed31))
* **parser:** add support for frontmatter parsing and highlighting ([#100](https://github.com/ventojs/webstorm-vento/issues/100)) ([#158](https://github.com/ventojs/webstorm-vento/issues/158)) ([9d50a6f](https://github.com/ventojs/webstorm-vento/commit/9d50a6f336c3d55a58904544c7f581f4521d24c3))
* **settings:** add frontmatter highlighting setting ([#162](https://github.com/ventojs/webstorm-vento/issues/162)) ([e0590b2](https://github.com/ventojs/webstorm-vento/commit/e0590b26ea129c6cc737836a2c2f1e63f97973c7))


### Bug Fixes

* **docs:** update README with front-matter and fragment support details ([#168](https://github.com/ventojs/webstorm-vento/issues/168)) ([fc62390](https://github.com/ventojs/webstorm-vento/commit/fc623907cf8394cec1cee139b14f69118d30c2e8))

## [0.4.0](https://github.com/ventojs/webstorm-vento/compare/v0.3.0...v0.4.0) (2025-12-02)


### Features

* **editing:** add block auto-closing and introduced basic autocompletion ([#154](https://github.com/ventojs/webstorm-vento/issues/154)) ([#156](https://github.com/ventojs/webstorm-vento/issues/156)) ([7511e10](https://github.com/ventojs/webstorm-vento/commit/7511e104bc105a7018c1f88725eb79ab8205a7ab))

## [0.3.0](https://github.com/ventojs/webstorm-vento/compare/v0.2.1...v0.3.0) (2025-11-28)


### Features

* **parser:** improve HTML language support for Vento templates ([#76](https://github.com/ventojs/webstorm-vento/issues/76)) ([#141](https://github.com/ventojs/webstorm-vento/issues/141)) ([959ba34](https://github.com/ventojs/webstorm-vento/commit/959ba34cac3ab294ff1ebc6ee26c87309a9c550f))


### Bug Fixes

* **cicd:** correct env variable name in release workflow ([69edf4d](https://github.com/ventojs/webstorm-vento/commit/69edf4d9c574b46848b3eccdd59d92ff6fdd980a))

## [0.2.1](https://github.com/ventojs/webstorm-vento/compare/v0.2.0...v0.2.1) (2025-11-16)


### Bug Fixes

* **cicd:** update token names in release workflow and Gradle build configuration ([d5e1cc3](https://github.com/ventojs/webstorm-vento/commit/d5e1cc3e9f0fd8730ee11c21051774328544e6be))

## [0.2.0](https://github.com/ventojs/webstorm-vento/compare/v0.1.0...v0.2.0) (2025-11-16)


### Features

* **clean_up:** add `.editorconfig` and `ktlint` configuration ([83f4ebf](https://github.com/ventojs/webstorm-vento/commit/83f4ebf26474c445f737e43abe06484e560a6558))
* **clean_up:** adjust lexer to handle whitespace and improve tokenization ([fbd8de7](https://github.com/ventojs/webstorm-vento/commit/fbd8de76e8d80d469bc971e2f5af3efdef7ecff6))
* **clean_up:** enhance lexer support for HTML, text, and line handling ([5862f7f](https://github.com/ventojs/webstorm-vento/commit/5862f7f7c2a0a68e25af801fdbba19ab9d9fa8de))
* **comment:** complete comment block support ([#47](https://github.com/ventojs/webstorm-vento/issues/47)) ([2170027](https://github.com/ventojs/webstorm-vento/commit/21700275f44b2d7aee925171b1d3b14635ca1559))
* **echo:** add support for `echo` keyword ([#40](https://github.com/ventojs/webstorm-vento/issues/40)) ([#130](https://github.com/ventojs/webstorm-vento/issues/130)) ([4a5d693](https://github.com/ventojs/webstorm-vento/commit/4a5d69338757b5c0c678414bf63c3c2e688c43f5))
* **function:** add function support ([#48](https://github.com/ventojs/webstorm-vento/issues/48)) ([#131](https://github.com/ventojs/webstorm-vento/issues/131)) ([37812d6](https://github.com/ventojs/webstorm-vento/commit/37812d624ee3d1e5f1cdacfa521a3961c09c43de))
* **html:** introduce HTML language injection and default content block handling ([#65](https://github.com/ventojs/webstorm-vento/issues/65)) ([f456757](https://github.com/ventojs/webstorm-vento/commit/f456757e3e3aec276433dc6f20ef3df484331e23))
* **import-export:** support import and export blocks ([#92](https://github.com/ventojs/webstorm-vento/issues/92)) ([d5378fe](https://github.com/ventojs/webstorm-vento/commit/d5378fe798639a7c5a2816805e8e7209bd37e53c))
* **include:** add support for `include` syntax ([#45](https://github.com/ventojs/webstorm-vento/issues/45)) ([#112](https://github.com/ventojs/webstorm-vento/issues/112)) ([32d96dd](https://github.com/ventojs/webstorm-vento/commit/32d96dd0d87a021c27645ba9c98a350aa61865c2))
* **layout:** add support for `layout` syntax in lexer, parser, and syntax highlighter ([#46](https://github.com/ventojs/webstorm-vento/issues/46)) ([#108](https://github.com/ventojs/webstorm-vento/issues/108)) ([45d6e93](https://github.com/ventojs/webstorm-vento/commit/45d6e932c7c8af2b35c83ae9d54b5ba179c0ec32))
* **lexer:** enhance JavaScript and comment parsing for Vento language ([25cc55e](https://github.com/ventojs/webstorm-vento/commit/25cc55e5377e09130f73d67d17f1991c95036398))
* **parser:** add support for `if`, `else`, and `else if` constructs ([#44](https://github.com/ventojs/webstorm-vento/issues/44)) ([#132](https://github.com/ventojs/webstorm-vento/issues/132)) ([cca641c](https://github.com/ventojs/webstorm-vento/commit/cca641c62d03fd1a20e6b16e1573430be7c8db24))
* **parser:** use js highlighting where possible ([#134](https://github.com/ventojs/webstorm-vento/issues/134)) ([#137](https://github.com/ventojs/webstorm-vento/issues/137)) ([a62befd](https://github.com/ventojs/webstorm-vento/commit/a62befd8ef09d56194a4954114a94f110c2302f0))
* **pipe:** improve pipe handling ([#41](https://github.com/ventojs/webstorm-vento/issues/41)) ([#129](https://github.com/ventojs/webstorm-vento/issues/129)) ([d9c1b54](https://github.com/ventojs/webstorm-vento/commit/d9c1b5453b4d6fabced5d065ac296bef598f12b9))
* **plugin:** add color settings page for Vento highlighting support in `clean_up` ([47763e0](https://github.com/ventojs/webstorm-vento/commit/47763e0fcfd02dbe1d8f7d8ec5e212680e1da34e))
* **set:** add support for Vento `set` blocks ([#42](https://github.com/ventojs/webstorm-vento/issues/42)) ([#104](https://github.com/ventojs/webstorm-vento/issues/104)) ([29c31d3](https://github.com/ventojs/webstorm-vento/commit/29c31d3e776871d62cbaf3c7623bf2f83f1ee1c5))
* **variables:** add support for variable pipe expressions (`|>`) ([fc1fd8c](https://github.com/ventojs/webstorm-vento/commit/fc1fd8ca8a355d2ec0e7c5a1da034a9170f46748)), closes [#39](https://github.com/ventojs/webstorm-vento/issues/39)


### Bug Fixes

* **cicd:** Update release-please-config.json ([659f9e8](https://github.com/ventojs/webstorm-vento/commit/659f9e821a0cc23944ac2a838b5d74b513f0b447))
* **clean_up:** correct typos and improve syntax in HTML demo text ([ab71ff3](https://github.com/ventojs/webstorm-vento/commit/ab71ff3dbddba8d4d470b67e6d6eb27d8e2cd774))
* **deps:** update junit-framework monorepo to v5.13.4 ([#67](https://github.com/ventojs/webstorm-vento/issues/67)) ([f62c4f0](https://github.com/ventojs/webstorm-vento/commit/f62c4f006259c2a22cde364e8e82cc5b970d0482))
* **deps:** update junit-framework monorepo to v6.0.1 ([#125](https://github.com/ventojs/webstorm-vento/issues/125)) ([5bfc342](https://github.com/ventojs/webstorm-vento/commit/5bfc342876f845c2f922c687e71acc1d6d7382da))

## [Unreleased]

## [0.1.0] - 2025-10-17
### Added

- Delegate parsing to the JetBrains JavaScript plugin. (#50)
- Comment block (`{{# }}`) support. (#47)
- Delegate parsing of the main body of a `.vto` file to the JetBrains HTML plugin. (#51)
- Variable block with (`{{ }}`) and without trimming (`{{- -}}`) support. (#36) (#38)
- Variable pipe support (#39)
- for support (#43) (#77)
- import support (#49) (#92)
- export support (#49) (#92)
- set support (#49) (#92)
- layout and slot support (#46) (#108)
- include support (#45) (#112)
- echo support (#40) (#112)
- if support (#44) (#132)

### Fixed

- Fixed the plugin project (#25)
- Fixed the GitHub workflow security issue (#55)
- Fixed the release process (#60)
- redesigned the lexer and parser (#94) (#113)
