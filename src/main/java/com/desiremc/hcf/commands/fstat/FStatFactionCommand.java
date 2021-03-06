package com.desiremc.hcf.commands.fstat;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

import java.util.List;

public class FStatFactionCommand extends FactionValidCommand
{

    public FStatFactionCommand()
    {
        super("faction", "Shows faction stats", Rank.MODERATOR);

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .build());

    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = (Faction) arguments.get(0).getValue();

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "trophy_points", true, false,
                "{points}", faction.getTrophies());

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "koth_wins", true, false,
                "{koth_wins}", faction.getKothWins());

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "faction", true, false,
                "{faction}", faction.getName());
    }

}
