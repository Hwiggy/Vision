plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly(project(":Module:API"))
    compileOnly(coreLibs.logback)
}