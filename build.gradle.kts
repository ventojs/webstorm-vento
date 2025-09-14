/*
 * Copyright (c) 2025 Florian Hehlen & Óscar Otero
 * All rights reserved.
 */
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.2.1"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
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

// Configure GrammarKit to generate the lexer
tasks {
    generateLexer {
        sourceFile.set(file("src/main/jflex/VentoLexer.flex"))
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
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // JUnit 5 (Jupiter) for tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.3")

    // Support for legacy JUnit 3/4 tests (e.g., classes extending TestCase)
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.10.3")

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
    pluginVerification { ides { recommended() } }
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
