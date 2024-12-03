package me.hwiggy.vision.api.product

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import me.hwiggy.extensible.binding.HashMapLoader
import me.hwiggy.extensible.binding.jvm.classloader.JarParentClassLoader
import me.hwiggy.extensible.binding.jvm.contract.JarDescriptor
import me.hwiggy.extensible.binding.jvm.contract.JarExtension
import me.hwiggy.extensible.binding.jvm.contract.JarLoadStrategy
import me.hwiggy.vision.api.resource.ProductResourceManager
import org.slf4j.LoggerFactory
import java.io.File
import java.util.jar.JarFile

open class Product : JarExtension<Product.Description>() {
    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }
    protected val resourceManager by lazy { ProductResourceManager(this) }

    class Description(
        @SerializedName("name") override val name: String,
        @SerializedName("version") override val version: String,
        @SerializedName("author") val author: String,
        @SerializedName("main") override val mainClass: String,
        @SerializedName("hard-dependencies") override val hardDependencies: List<String>,
        @SerializedName("soft-dependencies") override val softDependencies: List<String>
    ) : JarDescriptor

    // Ok there is a more reasonable way to do this, but I forgot how my own library works
    class Manager : JarParentClassLoader<Description, Product>(getSystemClassLoader()) {
        private val logger by lazy { LoggerFactory.getLogger(this::class.java) }
        override val loader = ProductClassCache()
        inner class ProductClassCache : HashMapLoader<Description, Product>() {

            override val strategy = object : JarLoadStrategy<Description, Product>(this@Manager) {
                override fun readDescriptor(source: File): Description = JarFile(source).use { jar ->
                    jar.getInputStream(jar.getEntry("description.json")).use {
                        Gson().fromJson(it.reader(), Description::class.java)
                    }
                }
            }

            override fun performLoad(extension: Product) {
                val desc = extension.descriptor
                logger.info("Loading product '${desc.name}' v${desc.version} by ${desc.author}...")
                extension.load()
            }

            override fun handleUncaught(ex: Throwable) {
                logger.error("Error loading product!", ex)
            }
        }

        companion object {
            @JvmStatic val Self = Manager()
            @JvmStatic val Cache = Self.loader
        }
    }
}