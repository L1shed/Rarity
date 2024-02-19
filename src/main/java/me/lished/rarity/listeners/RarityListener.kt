package me.lished.rarity.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class RarityListener : Listener {
    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val player = e.whoClicked

        if (e.clickedInventory == player.inventory) {
            val item = e.currentItem

            if (item != null && item.type != Material.AIR && item.lore() == null) {
                item.lore(listOf(Component.text("§r "), Component.text(item.rarity.name, TextColor.color(item.rarity.color)).decorate(TextDecoration.BOLD)))
            }
        }
    }
}