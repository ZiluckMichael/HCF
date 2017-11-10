package com.desiremc.hcf.commands;

import com.desiremc.hcf.validator.PlayerHasSafeTimeLeft;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;

public class PVPCommand extends ValidCommand
{

    public PVPCommand()
    {
        super("pvp", "Disable your PVP timer.", Rank.GUEST, new String[] {});

        addValidator(new PlayerValidator());
        addValidator(new PlayerHasSafeTimeLeft());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        HCFSession s = HCFSessionHandler.getHCFSession(((Player) sender).getUniqueId());

        s.setSafeTimeLeft(0);
        DesireHCF.getLangHandler().sendString(sender, "pvp.disabled");
        EntryRegistry.getInstance().removeValue(s.getPlayer(), DesireCore.getLangHandler().getStringNoPrefix("pvp.scoreboard"));
    }

}
