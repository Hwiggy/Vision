import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "me.hwiggy.vision.module"

dependencies {
    api(project(":Module:API"))
    implementation(kotlin("stdlib"))
    implementation(coreLibs.jda)
    implementation(coreLibs.logback)
}

tasks.withType<ShadowJar> {
    manifest.attributes.apply {
        put("Main-Class", "me.hwiggy.vision.core.VisionEntrypoint")
    }
}