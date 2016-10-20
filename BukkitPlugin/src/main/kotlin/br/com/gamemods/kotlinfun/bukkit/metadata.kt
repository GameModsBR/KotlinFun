@file:JvmName("MetadataUtil")
package br.com.gamemods.kotlinfun.bukkit

import org.bukkit.metadata.MetadataValue
import org.bukkit.metadata.Metadatable
import org.bukkit.plugin.Plugin
import org.bukkit.util.NumberConversions
import java.lang.ref.WeakReference

fun <T> MetadataValue.value(type: Class<T>) : T? {
    val value = value()
    return if(type.isInstance(value)) type.cast(value) else null
}

fun Metadatable.getMetadata(metadataKey: String, plugin: Plugin) = getMetadata(metadataKey).find { it.owningPlugin == plugin }
fun Metadatable.hasMetadata(metadataKey: String, plugin: Plugin) = getMetadata(metadataKey, plugin) != null

fun <T> Metadatable.getMetadata(metadataKey: String, plugin: Plugin, type: Class<T>)
        = getMetadata(metadataKey, plugin)?.value(type)

fun Metadatable.hasMetadata(metadataKey: String, plugin: Plugin, type: Class<*>)
        = type.isInstance(getMetadata(metadataKey, plugin))

fun <T> Metadatable.getMetadata(metadataKey: String, type: Class<T>) : List<T>
        = getMetadata(metadataKey).map { it.value() }.filterIsInstance(type)

fun Metadatable.hasMetadata(metadataKey: String, type: Class<*>)
        = getMetadata(metadataKey).map { it.value() }.filterIsInstance(type).isNotEmpty()

fun Metadatable.setMetadata(metadataKey: String, plugin: Plugin, value: Any?) = setMetadata(metadataKey, ConstantMetadataValue(plugin, value))

class ConstantMetadataValue(plugin: Plugin, private val value: Any?) : AbstractMetadataValue(WeakReference(plugin)) {
    override fun invalidate() {}
    override fun value() = value
}

class MutableMetadataValue(plugin: Plugin, var value: Any?) : AbstractMetadataValue(WeakReference(plugin)) {
    override fun invalidate() {}
    override fun value() = value
}

abstract class AbstractMetadataValue(private val plugin: WeakReference<Plugin>) : MetadataValue {

    final override fun getOwningPlugin(): Plugin? = plugin.get()

    override abstract fun value(): Any?

    override fun asInt(): Int {
        return NumberConversions.toInt(value())
    }

    override fun asFloat(): Float {
        return NumberConversions.toFloat(value())
    }

    override fun asDouble(): Double {
        return NumberConversions.toDouble(value())
    }

    override fun asLong(): Long {
        return NumberConversions.toLong(value())
    }

    override fun asShort(): Short {
        return NumberConversions.toShort(value())
    }

    override fun asByte(): Byte {
        return NumberConversions.toByte(value())
    }

    override fun asBoolean(): Boolean {
        val value = value()
        if (value is Boolean) {
            return value
        }

        if (value is Number) {
            return value.toInt() != 0
        }

        if (value is String) {
            return value.toBoolean()
        }

        return value != null
    }

    override fun asString() = value()?.toString() ?: ""
}
