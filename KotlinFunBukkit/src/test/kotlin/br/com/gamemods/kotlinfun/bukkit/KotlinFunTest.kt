package br.com.gamemods.kotlinfun.bukkit

import org.junit.Assert.*
import org.junit.Test

class KotlinFunTest {

    @Test
    fun colors() {
        assertEquals("§cTest", "Test".red().toString())
        assertEquals("§cTest§9§l 123", "Test".red() + " 123".blue().bold())
        assertEquals("§cTest§9§l 123", "Test".red() + " 123".bold().blue())
        assertEquals("§l§oBold and Italic", "Bold and Italic".bold().italic().toString())
    }
}
