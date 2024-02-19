package me.lished.rarity

import me.lished.rarity.listeners.RarityListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class RarityPlugin : JavaPlugin() {

    data class Rarity(val id: String, val items: List<String>, val display: String)

    private val rarities = mutableMapOf<String, Rarity>()

    override fun onEnable() {
        loadRarities()
        Bukkit.getPluginManager().registerEvents(RarityListener(), this)
    }

    private fun loadRarities() {
        val configFile = File(dataFolder, "rarities.yml")

        if (!configFile.exists()) {
            logger.warning("Config file not found!")
            return
        }

        val config = YamlConfiguration.loadConfiguration(configFile)

        val raritiesSection = config.get("rarities")
        if (raritiesSection is List<*>) {
            raritiesSection.forEach { rarityData ->
                if (rarityData is Map<*, *>) {
                    val rarityId = rarityData["id"] as? String
                    val items = rarityData["items"] as? List<String>
                    val display = rarityData["display"] as? String

                    if (rarityId != null && display != null) {
                        rarities[rarityId] = Rarity(rarityId, items ?: emptyList(), ChatColor.translateAlternateColorCodes('&', display))
                    } else {
                        logger.warning("Missing data for rarity: $rarityData")
                    }
                } else {
                    logger.warning("Invalid rarity data: $rarityData")
                }
            }
        } else {
            logger.warning("No 'rarities' section found or it's not a list")
        }

        logger.info("Rarities loaded successfully.")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name.equals("rarity", ignoreCase = true)) {
            val player = sender as Player
            val itemId: String
            if(args.isEmpty()) {
                player.sendMessage(player.inventory.itemInMainHand.rarity.name)
                itemId = player.inventory.itemInMainHand.type.name.toLowerCase().replace("_", " ")
            } else {
                itemId = args[0].lowercase()
            }

            val rarity = rarities.values.find { it.items.contains(itemId) }

            if (rarity != null) {
                player.sendMessage("$itemId is ${rarity.display}")
            } else {
                player.sendMessage("Rarity not found for $itemId")
            }

            return true
        }
        return false
    }
}
