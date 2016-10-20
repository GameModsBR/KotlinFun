package br.com.gamemods.kotlinfun.bungee

import com.google.common.collect.MapMaker
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.ConcurrentMap

open class DataCompanion<D : PlayerData>(val factory: (ProxiedPlayer) -> D) {
    private val idMap : ConcurrentMap<UUID, D> = MapMaker().weakValues().makeMap()

    operator fun get(uniqueId: UUID): D? {
        return idMap[uniqueId]
    }

    operator fun get(player: ProxiedPlayer) : D? {
        var data = get(player.uniqueId)
        if(data != null)
            return data

        data = createMissing(player)
        set(player, data)
        return data
    }

    operator fun set(player: ProxiedPlayer, data: D?) {
        idMap[player.uniqueId] = data
    }

    @Suppress("DEPRECATION")
    @Deprecated("Player names are not guaranteed to be unique", ReplaceWith("get(uniqueId:UUID)"))
    operator fun get(name: String) : D? {
        return idMap.values.find { it.name == name }
    }

    operator fun contains(player: ProxiedPlayer) = player.uniqueId in idMap

    protected open fun createMissing(player: ProxiedPlayer) = factory.invoke(player)

    private fun createAndSet(player: ProxiedPlayer) : D {
        val data = factory.invoke(player)
        set(player, data)
        return data
    }
}

open class AutoDataCompanion<D : PlayerData>(plugin: Plugin, factory: (ProxiedPlayer) -> D) :
        DataCompanion<D>(factory), Listener {

    init {
        plugin.register(this)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onJoin(event: PostLoginEvent) = set(event.player, factory.invoke(event.player))

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(event: PlayerDisconnectEvent) = set(event.player, null)
}


open class PlayerData(val uniqueId: UUID, val name : String) {
    private var playerRef : WeakReference<ProxiedPlayer?> = WeakReference(null)

    var player : ProxiedPlayer?
        get() {
            var player = playerRef.get()
            if(player != null) {
                return if(player.isConnected) player else null
            }

            player = ProxyServer.getInstance().getPlayer(uniqueId)
            if (player != null)
                playerRef = WeakReference(player)
            return player
        }
        private set(value) {
            playerRef = WeakReference(value)
        }

    constructor(player: ProxiedPlayer) : this(player.uniqueId, player.name) {
        this.player = player
    }
}
