package com.desiremc.hcf.command.commands.region.modify;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.RegionParser;
import com.desiremc.hcf.parser.StringParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.validator.StringLengthValidator;
import com.desiremc.hcf.validator.UnusedRegionNameValidator;

public class RegionModifyNameCommand extends ValidCommand
{

    public RegionModifyNameCommand()
    {
        super("name", "Change the name of a region.", Rank.ADMIN, new String[] { "region", "name" });

        addParser(new RegionParser(), "region");
        addParser(new StringParser(), "name");

        addValidator(new UnusedRegionNameValidator(), "name");
        addValidator(new StringLengthValidator(1, DesireCore.getConfigHandler().getInteger("regions.max-name")), "name");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Region r = (Region) args[0];
        String name = (String) args[1];
        String oldName = r.getName();

        if (name.equals(oldName))
        {
            sender.sendMessage(LANG.getString("region.same_name"));
            return;
        }

        r.setName(name);
        RegionHandler.getInstance().save(r);

        LANG.sendRenderMessage(sender, "region.changed_distance", "{change}", "name", "{old}", oldName, "{new}", name);

    }

}