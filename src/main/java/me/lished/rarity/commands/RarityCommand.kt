package me.lished.rarity.commands

import me.lished.rarity.RarityConfig
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class RarityCommand : CommandExecutor {
    private lateinit var rarityConfig: RarityConfig

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name.equals("rarity", ignoreCase = true)) {
            val itemId = args[0].lowercase()
            val rarity = rarityConfig.getRarity(itemId)

            sender.sendMessage("${itemId.capitalize()} is ${rarity!!.display}")

            return true
        }
        return false
    }

}
