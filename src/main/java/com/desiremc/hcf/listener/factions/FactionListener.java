package com.desiremc.hcf.listener.factions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.commands.factions.FactionHomeCommand;
import com.desiremc.hcf.events.faction.FactionLeaveEvent;

public class FactionListener implements Listener
{

    @EventHandler
    public void onFactionLeave(FactionLeaveEvent event)
    {
        // cancel their home task
        BukkitTask task = FactionHomeCommand.getTeleportTask(event.getFSession().getUniqueId());
        if (task != null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(event.getFSession().getSender(), "factions.home.cancelled", true, false);
            task.cancel();
        }
        
        // clean up claim session
        if (event.getFSession().hasClaimSession())
        {
            event.getFSession().clearClaimSession();
        }
    }
    
}
