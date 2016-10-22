package example.kotlinfun.bukkit

import br.com.gamemods.kotlinfun.bukkit.DataCompanion
import br.com.gamemods.kotlinfun.bukkit.PlayerData
import br.com.gamemods.kotlinfun.bukkit.register
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * Just a simple example
 */
class SamplePlugin : JavaPlugin() {
    companion object {
        lateinit var instance : SamplePlugin
    }

    override fun onEnable() {
        instance = this

        /**
         * You can register your listeners with a simple call
         */
        register(PlayerListener(this), WorldListener())

        getCommand("silly")?.setExecutor { sender, command, label, strings ->
            /**
             * We can get and work the player data easily
             */
            CustomPlayerData[sender]?.run { silly = !silly ; tellHim() ; true } ?: false
        }

        getCommand("ts")?.setExecutor { sender, command, label, strings ->
            CustomPlayerData[sender]?.run { observingSpawns = !observingSpawns; true } ?: false
        }

        getCommand("tdt")?.setExecutor { sender, command, label, strings ->
            CustomPlayerData[sender]?.run { observingDeaths = !observingDeaths; true } ?: false
        }

        getCommand("tda")?.setExecutor { sender, command, label, strings ->
            CustomPlayerData[sender]?.run { observingDamages = !observingDamages; true } ?: false
        }
    }
}

/**
 * This will be added in the next version
 */
operator fun <D:PlayerData> DataCompanion<D>.get(sender: CommandSender?): D? {
    return get(sender as? Player ?: return null)
}
