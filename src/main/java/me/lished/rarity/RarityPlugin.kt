package me.lished.rarity

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class RarityPlugin : JavaPlugin() {

    data class Rarity(val id: String, val items: List<String>, val display: String)

    private val rarities = mutableMapOf<String, Rarity>()

    override fun onEnable() {
        loadRarities()
    }

    private fun loadRarities() {
        val configFile = File(dataFolder, "rarities.yml")

        if (!configFile.exists()) {
            logger.warning("Config file not found!")
            return
        }

        val config = YamlConfiguration.loadConfiguration(configFile)

        config.getConfigurationSection("rarities")?.getKeys(false)?.forEach { rarityId ->
            val rarityPath = "rarities.$rarityId"
            val items = config.getStringList("$rarityPath.items")
            val display = config.getString("$rarityPath.display")

            logger.info(rarityPath + rarityId + items + display)
            logger.info("cacao")

            if (display != null) {
                rarities[rarityId] = Rarity(rarityId, items, ChatColor.translateAlternateColorCodes('&', display))
                logger.info("Loaded rarity $rarityId with items: $items")
            } else {
                logger.warning("Missing display for rarity $rarityId")
            }
        }

        logger.info("Rarities loaded successfully.")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name.equals("rarity", ignoreCase = true)) {
            if (args.isEmpty()) {
                sender.sendMessage(ChatColor.RED.toString() + "Usage: /rarity <item>")
                return true
            }

            val itemId = args[0].lowercase()

            val rarity = rarities.values.find { it.items.contains(itemId) }
            logger.info(rarities.values.toString())
            if (rarity != null) {
                sender.sendMessage("${ChatColor.GREEN}${itemId.capitalize()} is ${rarity.display}")
            } else {
                sender.sendMessage("${ChatColor.RED}Rarity not found for $itemId")
            }

            return true
        }
        return false
    }
}
