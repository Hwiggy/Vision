package me.hwiggy.vision.core

import me.hwiggy.vision.api.product.Product
import org.slf4j.LoggerFactory
import java.io.File

object VisionEntrypoint {
    private val LOGGER = LoggerFactory.getLogger(VisionEntrypoint::class.java)
    private val pluginManager = Product.ProductClassLoader.loader

    @JvmStatic fun main(args: Array<String>) {
        LOGGER.info("Starting Vision...")
        LOGGER.info("Adding shutdown hook...")
        Runtime.getRuntime().addShutdownHook(Thread {
            LOGGER.info("Attempting graceful shutdown...")
            LOGGER.info("Vision is terminated.")
        })
        LOGGER.info("Attempting to load products...")
        pluginManager.loadExtensions(File("products")) { it.extension.endsWith("jar", true) }
    }

}