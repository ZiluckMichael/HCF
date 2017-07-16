package me.borawski.hcf.command.commands.sub;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.ChatUtils;
import me.borawski.hcf.util.FriendUtils;
import me.borawski.hcf.util.PlayerUtils;

public class FriendDeclineCommand extends CustomCommand {

    public FriendDeclineCommand() {
        super("decline", "Decline a friend request.", Rank.GUEST, "deny");
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Core.getLangHandler().getString("only-players"));
            return;
        }
        if (args.length == 0) {
            if (!PlayerUtils.hasPlayed(args[0])) {
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "Player not found!");
                return;
            }
            Session session = SessionHandler.getSession((Player) sender);
            Session target = SessionHandler.getSession(PlayerUtils.getUUIDFromName(args[0]));
            if (FriendUtils.isFriends(session, target.getUniqueId())) {
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You're already friends with " + ChatUtils.getNameWithRankColor(target.getUniqueId(), false) + ChatColor.GRAY + "!");
                return;
            }

            if (!FriendUtils.hasRequest(session, target.getUniqueId())) {
                sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You don't have a request from " + ChatUtils.getNameWithRankColor(target.getUniqueId(), false) + ChatColor.GRAY + "!");
                return;
            }

            FriendUtils.denyFriendRequest(session, target.getUniqueId(), true);
            FriendUtils.denyFriendRequest(target, session.getUniqueId(), false);
            sender.sendMessage(Core.getInstance().getPrefix() + ChatColor.GRAY + "You denied " + ChatUtils.getNameWithRankColor(target.getUniqueId(), false) + ChatColor.GRAY + "'s friend invite!");
        }
    }

}