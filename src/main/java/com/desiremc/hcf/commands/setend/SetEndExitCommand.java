package com.desiremc.hcf.commands.setend;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.api.SetEndAPI;

import java.util.List;

public class SetEndExitCommand extends ValidCommand
{

    public SetEndExitCommand()
    {
        super("exit", "set end exit", Rank.ADMIN, true, new String[] {});
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        SetEndAPI.setEndExit(sender.getPlayer(), "endexit", "set_end.exit");
    }
}
