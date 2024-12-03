rootProject.name = "Vision"

val vKotlin = "2.0.0"
val vJDA = "5.2.1"
val vLogback = "1.5.12"
val vGson = "2.11.0"

val vKommander = "1.8.0"
val vExtensible = "1.4.5"
val vRegroup = "1.2-SNAPSHOT"

pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.0"
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("coreLibs") {
            library("kotlin", "org.jetbrains.kotlin:kotlin-stdlib:$vKotlin")
            library("jda", "net.dv8tion:JDA:$vJDA")
            library("kommander", "me.hwiggy.kommander:Discord:$vKommander")
            library("extensible", "me.hwiggy:Extensible:$vExtensible")
            library("logback", "ch.qos.logback:logback-classic:$vLogback")
            library("regroup", "me.hwiggy.regroup:API:$vRegroup")
            library("gson", "com.google.code.gson:gson:$vGson")
        }
    }
}

include("Module")
include("Product")
include("Module:API")
include("Module:Core")
include("Product:TestProduct")
findProject(":Product:TestProduct")?.name = "TestProduct"
