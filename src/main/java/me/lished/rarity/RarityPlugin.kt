import org.bukkit.ChatColor
import org.bukkit.Material
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

    override fun onDisable() {
        // Plugin shutdown logic
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

            if (items != null && display != null) {
                rarities[rarityId] = Rarity(rarityId, items, ChatColor.translateAlternateColorCodes('&', display))
            } else {
                logger.warning("Missing data for rarity $rarityId")
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