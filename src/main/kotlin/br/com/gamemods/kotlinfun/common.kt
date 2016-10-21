package br.com.gamemods.kotlinfun

fun Any?.str() = this?.toString() ?: ""

val toStringOrEmpty : ((Any?) -> CharSequence) = { it.str() }

abstract class ColorizedBase<E>(protected val str: String) {
    var color : E? = null
        protected set

    protected abstract val format : MutableSet<E>

    override fun toString(): String {
        return (color?.toString() ?: "") + format.joinToString(separator = "", transform = toStringOrEmpty) + str
    }

    abstract fun black() : ColorizedBase<E>
    abstract fun darkBlue() : ColorizedBase<E>
    abstract fun darkGreen() : ColorizedBase<E>
    abstract fun darkAqua() : ColorizedBase<E>
    abstract fun darkRed() : ColorizedBase<E>
    abstract fun darkPurple() : ColorizedBase<E>
    abstract fun gold() : ColorizedBase<E>
    abstract fun gray() : ColorizedBase<E>
    abstract fun darkGray() : ColorizedBase<E>
    abstract fun blue() : ColorizedBase<E>
    abstract fun green() : ColorizedBase<E>
    abstract fun aqua() : ColorizedBase<E>
    abstract fun red() : ColorizedBase<E>
    abstract fun lightPurple() : ColorizedBase<E>
    abstract fun yellow() : ColorizedBase<E>
    abstract fun white() : ColorizedBase<E>
    abstract fun reset() : ColorizedBase<E>
    abstract fun magic() : ColorizedBase<E>
    abstract fun bold() : ColorizedBase<E>
    abstract fun strike() : ColorizedBase<E>
    abstract fun underline() : ColorizedBase<E>
    abstract fun italic() : ColorizedBase<E>

    fun noColor(): ColorizedBase<E> {
        color = null
        return this
    }
    fun noFormat() : ColorizedBase<E> {
        format.clear()
        return this
    }

    private fun addFormat(f: E) : ColorizedBase<E> {
        format.add(f)
        return this
    }

    operator fun plus(o: Any?) = this.toString() + o
}
