@file:JvmName("SpigotUtil")
package br.com.gamemods.kotlinfun.bukkit

import br.com.gamemods.kotlinfun.bungee.ComponentArray
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.*

fun Colorized.builder() = ComponentArray(create())
fun Colorized.color(color: ChatColor) = builder().color(color)
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
