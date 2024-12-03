package me.hwiggy.vision.product

import me.hwiggy.vision.api.product.Product

class TestProductEntrypoint : Product() {
    override fun enable() {
        logger.info("LOADED THE TEST PRODUCT!")
    }
}