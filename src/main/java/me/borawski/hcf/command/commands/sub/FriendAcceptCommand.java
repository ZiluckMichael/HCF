package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.FriendsAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.PlayerSessionParser;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.validator.PlayerNotFriendsValidator;
import me.borawski.hcf.validator.PlayerSenderValidator;
import me.borawski.hcf.validator.SenderFriendRequestValidator;

public class FriendAcceptCommand extends ValidCommand {

    public FriendAcceptCommand() {
        super("accept", "Accept a friend request.", Rank.GUEST, new String[] { "target" }, "confirm");
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerSenderValidator());
        addValidator(new PlayerNotFriendsValidator(), "target");
        addValidator(new SenderFriendRequestValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        Session target = (Session) args[0];

        FriendsAPI.acceptRequest((Player) sender, target);
    }

}
