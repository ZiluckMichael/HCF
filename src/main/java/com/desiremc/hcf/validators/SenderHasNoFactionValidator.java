package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;

public class SenderHasNoFactionValidator extends FactionSenderValidator
{

    @Override
    public boolean factionsValidate(FSession sender)
    {
        if (!sender.hasFaction())
        {
            return true;
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.already_member");
        return false;
    }

}
