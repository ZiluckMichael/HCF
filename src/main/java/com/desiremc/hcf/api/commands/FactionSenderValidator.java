package com.desiremc.hcf.api.commands;

import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;

public abstract class FactionSenderValidator implements SenderValidator
{

    @Override
    public final boolean validate(Session sender)
    {
        FSession fSession = FSessionHandler.getOnlineFSession(sender.getUniqueId());
        
        return factionsValidate(fSession);
    }
    
    public abstract boolean factionsValidate(FSession sender);
    
}
