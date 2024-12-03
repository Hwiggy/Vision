import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    tasks.withType<ShadowJar> {
        destinationDirectory.set(File(rootProject.projectDir, "build/+ libs/products"))
        minimize()
    }
}