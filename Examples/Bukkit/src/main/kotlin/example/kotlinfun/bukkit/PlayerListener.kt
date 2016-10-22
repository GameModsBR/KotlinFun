package example.kotlinfun.bukkit

import br.com.gamemods.kotlinfun.bukkit.*
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class PlayerListener(val plugin: SamplePlugin) : Listener {
    @EventHandler
    fun placeMine(event: BlockPlaceEvent) {
        if(event.block.type != Material.WOOD_PLATE)
            return

        /**
         * You can set metadata to Metadatable directly, no need to create a FixedMetadataValue
         */
        event.block.setMetadata("mine", plugin, event.player.name)

        /**
         * Notice how the formatted message was created, you can swap the bold() and darkRed() calls if you want,
         * the invocation order is not important.
         *
         * If the red message wasn't red it would inherit the bold and dark red style
         */
        event.player.sendMessage("Mine> ".bold().darkRed() + "The bomb has been placed!".red())
    }

    @EventHandler
    fun activateMine(event: PlayerInteractEvent) {
        if(event.action != Action.PHYSICAL)
            return

        val block = event.clickedBlock ?: return

        /**
         * It's super easy to get or remove a metadata now
         */
        val owner = block.removeMetadata("mine", plugin, String::class.java) ?: return
        if(block.world.createExplosion(block.location, 4f))
            event.player.sendMessage("Mine> ".darkRed().bold() + "BOOOM!".underline().red() + " You've stepped on $owner's mine!".red())
    }
}