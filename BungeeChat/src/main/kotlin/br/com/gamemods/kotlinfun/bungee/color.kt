@file:JvmName(name = "BungeeColorUtil")
package br.com.gamemods.kotlinfun.bungee

import br.com.gamemods.kotlinfun.ColorizedBase
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.*
import java.util.*

class Colorized(str: String) : ColorizedBase<ChatColor>(str) {
    override val format : EnumSet<ChatColor> = EnumSet.noneOf(ChatColor::class.java)

    fun currentFormat(): EnumSet<ChatColor> = format.clone()

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
    @Deprecated("Conflicts with ComponentBuilder.reset()", ReplaceWith("resetColor()"))
    override fun reset() = resetColor()
    fun resetColor() = setColor(ChatColor.RESET)

    override fun magic() = addFormat(ChatColor.MAGIC)
    override fun bold() = addFormat(ChatColor.BOLD)
    override fun strike() = addFormat(ChatColor.STRIKETHROUGH)
    override fun underline() = addFormat(ChatColor.UNDERLINE)
    override fun italic() = addFormat(ChatColor.ITALIC)

    fun builder() = ComponentArray(create())
    fun color(color: ChatColor) = builder().color(color)
    fun obfuscated(magic: Boolean) = builder().obfuscated(magic)
    fun bold(bold: Boolean) = builder().bold(bold)
    fun strikethrough(strike: Boolean) = builder().strikethrough(strike)
    fun underlined(underlined: Boolean) = builder().underlined(underlined)
    fun italic(italic: Boolean) = builder().italic(italic)
    fun append(text: String) = builder().append(text)
    fun append(text: String, retention: ComponentBuilder.FormatRetention) = builder().append(text, retention)
    fun insertion(insertion: String) = builder().insertion(insertion)
    fun event(clickEvent: ClickEvent) = builder().event(clickEvent)
    fun event(hoverEvent: HoverEvent) = builder().event(hoverEvent)
    fun retain(retain: ComponentBuilder.FormatRetention) = builder().retain(retain)

    fun create(): Array<BaseComponent> = TextComponent.fromLegacyText(toString())

    operator fun plus(o: BaseComponent) = builder() + o
    operator fun plus(o: ComponentBuilder) = builder() + o
    operator fun plus(o: ComponentArray) = builder() + o
    operator fun plus(o: Array<out BaseComponent>) = builder() + o
}

operator fun BaseComponent.plus(o: ChatColor) = ComponentArray(this) + o
operator fun BaseComponent.plus(o: BaseComponent) = ComponentArray(this) + o
operator fun BaseComponent.plus(o: ComponentBuilder) = ComponentArray(o, this)
operator fun BaseComponent.plus(o: ComponentArray) = {o.add(0, this); o}
operator fun BaseComponent.plus(o: Array<out BaseComponent>) = ComponentArray(this) + o
operator fun BaseComponent.plus(o: String) = ComponentArray(this) + o
operator fun BaseComponent.plus(o: Any?) = ComponentArray(this) + o
fun BaseComponent.insertion(insertion: String?) = {setInsertion(insertion); this}
fun BaseComponent.event(clickEvent: ClickEvent?) = {setClickEvent(clickEvent); this}
fun BaseComponent.event(hoverEvent: HoverEvent?) = {setHoverEvent(hoverEvent); this}
fun BaseComponent.format(style: ChatColor?) : BaseComponent {
    when(style) {
        null -> Unit
        ChatColor.BOLD -> isBold = true
        ChatColor.ITALIC -> isItalic = true
        ChatColor.UNDERLINE -> isUnderlined = true
        ChatColor.STRIKETHROUGH -> isStrikethrough = true
        ChatColor.MAGIC -> isObfuscated = true
        else -> color = style
    }
    return this
}
fun BaseComponent.format(style: Iterable<ChatColor?>) : BaseComponent{
    for (st in style)
        format(st)
    return this
}

operator fun Array<out BaseComponent>.plus(o: ChatColor) = builder() + o
operator fun Array<out BaseComponent>.plus(o: BaseComponent) = builder() + o
operator fun Array<out BaseComponent>.plus(o: ComponentBuilder) = builder() + o
operator fun Array<out BaseComponent>.plus(o: ComponentArray) = builder() + o
operator fun Array<out BaseComponent>.plus(o: Array<out BaseComponent>) = builder() + o
operator fun Array<out BaseComponent>.plus(o: String) = builder() + o
operator fun Array<out BaseComponent>.plus(o: Any?) = builder() + o
fun Array<out BaseComponent>.builder() = ComponentArray(this)
fun Array<out BaseComponent>.toLegacyString() = wrap().toLegacyText()!!
fun Array<out BaseComponent>.toPlainText() = wrap().toPlainText()!!
fun Array<out BaseComponent>.wrap() : TextComponent {
    val comp = TextComponent()
    comp.extra.addAll(this)
    return comp
}

operator fun ChatColor.plus(o: BaseComponent) = component() + o
operator fun ChatColor.plus(o: ComponentBuilder) = component() + o
operator fun ChatColor.plus(o: ComponentArray) = component() + o
operator fun ChatColor.plus(o: Array<out BaseComponent>) = component() + o
operator fun ChatColor.plus(str: Any?) = this.toString() + str
fun ChatColor.component() = TextComponent("").format(this)

operator fun String.plus(o: BaseComponent) = component() + o
operator fun String.plus(o: ComponentBuilder) = component() + o
operator fun String.plus(o: ComponentArray) = component() + o
operator fun String.plus(o: Array<out BaseComponent>) = component() + o
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
fun String.resetColor() = Colorized(this).resetColor()
fun String.magic() = Colorized(this).magic()
fun String.bold() = Colorized(this).bold()
fun String.strike() = Colorized(this).strike()
fun String.underline() = Colorized(this).underline()
fun String.italic() = Colorized(this).italic()
fun String.builder() = ComponentBuilder(this)
fun String.component() = TextComponent(this)
fun String.fromLegacy(): Array<BaseComponent> = TextComponent.fromLegacyText(this)
fun String.insertion(insertion: String) = component().insertion(insertion)
fun String.event(clickEvent: ClickEvent) = component().event(clickEvent)
fun String.event(hoverEvent: HoverEvent) = component().event(hoverEvent)

operator fun ComponentBuilder.plus(o: BaseComponent) = create() + o
operator fun ComponentBuilder.plus(o: ComponentBuilder) = ComponentArray(o, create())
operator fun ComponentBuilder.plus(o: ComponentArray) = {o.addAll(0, create().asList()); o}
operator fun ComponentBuilder.plus(o: Array<out BaseComponent>) = this + o.builder()
operator fun ComponentBuilder.plus(o: Any?) = append(o.toString(), ComponentBuilder.FormatRetention.FORMATTING)!!
fun ComponentBuilder.black() = color(ChatColor.BLACK)!!
fun ComponentBuilder.darkBlue() = color(ChatColor.DARK_BLUE)!!
fun ComponentBuilder.darkGreen() = color(ChatColor.DARK_GREEN)!!
fun ComponentBuilder.darkAqua() = color(ChatColor.DARK_AQUA)!!
fun ComponentBuilder.darkRed() = color(ChatColor.DARK_RED)!!
fun ComponentBuilder.darkPurple() = color(ChatColor.DARK_PURPLE)!!
fun ComponentBuilder.gold() = color(ChatColor.GOLD)!!
fun ComponentBuilder.gray() = color(ChatColor.GRAY)!!
fun ComponentBuilder.darkGray() = color(ChatColor.DARK_GRAY)!!
fun ComponentBuilder.blue() = color(ChatColor.BLUE)!!
fun ComponentBuilder.green() = color(ChatColor.GREEN)!!
fun ComponentBuilder.aqua() = color(ChatColor.AQUA)!!
fun ComponentBuilder.red() = color(ChatColor.RED)!!
fun ComponentBuilder.lightPurple() = color(ChatColor.LIGHT_PURPLE)!!
fun ComponentBuilder.yellow() = color(ChatColor.YELLOW)!!
fun ComponentBuilder.white() = color(ChatColor.WHITE)!!
fun ComponentBuilder.resetColor() = color(ChatColor.RESET)!!

fun ComponentBuilder.magic() = obfuscated(true)!!
fun ComponentBuilder.bold() = bold(true)!!
fun ComponentBuilder.strike() = strikethrough(true)!!
fun ComponentBuilder.underline() = underlined(true)!!
fun ComponentBuilder.italic() = italic(true)!!
fun ComponentBuilder.format(style: ChatColor?): ComponentBuilder = when(style) {
    null -> this
    ChatColor.BOLD -> bold(true)
    ChatColor.ITALIC -> italic(true)
    ChatColor.UNDERLINE -> underlined(true)
    ChatColor.STRIKETHROUGH -> strikethrough(true)
    ChatColor.MAGIC -> obfuscated(true)
    else -> color(style)
}
fun ComponentBuilder.format(style: Iterable<ChatColor?>) : ComponentBuilder{
    for (st in style)
        format(st)
    return this
}

class ComponentArray : ArrayList<BaseComponent> {
    var openBuilder : ComponentBuilder? = null

    constructor(initialCapacity: Int = 2) : super(initialCapacity)
    constructor(components: Collection<BaseComponent>) : super(components)
    constructor(components: Iterable<BaseComponent>) : super(2) {
        addAll(components)
    }
    constructor(openBuilder: ComponentBuilder?, component: BaseComponent) : super(1) {
        this.openBuilder = openBuilder
        add(component)
    }
    constructor(openBuilder: ComponentBuilder?, components: Array<out BaseComponent>) : super(components.size){
        this.openBuilder = openBuilder
        addAll(components)
    }
    constructor(component: BaseComponent, vararg components: BaseComponent) : super(components.size + 1){
        add(component)
        addAll(components)
    }
    constructor(components: Array<out BaseComponent>) : super(components.size) {
        addAll(components)
    }
    constructor(component: BaseComponent) : super(1) {
        add(component)
    }

    constructor(str: String) : this(TextComponent.fromLegacyText(str))

    var builder : ComponentBuilder set(value) { openBuilder = value } get() {
        var builder = openBuilder
        if(builder != null)
            return builder

        builder = ComponentBuilder("")
        openBuilder = builder
        return builder
    }

    fun build() = builder
    fun color(color: ChatColor) = {builder.color(color); this}
    fun obfuscated(magic: Boolean) = {builder.obfuscated(magic); this}
    fun bold(bold: Boolean) = {builder.bold(bold); this}
    fun strikethrough(strike: Boolean) = {builder.strikethrough(strike); this}
    fun underlined(underlined: Boolean) = {builder.underlined(underlined); this}
    fun italic(italic: Boolean) = {builder.italic(italic); this}
    fun append(text: String) = {builder.append(text); this}
    fun append(text: String, retention: ComponentBuilder.FormatRetention) = {builder.append(text, retention); this}
    @Deprecated("Conflicts with ComponentBuilder.reset()", ReplaceWith("resetColor()"))
    fun reset() = {builder.reset(); this}
    fun insertion(insertion: String) = {builder.insertion(insertion); this}
    fun event(clickEvent: ClickEvent) = {builder.event(clickEvent); this}
    fun event(hoverEvent: HoverEvent) = {builder.event(hoverEvent); this}
    fun retain(retain: ComponentBuilder.FormatRetention) = {builder.retain(retain); this}
    fun create() = {flush(); toTypedArray()}
    fun flush() : ComponentArray {
        addAll(openBuilder?.create() ?: return this)
        openBuilder = null
        return this
    }

    fun black() = color(ChatColor.BLACK)
    fun darkBlue() = color(ChatColor.DARK_BLUE)
    fun darkGreen() = color(ChatColor.DARK_GREEN)
    fun darkAqua() = color(ChatColor.DARK_AQUA)
    fun darkRed() = color(ChatColor.DARK_RED)
    fun darkPurple() = color(ChatColor.DARK_PURPLE)
    fun gold() = color(ChatColor.GOLD)
    fun gray() = color(ChatColor.GRAY)
    fun darkGray() = color(ChatColor.DARK_GRAY)
    fun blue() = color(ChatColor.BLUE)
    fun green() = color(ChatColor.GREEN)
    fun aqua() = color(ChatColor.AQUA)
    fun red() = color(ChatColor.RED)
    fun lightPurple() = color(ChatColor.LIGHT_PURPLE)
    fun yellow() = color(ChatColor.YELLOW)
    fun white() = color(ChatColor.WHITE)
    fun resetColor() = color(ChatColor.RESET)

    fun magic() = obfuscated(true)
    fun bold() = bold(true)
    fun strike() = strikethrough(true)
    fun underline() = underlined(true)
    fun italic() = italic(true)

    fun format(format: ChatColor) = builder.format(format)
    fun format(formats: Iterable<ChatColor>) = builder.format(formats)

    override fun add(element: BaseComponent): Boolean {
        flush()
        return super.add(element)
    }

    override fun addAll(elements: Collection<BaseComponent>): Boolean {
        flush()
        return super.addAll(elements)
    }

    override fun add(index: Int, element: BaseComponent) {
        if(index == size - 1)
            flush()
        super.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<BaseComponent>): Boolean {
        if(index == size - 1)
            flush()
        return super.addAll(index, elements)
    }

    operator fun plus(o: ChatColor) = {flush(); builder.format(o)}
    operator fun plus(o: BaseComponent) = {add(o); this}
    operator fun plus(o: ComponentBuilder) = this + o.create()
    operator fun plus(other: ComponentArray) = {
        addAll(other)
        openBuilder = other.openBuilder
        this
    }
    operator fun plus(o: Array<out BaseComponent>) = {addAll(o); this}
    operator fun plus(o: Any?) = append(o.toString())
}
