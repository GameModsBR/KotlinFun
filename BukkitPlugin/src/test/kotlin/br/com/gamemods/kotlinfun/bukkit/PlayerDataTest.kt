package br.com.gamemods.kotlinfun.bukkit

import br.com.gamemods.kotlinfun.str
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class TestPlugin : JavaPlugin()

val testPlugin = TestPlugin()

class TestData(player: OfflinePlayer) : PlayerData(player) {
    companion object : AutoDataCompanion<TestData>(testPlugin, "Testing", TestData::class.java, ::TestData)
}

class MyNicePlugin : JavaPlugin() {
    companion object {
        lateinit var instance : MyNicePlugin
    }

    override fun onEnable() {
        instance = this
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        sender as? Player ?: return false
        if(command.name == "silly") {
            val data = SillyPlayerData[sender] ?: return false
            data.silly = !data.silly
            data.tellHim()
            return true
        }
        else {
            return false
        }
    }
}

class SillyPlayerData(player: OfflinePlayer) : PlayerData(player) {
    companion object : AutoDataCompanion<SillyPlayerData>(MyNicePlugin.instance, "SillyPlayer",
            SillyPlayerData::class.java, ::SillyPlayerData
    )

    var silly = false

    fun tellHim() = player?.sendMessage((if(silly) "You are silly!".red() else "You are not silly :)".green()).str())
}

fun test() {
    val data : TestData? = TestData[UUID.randomUUID()]
    val player = Bukkit.getOfflinePlayer(UUID.randomUUID())?.player ?: return
    TestData[player] = data
    TestData[player] = null

    if(player !in TestData)
        return

    if(TestData !in player)
        return
}
