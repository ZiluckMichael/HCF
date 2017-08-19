package me.borawski.hcf.parser;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

public class PlayerSessionParser implements ArgumentParser {

    @Override
    public Session parseArgument(CommandSender sender, String label, String arg) {
        Player parsedToPlayer = new PlayerParser().parseArgument(sender, label, arg);

        if (parsedToPlayer == null) {
            return null;
        }

        Session s = SessionHandler.getSession(parsedToPlayer);

        if (s == null) {
            LANG.sendString(sender, "player-not-found");
            return null;
        }

        return s;
    }

}
