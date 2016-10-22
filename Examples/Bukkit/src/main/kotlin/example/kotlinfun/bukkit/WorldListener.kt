package example.kotlinfun.bukkit

import br.com.gamemods.kotlinfun.bukkit.DataCompanion
import br.com.gamemods.kotlinfun.bukkit.PlayerData
import br.com.gamemods.kotlinfun.bukkit.darkGray
import br.com.gamemods.kotlinfun.bukkit.gray
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

class WorldListener : Listener {
    @EventHandler
    fun spawn(event: CreatureSpawnEvent) {
        val msg = "Spawn> ".darkGray() + "${event.entity.name} has spawned by ${event.spawnReason} in ${event.location}".gray()

        /**
         * We can easily iterate through all the session data from the players online
         */
        for(data in CustomPlayerData) {
            if(data.observingSpawns) {
                data.player?.sendMessage(msg)
            }
        }
    }

    @EventHandler
    fun death(event: EntityDeathEvent) {
        /**
         * Notice that we have a string "nothing" inside a string, Kotlin is fun
         */
        val msg = "Death> ".darkGray() + "${event.entity.name} has died by ${event.entity.lastDamageCause?.cause?.name ?: "nothing"} in ${event.entity.location}".gray()
        for(data in CustomPlayerData) {
            if(data.observingDeaths) {
                data.player?.sendMessage(msg)
            }
        }
    }

    @EventHandler
    fun damage(event: EntityDamageEvent) {
        val msg = "Damage> ".darkGray() + "${event.entity.name} has been damaged by ${event.cause} in ${event.entity.location}".gray()
        for(data in CustomPlayerData) {
            if(data.observingDeaths) {
                data.player?.sendMessage(msg)
            }
        }
    }
}

/**
 * This will be included in the next version
 */
operator fun <D: PlayerData> DataCompanion<D>.iterator() : Iterator<D> =
        Bukkit.getOnlinePlayers().map { get(it) }.filterNotNull().iterator()
