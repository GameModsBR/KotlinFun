@file:JvmName(name = "ColorUtil")
package br.com.gamemods.kotlinfun.bukkit

import br.com.gamemods.kotlinfun.ColorizedBase
import org.bukkit.ChatColor
import java.util.*

class Colorized(str: String) : ColorizedBase<ChatColor>(str) {
    override val format : EnumSet<ChatColor> = EnumSet.noneOf(ChatColor::class.java)

    fun currentFormat() = format.clone()

    private fun setColor(c: ChatColor?) : Colorized {
        color = c
        return this
    }

    private fun addFormat(f: ChatColor) : Colorized {
        format.add(f)
        return this
    }

    override fun black() = setColor(ChatColor.BLACK)
    override fun darkBlue() = setColor(ChatColor.DARK_BLUE)
    override fun darkGreen() = setColor(ChatColor.DARK_GREEN)
    override fun darkAqua() = setColor(ChatColor.DARK_AQUA)
    override fun darkRed() = setColor(ChatColor.DARK_RED)
    override fun darkPurple() = setColor(ChatColor.DARK_PURPLE)
    override fun gold() = setColor(ChatColor.GOLD)
    override fun gray() = setColor(ChatColor.GRAY)
    override fun darkGray() = setColor(ChatColor.DARK_GRAY)
    override fun blue() = setColor(ChatColor.BLUE)
    override fun green() = setColor(ChatColor.GREEN)
    override fun aqua() = setColor(ChatColor.AQUA)
    override fun red() = setColor(ChatColor.RED)
    override fun lightPurple() = setColor(ChatColor.LIGHT_PURPLE)
    override fun yellow() = setColor(ChatColor.YELLOW)
    override fun white() = setColor(ChatColor.WHITE)
    override fun reset() = setColor(ChatColor.RESET)

    override fun magic() = addFormat(ChatColor.MAGIC)
    override fun bold() = addFormat(ChatColor.BOLD)
    override fun strike() = addFormat(ChatColor.STRIKETHROUGH)
    override fun underline() = addFormat(ChatColor.UNDERLINE)
    override fun italic() = addFormat(ChatColor.ITALIC)
}

fun String.black() = Colorized(this).black()
fun String.darkBlue() = Colorized(this).darkBlue()
fun String.darkGreen() = Colorized(this).darkGreen()
fun String.darkAqua() = Colorized(this).darkAqua()
fun String.darkRed() = Colorized(this).darkRed()
fun String.darkPurple() = Colorized(this).darkPurple()
fun String.gold() = Colorized(this).gold()
fun String.gray() = Colorized(this).gray()
fun String.darkGray() = Colorized(this).darkGray()
fun String.blue() = Colorized(this).blue()
fun String.green() = Colorized(this).green()
fun String.aqua() = Colorized(this).aqua()
fun String.red() = Colorized(this).red()
fun String.lightPurple() = Colorized(this).lightPurple()
fun String.yellow() = Colorized(this).yellow()
fun String.white() = Colorized(this).white()
fun String.reset() = Colorized(this).reset()
fun String.magic() = Colorized(this).magic()
fun String.bold() = Colorized(this).bold()
fun String.strike() = Colorized(this).strike()
fun String.underline() = Colorized(this).underline()
fun String.italic() = Colorized(this).italic()

operator fun ChatColor.plus(str : String) = this.toString() + str
