@file:JvmName("SpigotUtil")
package br.com.gamemods.kotlinfun.spigot

import br.com.gamemods.kotlinfun.bukkit.Colorized
import br.com.gamemods.kotlinfun.bungee.ComponentArray
import br.com.gamemods.kotlinfun.bungee.*
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.*
import java.util.*

fun Colorized.builder() = ComponentArray(create())
fun Colorized.color(color: ChatColor) = builder().color(color)
fun Colorized.color(color: org.bukkit.ChatColor) = builder().color(color.asBungee())
fun Colorized.obfuscated(magic: Boolean) = builder().obfuscated(magic)
fun Colorized.bold(bold: Boolean) = builder().bold(bold)
fun Colorized.strikethrough(strike: Boolean) = builder().strikethrough(strike)
fun Colorized.underlined(underlined: Boolean) = builder().underlined(underlined)
fun Colorized.italic(italic: Boolean) = builder().italic(italic)
fun Colorized.append(text: String) = builder().append(text)
fun Colorized.append(text: String, retention: ComponentBuilder.FormatRetention) = builder().append(text, retention)
fun Colorized.insertion(insertion: String) = builder().insertion(insertion)
fun Colorized.event(clickEvent: ClickEvent) = builder().event(clickEvent)
fun Colorized.event(hoverEvent: HoverEvent) = builder().event(hoverEvent)
fun Colorized.retain(retain: ComponentBuilder.FormatRetention) = builder().retain(retain)

fun Colorized.create(): Array<BaseComponent> = TextComponent.fromLegacyText(toString())

operator fun Colorized.plus(o: BaseComponent) = builder() + o
operator fun Colorized.plus(o: ComponentBuilder) = builder() + o
operator fun Colorized.plus(o: ComponentArray) = builder() + o
operator fun Colorized.plus(o: Array<out BaseComponent>) = builder() + o

fun br.com.gamemods.kotlinfun.bungee.Colorized.color(color: org.bukkit.ChatColor) = color(color.asBungee())

fun BaseComponent.format(style: org.bukkit.ChatColor?) = format(style?.asBungee())
fun BaseComponent.setColor(color: org.bukkit.ChatColor?) = setColor(color?.asBungee())
fun BaseComponent.format(style: Iterable<org.bukkit.ChatColor?>) : BaseComponent{
    for (st in style)
        format(st)
    return this
}
operator fun Array<out BaseComponent>.plus(o: org.bukkit.ChatColor) = this + o.asBungee()
operator fun BaseComponent.plus(o: org.bukkit.ChatColor) = ComponentArray(this) + o.asBungee()

operator fun org.bukkit.ChatColor.plus(o: BaseComponent) = component() + o
operator fun org.bukkit.ChatColor.plus(o: ComponentBuilder) = component() + o
operator fun org.bukkit.ChatColor.plus(o: ComponentArray) = component() + o
operator fun org.bukkit.ChatColor.plus(o: Array<out BaseComponent>) = component() + o
fun org.bukkit.ChatColor.component() = asBungee().component()

fun ComponentBuilder.format(style: org.bukkit.ChatColor?) = format(style?.asBungee())
fun ComponentBuilder.format(style: Iterable<org.bukkit.ChatColor?>) : ComponentBuilder{
    for (st in style)
        format(st)
    return this
}

fun Iterable<org.bukkit.ChatColor?>.asBungeeNullable() : Iterable<ChatColor?> {
    return object : Iterable<ChatColor?> {
        override fun iterator(): Iterator<ChatColor?> {
            val host = this@asBungeeNullable.iterator()
            return object : Iterator<ChatColor?> {
                override fun hasNext() = host.hasNext()

                override fun next() = host.next()?.asBungee()
            }
        }
    }
}

fun Iterable<org.bukkit.ChatColor>.asBungee() : Iterable<ChatColor> {
    return object : Iterable<ChatColor> {
        override fun iterator(): Iterator<ChatColor> {
            val host = this@asBungee.iterator()
            return object : Iterator<ChatColor> {
                override fun hasNext() = host.hasNext()

                override fun next() = host.next().asBungee()
            }
        }
    }
}

private val bungeeToBukkit = EnumMap<ChatColor, org.bukkit.ChatColor>(ChatColor::class.java)
    get() {
        if(field.isEmpty()) {
            val bungee = ChatColor.values()
            val bukkit = org.bukkit.ChatColor.values()
            for (color in bungee)
                field[color] = bukkit[color.ordinal]
        }
        return field
    }

fun ChatColor.asBukkit() = bungeeToBukkit[this]!!

fun Iterable<ChatColor?>.asBukkitNullable() : Iterable<org.bukkit.ChatColor?> {
    return object : Iterable<org.bukkit.ChatColor?> {
        override fun iterator(): Iterator<org.bukkit.ChatColor?> {
            val host = this@asBukkitNullable.iterator()
            return object : Iterator<org.bukkit.ChatColor?> {
                override fun hasNext() = host.hasNext()

                override fun next() = host.next()?.asBukkit()
            }
        }
    }
}

fun Iterable<ChatColor>.asBukkit() : Iterable<org.bukkit.ChatColor> {
    return object : Iterable<org.bukkit.ChatColor> {
        override fun iterator(): Iterator<org.bukkit.ChatColor> {
            val host = this@asBukkit.iterator()
            return object : Iterator<org.bukkit.ChatColor> {
                override fun hasNext() = host.hasNext()

                override fun next() = host.next().asBukkit()
            }
        }
    }
}

fun ComponentArray.color(color: org.bukkit.ChatColor) = color(color.asBungee())
fun ComponentArray.format(format: org.bukkit.ChatColor) = format(format.asBungee())
fun ComponentArray.format(formats: Iterable<org.bukkit.ChatColor>) = builder.format(formats)
operator fun ComponentArray.plus(o: org.bukkit.ChatColor) = plus(o.asBungee())
