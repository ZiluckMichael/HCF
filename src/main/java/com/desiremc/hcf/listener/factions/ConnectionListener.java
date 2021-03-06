package com.desiremc.hcf.listener.factions;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.commands.factions.FactionHomeCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.session.faction.FactionSetting;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class ConnectionListener implements Listener
{

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        // grab their session
        FSession session = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());

        // check if they have a faction before proceeding
        if (session.hasFaction())
        {
            // doing this because it will be used a lot and will make our lives easier.
            Faction faction = session.getFaction();

            // go through all online players and let them know one of their members connected if they have the setting enabled
            for (FSession online : faction.getOnlineMembers())
            {
                if (online != session && online.hasSetting(FactionSetting.CONNECTION_MESSAGES))
                {
                    DesireHCF.getLangHandler().sendRenderMessage(online.getSession(), "faction.member_connect", true, false,
                            "{player}", session.getName());
                }
            }

            // have a delay before sending the player's announcements so they are sure to see them.
            Bukkit.getScheduler().runTaskLater(DesireHCF.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    // grab all the pending announcements for the player
                    List<String> announcements = faction.getAnnouncements(session);

                    // if they have no announcements, don't bother looping and saving.
                    if (announcements != null && announcements.size() > 0)
                    {
                        // loop through any pending faction announcements this player has and send it to them.
                        for (String announcement : announcements)
                        {
                            session.sendMessage(announcement);
                        }

                        // announcements should only be displayed once, so clear out this player's announcements.
                        faction.clearAnnouncements(session);
                        faction.save();
                    }
                }
            }, 33L);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        // grab their session
        FSession fSession = FSessionHandler.getOnlineFSession(event.getPlayer().getUniqueId());

        // check if the quitting player is stuck
        if (FactionHandler.isStuck(fSession))
        {
            // if they are stuck, let them know that the next time they get on.
            fSession.getFaction().addAnnouncement(fSession, DesireHCF.getLangHandler().renderMessage("factions.stuck.cancelled.quit", false, false));
        }

        // take their claim wand if they have one
        fSession.clearClaimSession();

        // cancel the home task if they have one
        BukkitTask task = FactionHomeCommand.getTeleportTask(event.getPlayer().getUniqueId());
        if (task != null)
        {
            DesireHCF.getLangHandler().sendRenderMessage(event.getPlayer(), "factions.home.cancelled", false, false);
            task.cancel();
        }

        // if they are in a faction, do some clean up
        if (fSession.hasFaction())
        {
            Faction faction = fSession.getFaction();

            // set the last time a player logged off
            faction.setLastLogOff(System.currentTimeMillis());

            // go through all online players and let them know one of their members disconnected if they have the setting enabled
            for (FSession online : faction.getOnlineMembers())
            {
                if (online.hasSetting(FactionSetting.CONNECTION_MESSAGES))
                {
                    DesireHCF.getLangHandler().sendRenderMessage(online.getSession(), "faction.member_disconnect", true, false,
                            "{player}", fSession.getName());
                }
            }
        }
    }

}
