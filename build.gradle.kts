/*
 * Copyright (c) 2023 Óscar Otero
 * All rights reserved.
 */

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.2.1"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
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
        targetOutputDir.set(file("src/main/gen/org/js/vento/webstormvento"))
        purgeOldFiles.set(true)
    }

    // Ensure lexer is generated and moved before compilation
    compileKotlin {
        dependsOn(generateLexer)
    }

    compileJava {
        dependsOn(generateLexer)
    }
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    intellijPlatform {
        create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })
        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })
        pluginVerifier()
        zipSigner()
    }
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
        channels = providers.gradleProperty("pluginVersion")
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
