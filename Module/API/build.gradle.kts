plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "me.hwiggy.vision.module"

dependencies {
    compileOnly(coreLibs.kotlin)
    compileOnly(coreLibs.jda)
    api(coreLibs.kommander)
    api(coreLibs.extensible)
    api(coreLibs.regroup)
    api(coreLibs.gson)
}