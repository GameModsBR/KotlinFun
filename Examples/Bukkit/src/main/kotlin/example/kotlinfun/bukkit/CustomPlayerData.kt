package example.kotlinfun.bukkit

import br.com.gamemods.kotlinfun.bukkit.*
import br.com.gamemods.kotlinfun.str
import org.bukkit.OfflinePlayer

/**
 * This holds temporary player data, it's not persisted to the disk but can be quickly accessed from the player object,
 */
class CustomPlayerData(player: OfflinePlayer) : PlayerData(player) {
    /**
     * The super class of this companion object will setup everything automatically, that's why we need to provide
     * all that stuff. Please note that this constructor will be improved in the next version.
     */
    companion object : AutoDataCompanion<CustomPlayerData>(SamplePlugin.instance, "session", CustomPlayerData::class.java, ::CustomPlayerData)

    // We can do whatever we want here
    var observingSpawns = false
    var observingDeaths = false
    var observingDamages = false
        set(value) {
            field = value
            player?.sendMessage("Damage>".gold() + if(value) "You'll now be notified about entity damages".red() else " You'll no longer see messages about damage".green())
        }

    var silly = false
    fun tellHim() {
        /**
         * Notice that we have constructed this class with an OfflinePlayer.
         * If the player is online than it will be returned below as a Player object, if it's not then it will be null
         */
        player?.sendMessage(
                /**
                 * Notice that we have encapsulated the if..else with parentheses to call str() in the end.
                 * That's because String.red() and String.green() will return a formatter object called Colorized,
                 * that would cause an issue here because Colorized is not an string, all we have to do is to call
                 * .str() or .toString() to transform it, but, we had two calls here, so to avoid repetition I
                 * wrapped the if statement and casted the result.
                 *
                 * An other way to workaround this would be to add an empty string, for example:
                 *   if(silly) "You are silly!".red()+"" else "You are cool :)".green()+""
                 */
                ( if(silly) "You are silly!".red() else "You are cool :)".green() ).str()
        )
    }
}
