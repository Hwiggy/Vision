import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

allprojects{
    val buildFolder = File(rootProject.projectDir, "build")

    group = "me.hwiggy.shoelace"
    version = "1.0-SNAPSHOT"
    buildDir = File(buildFolder, name)

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://nexus.mcdevs.us/repository/mcdevs")
    }

    tasks {
        withType<ProcessResources> {
            filesMatching("description.json") { expand("project" to project) }
            outputs.upToDateWhen { false }
        }
        withType<JavaCompile> {
            targetCompatibility = "1.8"
        }
        withType<KotlinCompile> {
            compilerOptions {
                jvmTarget = JvmTarget.JVM_1_8
            }
        }
        withType<ShadowJar> {
            destinationDirectory.set(File(buildFolder, "+ libs"))
        }
    }
}