package me.hwiggy.vision.core

import com.google.gson.JsonObject
import me.hwiggy.vision.api.product.Product
import me.hwiggy.vision.api.resource.JsonResource
import me.hwiggy.vision.api.resource.RootResourceManager
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.io.path.Path
import kotlin.system.exitProcess

object VisionEntrypoint {
    private val LOGGER = LoggerFactory.getLogger(VisionEntrypoint::class.java)
    private val pluginManager = Product.Manager.Cache
    private val resourceManager = RootResourceManager(this::class.java)

    private lateinit var config: JsonObject

    @JvmStatic fun main(args: Array<String>) {
        LOGGER.info("Starting Vision...")
        LOGGER.info("Adding shutdown hook...")
        Runtime.getRuntime().addShutdownHook(Thread {
            LOGGER.info("Attempting graceful shutdown...")
            Product.Manager.Self.close()
            LOGGER.info("Vision is terminated.")
        })
        LOGGER.info("Loading configuration from disk...")
        config = kotlin.runCatching {
            resourceManager.loadFromDiskThrowing(JsonResource, Path("config.json"))
        }.getOrElse {
            LOGGER.info("Could not load config from disk, copying from jar...")
            resourceManager.loadFromJarThrowing(JsonResource, Path("config.example.json"), Path("config.json")).also {
                LOGGER.info("Example configuration saved to disk, exiting...")
                exitProcess(1)
            }
        }
        LOGGER.info("Attempting to load products...")
        pluginManager.loadExtensions(File("products")) { it.extension.endsWith("jar", true) }.forEach { ext ->
            val desc = ext.descriptor
            LOGGER.info("Enabling '${desc.name}' v${desc.version}...")
            try {
                ext.enable()
            } catch (e: Exception) {
                LOGGER.error("Error enabling '${desc.name}' v${desc.version}!", e)
            }
        }
    }

}