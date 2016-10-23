package example.kotlinfun.bungee

import net.md_5.bungee.api.plugin.Plugin

class ExamplePlugin : Plugin() {
    companion object {
        lateinit var instance : ExamplePlugin
    }

    override fun onEnable() {
        instance = this
    }
}
