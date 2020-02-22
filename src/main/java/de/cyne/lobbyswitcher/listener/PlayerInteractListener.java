package de.cyne.lobbyswitcher.listener;

import de.cyne.lobbyswitcher.LobbySwitcher;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();

        if (e.getAction() == Action.RIGHT_CLICK_AIR | e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item != null) {
                if (item.getType() == Material.getMaterial(LobbySwitcher.cfg.getString("hotbarItem.material")) && item
                        .getItemMeta().getDisplayName().equals(LobbySwitcher.getString("hotbarItem.displayname"))) {
                    e.setCancelled(true);
                    LobbySwitcher.getInstance().openGUI(p);
                    return;
                }
            }
        }

    }

}
