package example.kotlinfun.bungee

import br.com.gamemods.kotlinfun.bungee.*
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.event.ServerSwitchEvent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * This is a complete example of party plugin running on BungeeCord with KotlinFun
 */

/**
 * Stores current party and received parties invitations
 */
class PartyData(player: ProxiedPlayer, var party: Party?) : PlayerData(player) {
    constructor(player: ProxiedPlayer) : this(player, null)
    companion object : DataCompanion<PartyData>(::PartyData)

    val invites : MutableSet<Party> = mutableSetOf()
}

/**
 * Handles the following commands:
 * /party kick PlayerName
 * /party leave
 * /party PlayerName
 */
class PartyCommand : Command("party") {
    override fun execute(sender: CommandSender, vararg args: String) {
        if(sender !is ProxiedPlayer) {
            /**
             * It's kinda tricky to handle the multiple ways that BungeeCord gives us to create messages...
             * This will be improved soon to remove the fromLegacy() from the end and the pointeer (*) from the beginning
             *
             * @todo Improve the chat api
             */
            sender.sendMessage(*("Party> ".darkRed() + "Only players can execute this command".red()).fromLegacy())
            return
        }

        if(args.size == 2) {
            if(args[0].toLowerCase() == "kick") {
                /**
                 * It's very easy to access the party data
                 *
                 * @todo A companion data that guarantees non-null value on PartyData.get()
                 */
                val party = PartyData[sender]?.party
                if(party == null) {
                    sender.sendMessage(*("Party> ".darkRed() + "You are not in a party".red()).fromLegacy())
                    return
                }

                val other = party[args[1]]
                if(other == null) {
                    sender.sendMessage(*("Party> ".darkRed() + "${args[1]} is not the party".red()).fromLegacy())
                    return
                }

                if(other == sender) {
                    party.remove(other)
                    return
                }

                if(party.leader != sender) {
                    sender.sendMessage(*("Party> ".darkBlue() + " You are not the party leader, asking ${party.leader} to do that for you...".gray()).fromLegacy())
                    party.leader.sendMessage(*("Party> ".darkBlue()+" ${sender.name} wants to kick ${other.name}, type '/party kick ${other.name}' if you agree".gray()).fromLegacy())
                    return
                }

                party.remove(other)
                /**
                 * This will broadcast the message to everybody in the party
                 */
                party.send(*("Party> ".darkBlue()+" ${other.name} has been kicked from the party".gray()).fromLegacy())
                return
            }

            usage(sender)
            return

        } else if(args.size == 1) {
            if(args[0].toLowerCase() == "leave") {
                val party = PartyData[sender]?.party
                if(party == null) {
                    sender.sendMessage(*("Party> ".darkRed() + "You are not in a party".red()).fromLegacy())
                    return
                }

                party.remove(sender)
                return
            }

            val other = ProxyServer.getInstance().getPlayer(args[1])
            if(other == null) {
                sender.sendMessage(*("Party> ".darkRed()+" No player was found with name ${args[1]}".red()).fromLegacy())
                return
            }

            if(other == sender) {
                sender.sendMessage(*("Party> ".darkRed()+"You can't invite yourself to a party".red()).fromLegacy())
                return
            }

            /**
             * Attempts to get the data, if it doesn't exists
             *
             * @todo Simplify this to avoid repetitions
             */
            val data = PartyData[sender] ?: PartyData(sender)
            val otherData = PartyData[other] ?: PartyData(other)
            val party = data.party
            val otherParty = otherData.party

            if(party != null) {
                if(party == otherParty) {
                    sender.sendMessage(*("Party> ".darkRed()+"${other.name} is already in your party".red()).fromLegacy())
                    return
                }
            }

            if(otherParty != null && otherParty in data.invites) {
                otherParty.add(sender)
                return
            }

            for(invite in data.invites) {
                if(invite.leader == other) {
                    invite.add(sender)
                    if(invite.size == 2) {
                        otherParty?.remove(other)
                        PartyData[other] = PartyData(other, invite)
                    }
                    return
                }
            }

            if(party != null && party in otherData.invites || otherData.invites.any { it.leader == sender }) {
                sender.sendMessage(*("Party> ".darkRed()+"${other.name} is already invited to your party".red()).fromLegacy())
                return
            }

            val invite = party ?: Party(sender, emptyList())
            otherData.invites.add(invite)
            ProxyServer.getInstance().scheduler.schedule(ExamplePlugin.instance, { otherData.invites.remove(invite) }, 60, TimeUnit.SECONDS)
            other.sendMessage(*("Party> ".darkBlue()+"${sender.name} -> Do you want to join my party? Type: /party ${sender.name}".gray()).fromLegacy())
            sender.sendMessage(*("Party> ".darkBlue()+"${other.name} has been invited to your party".gray()).fromLegacy())
            return
        }

        usage(sender)
    }

    fun usage(sender: CommandSender) {
        sender.sendMessage("Type '/party leave' to leave, '/party kick player' to kick or '/party <player-name>' to invite".component())
    }
}

class Party(leader: ProxiedPlayer, iterable: Iterable<ProxiedPlayer>) : MutableSet<ProxiedPlayer> {
    var disbanded = false
        private set

    var leader : ProxiedPlayer = leader
        set(player) {
            field = player
            PartyData[player] = PartyData(player, this)
        }

    private val members: MutableSet<ProxiedPlayer> = iterable.toMutableSet()
    override val size: Int get() = if(disbanded) 0 else members.size + 1

    fun send(vararg messages: BaseComponent, position: ChatMessageType = ChatMessageType.SYSTEM, filter: ((ProxiedPlayer) -> Boolean)? = null) {
        for(player in this) {
            if(filter?.invoke(player) ?: true) {
                player.sendMessage(position, *messages)
            }
        }
    }

    fun send(message: BaseComponent, position: ChatMessageType = ChatMessageType.SYSTEM, filter: ((ProxiedPlayer) -> Boolean)? = null) {
        for(player in this) {
            if(filter?.invoke(player) ?: true) {
                player.sendMessage(position, message)
            }
        }
    }

    fun send(vararg messages: String, position: ChatMessageType = ChatMessageType.SYSTEM, filter: ((ProxiedPlayer) -> Boolean)? = null) {
        val components = messages.map(String::fromLegacy)
        for(player in this) {
            if(filter?.invoke(player) ?: true) {
                for(message in components) {
                    player.sendMessage(position, *message)
                }
            }
        }
    }

    fun send(message: String, position: ChatMessageType = ChatMessageType.SYSTEM, filter: ((ProxiedPlayer) -> Boolean)? = null) {
        val components = message.fromLegacy()
        for(player in this) {
            if(filter?.invoke(player) ?: true) {
                player.sendMessage(position, *components)
            }
        }
    }

    operator fun get(name: String) = find { it.name.equals(name, true) }
    operator fun get(uniqueId: UUID) = find { it.uniqueId == uniqueId }
    operator fun contains(name: String) = get(name) != null
    operator fun contains(uniqueId: UUID) = get(uniqueId) != null

    override fun isEmpty() = disbanded
    override fun contains(element: ProxiedPlayer) = !disbanded && (leader == element || members.contains(element))
    override fun containsAll(elements: Collection<ProxiedPlayer>) = !disbanded && (leader in elements && members.containsAll(elements))
    override fun iterator(): MutableIterator<ProxiedPlayer> {
        if(disbanded)
            return object : MutableIterator<ProxiedPlayer> by mutableListOf<ProxiedPlayer>().iterator() {}

        return object : MutableIterator<ProxiedPlayer> {
            private var nextIsLeader = true
            private var last : ProxiedPlayer? = null
            private var base : MutableIterator<ProxiedPlayer>? = null

            override fun hasNext() = if(disbanded) false else if(nextIsLeader) true else base!!.hasNext()

            override fun next(): ProxiedPlayer = if(disbanded) throw NoSuchElementException("The party has been disbanded") else
                if(nextIsLeader) {
                    nextIsLeader = false
                    last = leader
                    leader
                }
                else {
                    var b = base
                    if(b == null) {
                        b = members.iterator()
                        base = b
                    }
                    val next = b.next()
                    last = next
                    next
                }


            override fun remove() {
                if(disbanded)
                    throw IllegalStateException("The party has been disbanded")

                val base = base ?: throw IllegalStateException("next() not called")
                val last = last ?: throw IllegalStateException("Already removed")

                if(last == leader) {
                    remove(leader)
                } else {
                    base.remove()
                    left(last)
                    if(members.isEmpty()) {
                        clear()
                    }
                }
            }
        }
    }

    override fun add(element: ProxiedPlayer) = if(element == leader) false else if(members.add(element)){
        if(disbanded)
            throw IllegalStateException("The party has been disbanded")

        PartyData[element]?.party?.remove(element)
        PartyData[element] = PartyData(element, this)
        send("Party>".darkBlue() + " ${element.displayName} has joined the party".gray())
        true
    } else false

    override fun addAll(elements: Collection<ProxiedPlayer>) : Boolean {
        if(disbanded)
            throw IllegalStateException("The party has been disbanded")

        var modified = false
        for(element in elements)
            modified = add(element)
        return modified
    }

    override fun clear() {
        if(disbanded)
            return

        disbanded = true
        send("Party>".darkBlue() + " The party has been disbanded".gray())

        PartyData[leader] = null
        for(member in members) {
            PartyData[member] = null
        }
        members.clear()
    }

    private fun left(player: ProxiedPlayer) {
        /**
         * This will remove the PartyData from the player
         */
        PartyData[player] = null
        send("Party>".darkBlue() + " ${player.displayName} has left the party".gray())
    }

    override fun remove(element: ProxiedPlayer): Boolean {
        if(disbanded)
            return false

        if(element != leader) {
            if(members.remove(element)) {
                left(element)
                if(members.isEmpty()) {
                    members.add(element)
                    clear()
                }
                return true
            }
            return false
        }

        left(element)
        if(members.size <= 1) {
            clear()
            return true
        }

        leader = members.toList()[Random().nextInt(members.size)]
        members.remove(leader)

        send("Party>".darkBlue() +" ${leader.displayName} is now the leader of the party".gray())
        return true
    }

    override fun removeAll(elements: Collection<ProxiedPlayer>): Boolean {
        val removeLeader = leader in elements
        var removed = false
        for(player in if(removeLeader) elements - leader else elements) {
            removed = removed || remove(player)
        }
        if(removeLeader) {
            removed = removed || remove(leader)
        }
        return removed
    }

    override fun retainAll(elements: Collection<ProxiedPlayer>): Boolean {
        var removed = false
        val iter = iterator()
        iter.next() // Skip the leader
        while (iter.hasNext()) {
            if(iter.next() !in elements) {
                iter.remove()
                removed = true
            }
        }

        if(members.isEmpty()) {
            clear()
            return true
        }

        val leader = leader
        if(leader !in elements) {
            remove(leader)
            return true
        }

        return removed
    }
}

object PartyListener : Listener {
    init {
        ExamplePlugin.instance.register(this)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onServerConnect(event: ServerConnectEvent) {
        val player = event.player
        val party = PartyData[player]?.party ?: return
        if(party.leader.server.info != event.target && player != party.leader && player in party) {
            player.sendMessage(*("Party> ".darkRed()+"You can't go to an other server alone because you are in a party".red()).fromLegacy())
            if(player.server != party.leader.server)
                event.target = party.leader.server.info
            else
                event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDisconnect(event: PlayerDisconnectEvent) {
        PartyData[event.player]?.party?.remove(event.player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerSwitch(event: ServerSwitchEvent) {
        val player = event.player
        val party = PartyData[player]?.party ?: return
        if(party.leader == player) {
            val iter = party.iterator()
            iter.next()
            val current = player.server
            val info = current.info
            for(other in iter){
                if(other.server != current)
                    other.connect(info)
            }
        }
    }
}

/**
 * @todo Include this
 */
fun Listener.unregister() = ProxyServer.getInstance().pluginManager.unregisterListener(this)
