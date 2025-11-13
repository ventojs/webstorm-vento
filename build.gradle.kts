/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask
import org.jlleitschuh.gradle.ktlint.tasks.KtLintFormatTask

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.2.21"
    id("org.jetbrains.intellij.platform") version "2.10.4"
    id("org.jetbrains.grammarkit") version "2023.3"
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
    id("org.jetbrains.changelog") version "2.4.0"
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

kotlin { jvmToolchain(21) }

repositories {
    mavenCentral()
    intellijPlatform { defaultRepositories() }
}

// Configure source sets to include generated sources
sourceSets {
    main {
        java.srcDirs("src/main/gen")
        resources.srcDirs("src/main/resources")
    }
}

// Task to copy images from docs/assets to plugin resources
val copyImagesToResources =
    tasks.register<Copy>("copyImagesToResources") {
        from(layout.projectDirectory.dir("docs/assets"))
        into(layout.buildDirectory.dir("resources/main/assets"))
        include("*.png", "*.jpg", "*.jpeg", "*.svg", "*.gif")
    }

// Task to prepare cleaned .flex files for generation
val prepareFlexFiles =
    tasks.register<Task>("prepareFlexFiles") {
        val sourceDir = layout.projectDirectory.dir("src/main/jflex")
        val tempDir = layout.buildDirectory.dir("tmp/jflex-cleaned")

        inputs.dir(sourceDir)
        outputs.dir(tempDir)

        doLast {
            val sourceDirFile = sourceDir.asFile
            val tempDirFile = tempDir.get().asFile

            // Clean and recreate temp directory
            tempDirFile.deleteRecursively()
            tempDirFile.mkdirs()

            // Process include files to extract blocks
            val includeFiles =
                sourceDirFile
                    .walkTopDown()
                    .filter { it.isFile && it.extension == "flex" && it.path.contains("includes/") }

            val block1Lines = mutableSetOf<String>()
            val block2Lines = mutableSetOf<String>()
            val includeRuleContent = mutableMapOf<String, String>()
            // Track duplicates across include files
            val block1DupAcrossIncludes = mutableSetOf<String>()
            val block2DupAcrossIncludes = mutableSetOf<String>()

            includeFiles.forEach { includeFile ->
                val content = includeFile.readText()
                val relativePath = includeFile.relativeTo(sourceDirFile).path

                // Extract BLOCK 1 content (imports) and deduplicate
                val block1Regex = Regex("""// BLOCK 1 - START\s*\n(.*?)\n// BLOCK 1 - END""", RegexOption.DOT_MATCHES_ALL)
                block1Regex.find(content)?.let { match ->
                    val blockContent = match.groupValues[1].trim()
                    if (blockContent.isNotEmpty()) {
                        // Split by lines and add to set for deduplication
                        blockContent.lines().forEach { line ->
                            val trimmedLine = line.trim()
                            if (trimmedLine.isNotEmpty()) {
                                if (!block1Lines.add(trimmedLine)) {
                                    block1DupAcrossIncludes.add(trimmedLine)
                                }
                            }
                        }
                    }
                }

                // Extract BLOCK 2 content (states) and deduplicate
                val block2Regex = Regex("""// BLOCK 2 - START\s*\n(.*?)\n// BLOCK 2 - END""", RegexOption.DOT_MATCHES_ALL)
                block2Regex.find(content)?.let { match ->
                    val blockContent = match.groupValues[1].trim()
                    if (blockContent.isNotEmpty()) {
                        // Split by lines and add to set for deduplication
                        blockContent.lines().forEach { line ->
                            val trimmedLine = line.trim()
                            if (trimmedLine.isNotEmpty()) {
                                if (!block2Lines.add(trimmedLine)) {
                                    block2DupAcrossIncludes.add(trimmedLine)
                                }
                            }
                        }
                    }
                }

                // Extract rule content (everything after second %%)
                val parts = content.split("%%")
                if (parts.size >= 3) {
                    val ruleContent = parts.drop(2).joinToString("%%").trim()
                    includeRuleContent[relativePath] = ruleContent
                }
            }

            // Process root VentoLexer.flex file
            val rootFile = File(sourceDirFile, "VentoLexer.flex")
            if (rootFile.exists()) {
                val rootContent = rootFile.readText()
                val parts = rootContent.split("%%")

                if (parts.size >= 3) {
                    val beforeFirstPercent = parts[0]
                    val betweenPercents = parts[1]
                    val afterSecondPercent = parts.drop(2).joinToString("%%")

                    // Build merged content with de-duplication against root and among includes
                    // Extract existing items from root declarations section (between the first and second %%)
                    val existingImportLinesInRoot =
                        beforeFirstPercent
                            .lines()
                            .map { it.trim() }
                            .filter { it.startsWith("import ") }
                            .toSet()

                    val trimmedBetweenLines = betweenPercents.lines().map { it.trim() }.toList()
                    val existingBetweenSet = trimmedBetweenLines.toSet()

                    val stateDeclRegex = Regex("^%state\\s+(.+)$")
                    val macroRegex = Regex("^([A-Z_][A-Z0-9_]*)\\s*=.*$")

                    // Collect existing macro names and state names from root
                    val existingMacroNames =
                        trimmedBetweenLines
                            .mapNotNull { line -> macroRegex.matchEntire(line)?.groupValues?.get(1) }
                            .toMutableSet()
                    val existingStateNames =
                        trimmedBetweenLines
                            .flatMap { line ->
                                stateDeclRegex
                                    .matchEntire(line)
                                    ?.groupValues
                                    ?.get(1)
                                    ?.trim()
                                    ?.split(Regex("\\s+"))
                                    ?.filter { it.isNotBlank() }
                                    ?: emptyList()
                            }.toMutableSet()

                    // Prepare include BLOCK 1 imports: filter out those already present in root
                    val dedupBlock1Imports =
                        block1Lines
                            .filter { it.isNotBlank() }
                            .filterNot { existingImportLinesInRoot.contains(it.trim()) }
                            .toSortedSet()
                    // Imports dropped because they already exist in root
                    val importsDupAgainstRoot =
                        block1Lines
                            .filter { existingImportLinesInRoot.contains(it.trim()) }
                            .toSortedSet()

                    // Prepare include BLOCK 2: split into states, macros (by name), and others; filter by existing
                    val includeStateNames = linkedSetOf<String>()
                    val includeMacrosByName = linkedMapOf<String, String>()
                    val includeOtherLines = linkedSetOf<String>()
                    // Track duplicates against root and across includes for BLOCK 2
                    val statesDupAgainstRoot = mutableSetOf<String>()
                    val macrosDupAgainstRoot = mutableSetOf<String>()
                    val macrosDupAcrossIncludesByName = mutableSetOf<String>()
                    val otherDupAgainstRoot = mutableSetOf<String>()

                    block2Lines.forEach { rawLine ->
                        val line = rawLine.trim()
                        if (line.isEmpty()) return@forEach
                        val stateMatch = stateDeclRegex.matchEntire(line)
                        if (stateMatch != null) {
                            val names =
                                stateMatch.groupValues[1]
                                    .trim()
                                    .split(Regex("\\s+"))
                                    .filter { it.isNotBlank() }
                            names.forEach { name ->
                                if (existingStateNames.contains(name)) {
                                    statesDupAgainstRoot.add(name)
                                } else {
                                    includeStateNames.add(name)
                                }
                            }
                        } else {
                            val macroMatch = macroRegex.matchEntire(line)
                            if (macroMatch != null) {
                                val name = macroMatch.groupValues[1]
                                when {
                                    existingMacroNames.contains(name) -> macrosDupAgainstRoot.add(name)
                                    includeMacrosByName.containsKey(name) -> macrosDupAcrossIncludesByName.add(name)
                                    else -> includeMacrosByName[name] = line
                                }
                            } else {
                                // keep non-macro, non-state lines if they are not already in root between %%
                                if (existingBetweenSet.contains(line)) {
                                    otherDupAgainstRoot.add(line)
                                } else {
                                    includeOtherLines.add(line)
                                }
                            }
                        }
                    }

                    // Report duplicates as warnings
                    if (block1DupAcrossIncludes.isNotEmpty()) {
                        logger.warn(
                            "prepareFlexFiles: Duplicate import lines across include files were deduplicated: ${
                                block1DupAcrossIncludes.joinToString(
                                    ", ",
                                )
                            }",
                        )
                    }
                    if (importsDupAgainstRoot.isNotEmpty()) {
                        logger.warn(
                            "prepareFlexFiles: Import lines skipped because they already exist in root VentoLexer.flex: ${
                                importsDupAgainstRoot.joinToString(
                                    ", ",
                                )
                            }",
                        )
                    }
                    if (block2DupAcrossIncludes.isNotEmpty()) {
                        logger.warn(
                            "prepareFlexFiles: Duplicate declaration lines across include files were deduplicated: ${
                                block2DupAcrossIncludes.joinToString(
                                    ", ",
                                )
                            }",
                        )
                    }
                    if (statesDupAgainstRoot.isNotEmpty()) {
                        logger.warn(
                            "prepareFlexFiles: State names skipped because they already exist in root VentoLexer.flex: ${
                                statesDupAgainstRoot.toSortedSet().joinToString(
                                    ", ",
                                )
                            }",
                        )
                    }
                    if (macrosDupAgainstRoot.isNotEmpty()) {
                        logger.warn(
                            "prepareFlexFiles: Macro names skipped because they already exist in root VentoLexer.flex: ${
                                macrosDupAgainstRoot.toSortedSet().joinToString(
                                    ", ",
                                )
                            }",
                        )
                    }
                    if (macrosDupAcrossIncludesByName.isNotEmpty()) {
                        val duplicates = macrosDupAcrossIncludesByName.toSortedSet().joinToString(", ")
                        logger.error(
                            "prepareFlexFiles: Macro names duplicated across include files: $duplicates",
                        )
                        throw GradleException(
                            "Build failed: Duplicate macro names with different values found across include files: $duplicates",
                        )
                    }
                    if (otherDupAgainstRoot.isNotEmpty()) {
                        val duplicates = otherDupAgainstRoot.toSortedSet().joinToString(", ")
                        logger.error(
                            "prepareFlexFiles: Lines skipped because they already exist in root declarations: $duplicates",
                        )
                        throw GradleException(
                            "Build failed: Lines skipped because they already exist in root declarations: $duplicates",
                        )
                    }

                    val mergedContent =
                        buildString {
                            // Original content before first %%
                            append(beforeFirstPercent.trimEnd())

                            // Add BLOCK 1 content (imports) if any
                            if (dedupBlock1Imports.isNotEmpty()) {
                                append("\n\n// Merged imports from include files\n")
                                dedupBlock1Imports.forEach { line ->
                                    append(line)
                                    append("\n")
                                }
                            }

                            append("\n%%")

                            // Original content between %% markers
                            append(betweenPercents)

                            // Add BLOCK 2 content (states/macros) if any
                            if (includeStateNames.isNotEmpty() || includeMacrosByName.isNotEmpty() || includeOtherLines.isNotEmpty()) {
                                append("\n// Merged states from include files\n")
                                includeStateNames.toSortedSet().forEach { state ->
                                    append("%state ")
                                    append(state)
                                    append("\n")
                                }
                                includeMacrosByName.toSortedMap().values.forEach { macroLine ->
                                    append(macroLine)
                                    append("\n")
                                }
                                includeOtherLines.forEach { line ->
                                    append(line)
                                    append("\n")
                                }
                            }

                            append("\n%%")

                            // Process content after second %%, replacing %include directives
                            val processedAfterContent =
                                afterSecondPercent
                                    .lines()
                                    .joinToString("\n") { line ->
                                        val trimmedLine = line.trim()
                                        if (trimmedLine.startsWith("%include ")) {
                                            val includePath = trimmedLine.removePrefix("%include ").trim()
                                            val ruleContent = includeRuleContent[includePath]
                                            if (ruleContent != null) {
                                                "\n// Content from $includePath\n$ruleContent"
                                            } else {
                                                "// Warning: Could not find content for $includePath"
                                            }
                                        } else {
                                            line
                                        }
                                    }

                            append(processedAfterContent)
                        }

                    // Write merged file
                    val targetFile = File(tempDirFile, "VentoLexer.flex")
                    targetFile.writeText(mergedContent)
                    println("Created merged VentoLexer.flex with content from ${includeFiles.count()} include files")
                }
            }
        }
    }

// Configure GrammarKit to generate the lexer
tasks {

    generateLexer {
        dependsOn("prepareFlexFiles")
        sourceFile.set(file("build/tmp/jflex-cleaned/VentoLexer.flex"))
        targetOutputDir.set(file("src/main/gen/org/js/vento/plugin/lexer"))
        purgeOldFiles.set(true)
    }

    // Ensure lexer is generated and moved before compilation
    compileKotlin {
        dependsOn(generateLexer)
    }

    compileJava {
        dependsOn(generateLexer)
    }

    // Ensure images are copied before processing resources
    processResources {
        dependsOn(copyImagesToResources)
    }

    // Enable JUnit Platform for tests (Jupiter + Vintage)
    test {
        useJUnitPlatform()
        // Ensure cleaned flex file is created before tests
        dependsOn("prepareFlexFiles")
    }

    // Make KtLint tasks depend on lexer generation
    withType<KtLintCheckTask> {
        dependsOn("generateLexer")
    }

    // If you also have KtLint format tasks, add the same dependency
    withType<KtLintFormatTask> {
        dependsOn("generateLexer")
    }

    publishPlugin {
        dependsOn("patchChangelog")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // JUnit 6 (Jupiter) for tests
    testImplementation(platform("org.junit:junit-bom:6.0.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Support for legacy JUnit 3/4 tests (e.g., classes extending TestCase)
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:6.0.1")

    // Kotlin test assertions routed to JUnit Platform
    testImplementation(kotlin("test"))

    intellijPlatform {
        create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })
        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })
        pluginVerifier()
        zipSigner()
    }

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
    }

    testImplementation("junit:junit:4.13.2")
}

intellijPlatform {

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        channels =
            providers
                .gradleProperty("pluginVersion")
                .map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    // Configure plugin verifier IDEs
    pluginVerification {
        ides {
            // Webstorm
            ide("WS", "2024.3")
            ide("WS", "2025.2")
//            ide("WS", "253.28086.39")
            // IntelliJ Ultimate
            ide("IU", "2024.3")
            ide("IU", "2025.2")
//            ide("IU", "253.28086.51")
        }
    }

    pluginConfiguration {
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        description =
            providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                with(it.lines()) {
                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                    }
                    val descriptionText = subList(indexOf(start) + 1, indexOf(end)).joinToString("\n")
                    // Replace docs/assets/ paths with plugin-relative paths
                    val updatedText =
                        descriptionText.replace(
                            Regex("""docs/assets/([^"'\s)]+)"""),
                            "assets/$1",
                        )
                    markdownToHTML(updatedText)
                }
            }

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes =
            providers.gradleProperty("pluginVersion").map { pluginVersion ->
                with(changelog) {
                    renderItem(
                        (getOrNull(pluginVersion) ?: getUnreleased())
                            .withHeader(false)
                            .withEmptySections(false),
                        Changelog.OutputType.HTML,
                    )
                }
            }

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
        }
    }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl").get()
}

tasks { wrapper { gradleVersion = providers.gradleProperty("gradleVersion").get() } }

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Ktlint: Add formatting and linting tasks
tasks {
    // Make the standard 'check' task run ktlint checks as well
    named("check") { dependsOn("ktlintCheck") }

    // Convenience alias to format Kotlin sources
    register<DefaultTask>("formatKotlin") { dependsOn("ktlintFormat") }
}
