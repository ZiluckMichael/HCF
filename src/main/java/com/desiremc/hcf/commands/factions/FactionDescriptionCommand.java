package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.validators.StringLengthValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.validators.SenderFactionOfficerValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;

import java.util.List;

public class FactionDescriptionCommand extends FactionValidCommand
{

    public FactionDescriptionCommand()
    {
        super("description", "Sets your faction's description.", new String[] { "desc" });

        addSenderValidator(new SenderHasFactionValidator());
        addSenderValidator(new SenderFactionOfficerValidator());

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("description")
                .setParser(new StringParser())
                .addValidator(new StringLengthValidator(1, 32))
                .setVariableLength()
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        Faction faction = sender.getFaction();
        String description = (String) arguments.get(0).getValue();
        
        faction.setDescription(description);
        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.description", true, false,
                "{description}", description);
        
    }

}
