package com.desiremc.hcf.handler;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.utils.cache.Cache;
import com.desiremc.core.utils.cache.RemovalListener;
import com.desiremc.core.utils.cache.RemovalNotification;
import com.desiremc.hcf.DesireHCF;

public class EnderpearlHandler implements Listener
{

    private static int TIMER;

    private Cache<UUID, Long> history;

    public EnderpearlHandler()
    {
        TIMER = DesireHCF.getConfigHandler().getInteger("enderpearl.time");
        history = new Cache<>(TIMER, TimeUnit.SECONDS, new RemovalListener<UUID, Long>()
        {
            @Override
            public void onRemoval(RemovalNotification<UUID, Long> entry)
            {
                Player p = PlayerUtils.getPlayer(entry.getKey());
                if (p != null)
                {
                    if (entry.getCause() != RemovalNotification.Cause.EXPIRE)
                    {
                        return;
                    }
                    DesireHCF.getLangHandler().sendRenderMessage(p, ChatColor.GRAY + "You can now use an enderpearl.", true, false);
                    //EntryRegistry.getInstance().removeValue(p, DesireHCF.getLangHandler().renderMessage("enderpearl.scoreboard", false, false));
                }
            }
        }, DesireHCF.getInstance());

        Bukkit.getScheduler().runTask(DesireHCF.getInstance(), new EnderpearlUpdater());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();

        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        if (e.getItem() != null && e.getItem().getType().equals(Material.ENDER_PEARL))
        {
            UUID uuid = p.getUniqueId();
            Long time = history.get(uuid);

            if (time == null)
            {
                history.put(uuid, System.currentTimeMillis());
            }
            else
            {
                DesireHCF.getLangHandler().sendRenderMessage(p, ChatColor.GRAY + "You can't use an enderpearl yet.", true, false);
                e.setCancelled(true);
            }
        }
    }

    private class EnderpearlUpdater implements Runnable
    {

        @Override
        public void run()
        {
            for (Entry<UUID, Long> entry : history.entrySet())
            {
                Player p = PlayerUtils.getPlayer(entry.getKey());
                if (p != null)
                {
                    //EntryRegistry.getInstance().setValue(p, DesireHCF.getLangHandler().renderMessage("enderpearl.scoreboard", false, false), String.valueOf(TIMER - ((System.currentTimeMillis() - entry.getValue()) / 1000)));
                }
                else
                {
                    history.toRemove(entry.getKey());
                }
            }
            Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), this, 10);
        }

    }

}
