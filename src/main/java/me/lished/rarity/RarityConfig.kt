package me.lished.rarity

import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class RarityConfig(private val plugin: RarityPlugin) {

    data class Rarity(val id: String, val items: List<String>, val display: String)

    private val rarities = mutableMapOf<String, Rarity>()

    fun loadRarities() {
        val configFile = File(plugin.dataFolder, "config.yml")

        if (!configFile.exists()) {
            plugin.logger.warning("Config file not found!")
            return
        }

        val config = YamlConfiguration.loadConfiguration(configFile)

        config.getConfigurationSection("rarities")?.getKeys(false)?.forEach { rarityId ->
            val rarityPath = "rarities.$rarityId"
            val items = config.getStringList("$rarityPath.items")
            val display = config.getString("$rarityPath.display")

            if (display != null) {
                rarities[rarityId] = Rarity(rarityId, items, ChatColor.translateAlternateColorCodes('&', display))
            } else {
                plugin.logger.warning("Missing data for rarity $rarityId")
            }
        }

        plugin.logger.info("Rarities loaded successfully.")
    }

    fun getRarity(itemId: String): Rarity? {
        return rarities.values.find { it.items.contains(itemId) }
    }
}
