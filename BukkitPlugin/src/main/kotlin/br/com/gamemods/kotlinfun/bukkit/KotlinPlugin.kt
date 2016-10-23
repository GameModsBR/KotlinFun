package br.com.gamemods.kotlinfun.bukkit

import com.avaje.ebean.EbeanServer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStream

open class KotlinPlugin : JavaPlugin() {
    override fun getResource(filename: String): InputStream {
        return super.getResource(filename)
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, vararg args: String): MutableList<String> {
        return super.onTabComplete(sender, command, alias, args)
    }

    override fun getDatabase(): EbeanServer {
        return super.getDatabase() ?: throw IllegalStateException("Plugin has changed database to true at runtime")
    }

    override fun getDatabaseClasses(): MutableList<Class<*>> {
        return super.getDatabaseClasses()
    }

    override fun getFile(): File {
        return super.getFile()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, vararg args: String): Boolean {
        return false
    }

    override fun getConfig(): FileConfiguration {
        return super.getConfig()
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String): ChunkGenerator {
        return super.getDefaultWorldGenerator(worldName, id)
    }

    override fun getCommand(name: String?): PluginCommand {
        return super.getCommand(name)
    }
}
