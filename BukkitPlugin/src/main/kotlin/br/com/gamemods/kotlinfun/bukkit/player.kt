@file:JvmName("PlayerUtil")
package br.com.gamemods.kotlinfun.bukkit

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import java.lang.ref.WeakReference
import java.util.*

fun <T:PlayerData> Player.setMetadata(data: DataCompanion<T>, value: T) = setMetadata(data.key, data.plugin, value)
fun <T:PlayerData> Player.getMetadata(data: DataCompanion<T>) = getMetadata(data.key, data.plugin, data.type)
fun Player.hasMetadata(data: DataCompanion<*>) = hasMetadata(data.key, data.plugin, data.type)
fun Player.removeMetadata(data: DataCompanion<*>) = removeMetadata(data.key, data.plugin, data.type)
operator fun Player.contains(data: DataCompanion<*>) = hasMetadata(data.key, data.plugin, data.type)

open class DataCompanion<D : PlayerData>(val plugin: Plugin, val key: String, val type: Class<D>, val factory: (Player) -> D) {
    operator fun get(uniqueId: UUID): D? {
        return get(Bukkit.getPlayer(uniqueId) ?: return null)
    }

    operator fun get(player: Player) : D? {
        var data = player.getMetadata(key, plugin, type)
        if(data != null)
            return data

        data = createMissing(player)
        player.setMetadata(key, plugin, data)
        return data
    }

    operator fun get(sender: CommandSender?): D? {
        return get(sender as? Player ?: return null)
    }

    operator fun set(player: Player, data: D?) {
        if(data != null)
            player.setMetadata(key, plugin, data)
        else
            player.removeMetadata(key, plugin, type)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Player names are not guaranteed to be unique", ReplaceWith("get(uniqueId:UUID)"))
    operator fun get(name: String) : D? {
        return get(Bukkit.getPlayer(name) ?: return null)
    }

    operator fun contains(player: Player) = player.hasMetadata(key, plugin, type)

    operator fun iterator() : Iterator<D> =  Bukkit.getOnlinePlayers().map { get(it) }.filterNotNull().iterator()

    protected open fun createMissing(player: Player) = factory.invoke(player)
}

open class AutoDataCompanion<D : PlayerData>(plugin: Plugin, key: String, type: Class<D>, factory: (Player) -> D) :
        DataCompanion<D>(plugin, key, type, factory), Listener {

    init {
        plugin.register(this)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PlayerJoinEvent) = event.player.setMetadata(key, plugin, factory.invoke(event.player))

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(event: PlayerQuitEvent) = event.player.removeMetadata(key, plugin)
}

open class PlayerData(val uniqueId: UUID, val name : String) {
    private var playerRef : WeakReference<Player?> = WeakReference(null)

    var player : Player?
        get() {
            var player = playerRef.get()
            if(player != null) {
                return if(player.isOnline) player else null
            }

            player = Bukkit.getPlayer(uniqueId)
            if (player != null)
                playerRef = WeakReference(player)
            return player
        }
        private set(value) {
            playerRef = WeakReference(value)
        }

    constructor(player: OfflinePlayer) : this(player.uniqueId, player.name) {
        this.player = player.player
    }
}
