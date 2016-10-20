package br.com.gamemods.kotlinfun.bukkit

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class TestPlugin : JavaPlugin()

val testPlugin = TestPlugin()

class TestData(player: OfflinePlayer) : PlayerData(player) {
    companion object : AutoDataCompanion<TestData>(testPlugin, "Testing", TestData::class.java, ::TestData)
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
