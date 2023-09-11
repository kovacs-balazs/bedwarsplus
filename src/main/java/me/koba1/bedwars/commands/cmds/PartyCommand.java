package me.koba1.bedwars.commands.cmds;

import me.koba1.bedwars.commands.MainCommand;
import me.koba1.bedwars.commands.argumentMatchers.ContainingAllCharsOfStringArgumentMatcher;
import me.koba1.bedwars.commands.subcmds.party.*;
import me.koba1.bedwars.configs.messages.Messages;

public class PartyCommand extends MainCommand {

    public PartyCommand() {
        super(Messages.NO_PERMISSION.message().queue(), new ContainingAllCharsOfStringArgumentMatcher());
    }

    @Override
    protected void registerSubCommands() {
        subCommands.add(new PartyInviteCommand());
        subCommands.add(new PartyLeaveCommand());
        subCommands.add(new PartyDisbandCommand());
        subCommands.add(new PartyTransferCommand());
        subCommands.add(new PartyKickCommand());
        subCommands.add(new PartyListCommand());
        subCommands.add(new PartyAcceptCommand());
    }
}
