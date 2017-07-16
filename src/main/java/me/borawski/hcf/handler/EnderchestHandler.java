package me.borawski.hcf.handler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.borawski.hcf.Core;
import me.borawski.hcf.MscAchievements;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.Utils;

public class EnderchestHandler implements Listener {

    public EnderchestHandler() {
        Bukkit.getPluginManager().registerEvents(this, Core.getInstance());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                if (Utils.enderchestDisabled == true) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("EnderChest are disabled!");
                } else {
                    Session s = SessionHandler.getSession(event.getPlayer());
                    if (!s.hasAchievement(MscAchievements.FIRST_ENDERCHEST_OPEN.getId())) {
                        s.awardAchievement(MscAchievements.FIRST_ENDERCHEST_OPEN, true);
                    }

                    event.setCancelled(false);
                }
            }
        }
    }
}