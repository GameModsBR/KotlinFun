package example.kotlinfun.bungee

import br.com.gamemods.kotlinfun.bungee.AutoDataCompanion
import br.com.gamemods.kotlinfun.bungee.PlayerData
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * This data will be associated to the player while he is connected to the proxy
 */
class SessionData(player: ProxiedPlayer) : PlayerData(player) {
    companion object : AutoDataCompanion<SessionData>(ExamplePlugin.instance, ::SessionData)

}
