package me.hwiggy.vision.api.product

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import me.hwiggy.extensible.binding.HashMapLoader
import me.hwiggy.extensible.binding.jvm.classloader.JarParentClassLoader
import me.hwiggy.extensible.binding.jvm.contract.JarDescriptor
import me.hwiggy.extensible.binding.jvm.contract.JarExtension
import me.hwiggy.extensible.binding.jvm.contract.JarLoadStrategy
import org.slf4j.LoggerFactory
import java.io.File
import java.util.jar.JarFile

open class Product : JarExtension<Product.Description>() {
    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    class Description @JsonCreator constructor(
        @JsonProperty("name") override val name: String,
        @JsonProperty("version") override val version: String,
        @JsonProperty("author") val author: String,
        @JsonProperty("main") override val mainClass: String,
        @JsonProperty("hard-dependencies") override val hardDependencies: List<String>,
        @JsonProperty("soft-dependencies") override val softDependencies: List<String>
    ) : JarDescriptor

    internal class LoadStrategy(
        parent: JarParentClassLoader<Description, Product>
    ) : JarLoadStrategy<Description, Product>(parent) {
        override fun readDescriptor(source: File): Description = JarFile(source).use { jar ->
            jar.getInputStream(jar.getEntry("description.json")).use { ObjectMapper().readValue(it, Description::class.java) }
        }
    }

    object ProductClassLoader : JarParentClassLoader<Description, Product>(getSystemClassLoader()) {
        override val loader = object : HashMapLoader<Description, Product>() {
            private val logger by lazy { LoggerFactory.getLogger(this::class.java) }
            override val strategy = LoadStrategy(this@ProductClassLoader)

            override fun performLoad(extension: Product) {
                val desc = extension.descriptor
                extension.logger.info("Loading product '${desc.name}' v${desc.version} by ${desc.author}...")
                extension.load()
            }

            override fun handleUncaught(ex: Throwable) {
                logger.error("Error loading product!", ex)
            }
        }
    }
}