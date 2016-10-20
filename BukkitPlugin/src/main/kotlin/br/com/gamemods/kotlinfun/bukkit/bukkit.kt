@file:JvmName(name = "BukkitUtil")
package br.com.gamemods.kotlinfun.bukkit

import org.bukkit.plugin.java.JavaPlugin

class KotlinFun : JavaPlugin() {
    companion object {
        lateinit var instance : KotlinFun
    }

    override fun onEnable() {
        instance = this
    }
}
