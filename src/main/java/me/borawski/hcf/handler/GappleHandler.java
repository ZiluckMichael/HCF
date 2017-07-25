package me.borawski.hcf.handler;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import me.borawski.hcf.Components;
import me.borawski.hcf.Core;
import me.borawski.hcf.MscAchievements;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.Cooldown;
import me.borawski.hcf.util.Utils;
import me.borawski.hcf.util.Cooldown.CooldownBase;
import me.borawski.hcf.util.Cooldown.Time;

public class GappleHandler implements Listener {

    private final Cooldown cooldown;

    public GappleHandler() {
        (cooldown = Components.getInstance().getCooldown(Components.GAPPLE)).setOnEndSequece(new Consumer<UUID>() {

            @Override
            public void accept(UUID id) {
                Bukkit.getScheduler().runTask(Core.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getPlayer(id).sendMessage(Utils.chat(Core.getInstance().getConfig().getString("gapple_ended")));
                    }
                });
            }
        });
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (e.getItem().getType() == Material.GOLDEN_APPLE && e.getItem().getDurability() == 1) {
            CooldownBase base = cooldown.get(p.getUniqueId());
            if (base == null || Cooldown.getAmountLeft(base) <= 0) {
                Session s = SessionHandler.getSession(e.getPlayer());
                if (!s.hasAchievement("first_gapple")) {
                    s.awardAchievement(MscAchievements.FIRST_GAPPLE, true);
                }
                cooldown.startCooldown(p.getUniqueId(), Cooldown.timeToMillis(Core.getInstance().getConfig().getString("gapple_time")));
            } else {
                e.setCancelled(true);
                String message = Core.getInstance().getConfig().getString("gapple_message");
                long left = Cooldown.getAmountLeft(base);
                Map<Time, Long> times = Cooldown.timeFromMillis(left);
                message = message.replace("<days>", (times.containsKey(Time.DAY) ? times.get(Time.DAY) : 0) + "d");
                message = message.replace("<hours>",
                        (times.containsKey(Time.HOUR) ? times.get(Time.HOUR) : 0) + "h");
                message = message.replace("<minutes>",
                        (times.containsKey(Time.MINUTE) ? times.get(Time.MINUTE) : 0) + "m");
                message = message.replace("<seconds>",
                        (times.containsKey(Time.SECOND) ? times.get(Time.SECOND) : 0) + "s");
                p.sendMessage(Utils.chat(message));
            }
        }
    }

}
