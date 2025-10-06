plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "com.wyattphillips"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2023.2.5")
    type.set("AI") // Android Studio
    
    plugins.set(listOf(
        "com.intellij.nativeDebug"
    ))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("241.*")
        
        // Extract the <!-- Plugin description --> section from README.md and provide to the plugin's manifest
        pluginDescription.set(
            projectDir.resolve("README.md").readText().lines().run {
                val start = "## Features"
                val end = "## Installation"

                if (!joinToString("\n").contains(start) || !joinToString("\n").contains(end)) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }

                subList(indexOf(start) + 1, indexOf(end))
                    .joinToString("\n").trim()
            }
        )
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}