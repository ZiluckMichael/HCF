package com.desiremc.hcf.api;

import com.desiremc.hcf.HCFCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;

public class LivesAPI
{

    private final static LangHandler LANG = HCFCore.getLangHandler();

    public static void takeLives(CommandSender sender, Player target, Integer amount)
    {
        String strAmount = Integer.toString(amount);
        String targetName = target.getDisplayName();
        String senderName = ((Player) sender).getDisplayName();

        HCFSession session = HCFSessionHandler.getHCFSession(target);
        session.takeLives(amount);

        LANG.sendRenderMessage(sender, "lives.remove", "{amount}", strAmount, "{player}", targetName);

        LANG.sendRenderMessage(sender, "lives.lost", "{amount}", Integer.toString(amount), "{sender}", senderName);
    }

    public static void addLives(CommandSender sender, Player target, Integer amount)
    {
        String strAmount = Integer.toString(amount);
        String targetName = target.getDisplayName();
        String senderName = ((Player) sender).getDisplayName();

        HCFSession session = HCFSessionHandler.getHCFSession(target);
        session.addLives(amount);

        LANG.sendRenderMessage(sender, "lives.add", "{amount}", strAmount, "{player}", targetName);
        LANG.sendRenderMessage(sender, "lives.recieved", "{amount}", strAmount, "{sender}", senderName);
    }
}
