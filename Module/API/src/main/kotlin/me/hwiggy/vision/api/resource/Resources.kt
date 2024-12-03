package me.hwiggy.vision.api.resource

import com.google.gson.Gson
import com.google.gson.JsonObject
import me.hwiggy.regroup.api.Resource
import me.hwiggy.vision.api.product.Product
import java.nio.file.Files
import kotlin.io.path.Path

class RootResourceManager(mainClass: Class<*>) : Resource.Manager(mainClass, Path("."))
class ProductResourceManager(product: Product): Resource.Manager(product::class.java, Path("products/${product.descriptor.name}"))

object JsonResource : Resource<JsonObject> {
    override val extension = "json"

    override val loader = Resource.Loader<JsonObject> { path ->
        Files.newInputStream(path).reader().let {
            Gson().fromJson(it, JsonObject::class.java)
        }
    }

    override val saver = Resource.Saver<JsonObject> { res, path ->
        Files.newOutputStream(path).writer().use {
            Gson().toJson(res, it)
        }
    }
}