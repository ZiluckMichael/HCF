package com.desiremc.hcf.listener;

import java.util.function.Consumer;

import com.desiremc.hcf.HCFCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.ChatUtils;
import com.desiremc.hcf.session.FactionSession;
import com.desiremc.hcf.session.FactionSessionHandler;
import com.desiremc.hcf.util.FactionsUtils;
import com.massivecraft.factions.Faction;

public class ChatListener implements Listener
{

    @EventHandler
    public void chat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        event.setCancelled(true);
        Session s = SessionHandler.getSession(player);
        if (s.isMuted() != null)
        {
            player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            player.sendMessage("");
            ChatUtils.sendCenteredMessage(player, HCFCore.getLangHandler().getPrefix().replace(" ", ""));
            player.sendMessage("");
            ChatUtils.sendCenteredMessage(player, ChatColor.GRAY + "You are muted and " + ChatColor.RED + "CANNOT " + ChatColor.GRAY + "speak!");
            ChatUtils.sendCenteredMessage(player, ChatColor.GRAY + "Visit our rules @ " + ChatColor.YELLOW + "https://desirehcf.net/rules");
            player.sendMessage("");
            player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------------------------");
            return;
        }

        String msg = event.getMessage();

        Faction f = FactionsUtils.getFaction(player);

        String parsedMessage = s.getRank().getId() >= Rank.ADMIN.getId() ? ChatColor.translateAlternateColorCodes('&', msg) : msg;
        System.out.println(player.getName() + ": " + parsedMessage);
        Bukkit.getOnlinePlayers().stream().forEach(new Consumer<Player>()
        {
            @Override
            public void accept(Player players)
            {

                if (f == null)
                {
                    new FancyMessage(s.getRank().getPrefix())
                            .then(player.getName())
                            .tooltip(new String[] {
                                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "NO FACTION"
                            })
                            .then(": ")
                            .then(parsedMessage)
                            .color(s.getRank().getColor())
                            .send(players);
                    return;
                }

                FactionSession fSession = FactionSessionHandler.getFactionSession(f.getTag());

                new FancyMessage(s.getRank().getPrefix())
                        .then(player.getName())
                        .tooltip(new String[] {
                                ChatColor.DARK_RED + "" + ChatColor.BOLD + "FACTION INFO",
                                ChatColor.GRAY + "Name: " + ChatColor.YELLOW + "" + (f != null ? f.getTag() : "NONE"),
                                ChatColor.GRAY + "Members: " + ChatColor.YELLOW + "" + (f != null ? f.getFPlayers().size() : "NONE"),
                                ChatColor.GRAY + "Trophy Points: " + ChatColor.YELLOW + "" + (f != null && fSession != null ? fSession.getTrophies() : "---")
                        })
                        .then(": ")
                        .then(parsedMessage)
                        .color(s.getRank().getColor())
                        .send(players);
            }
        });
    }

}
