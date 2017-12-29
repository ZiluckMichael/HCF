package com.desiremc.hcf.commands.factions;

import java.util.ArrayList;
import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.session.faction.FactionState;
import com.desiremc.hcf.session.faction.FactionType;
import com.desiremc.hcf.validators.ServerHasFactionsValidator;

public class FactionListCommand extends FactionValidCommand
{
    public FactionListCommand()
    {
        super("list", "List all factions.", Rank.GUEST, true, new String[] {});

        addSenderValidator(new ServerHasFactionsValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("page")
                .setParser(new IntegerParser())
                .setOptional()
                .build());

    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        List<Faction> factionList = new ArrayList<>(FactionHandler.getSortedFactions());

        final int height = 9;
        int page = arguments.get(0).hasValue() ? ((Number) arguments.get(0).getValue()).intValue() : 0;
        int pages = (factionList.size() / height) + 1;

        if (page > pages)
        {
            page = pages;
        }
        else if (page < 1)
        {
            page = 1;
        }

        int start = (page - 1) * height;
        int end = start + height;
        if (end > factionList.size())
        {
            end = factionList.size();
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.list.header", false, false, "{pagenumber}", page, "{pagecount}", pages);

        factionList.subList(start, end).stream()
                .filter(faction -> faction.getType() == FactionType.PLAYER)
                .filter(faction -> faction.getState() == FactionState.ACTIVE)
                .forEach(faction ->
                {
                    DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.list.message", false, false, "{faction}", faction.getName(),
                            "{online}", faction.getOnlineMembers().size(), "{members}", faction.getMemberSize(), "{dtr}", faction.getDTR());
                });
    }
}