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
    id("org.jetbrains.kotlin.jvm") version "2.2.20"
    id("org.jetbrains.intellij.platform") version "2.9.0"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
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
    }
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
                                block1Lines.add(trimmedLine)
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
                                block2Lines.add(trimmedLine)
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

                    // Build merged content
                    val mergedContent =
                        buildString {
                            // Original content before first %%
                            append(beforeFirstPercent.trimEnd())

                            // Add BLOCK 1 content (imports) if any
                            if (block1Lines.isNotEmpty()) {
                                append("\n\n// Merged imports from include files\n")
                                block1Lines.sorted().forEach { line ->
                                    append(line)
                                    append("\n")
                                }
                            }

                            append("\n%%")

                            // Original content between %% markers
                            append(betweenPercents)

                            // Add BLOCK 2 content (states) if any
                            if (block2Lines.isNotEmpty()) {
                                append("\n// Merged states from include files\n")
                                block2Lines.sorted().forEach { line ->
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

    // Enable JUnit Platform for tests (Jupiter + Vintage)
    test {
        useJUnitPlatform()
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

    // JUnit 5 (Jupiter) for tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.13.4")

    // Support for legacy JUnit 3/4 tests (e.g., classes extending TestCase)
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.13.4")

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

    pluginConfiguration {

        version = providers.gradleProperty("pluginVersion")
        description = providers.gradleProperty("pluginDescription")

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = providers.gradleProperty("pluginUntilBuild")
        }
    }

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
            ide("WS", "2025.2")
            // IntelliJ Ultimate
            ide("IU", "2025.2")
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
                    subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
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
