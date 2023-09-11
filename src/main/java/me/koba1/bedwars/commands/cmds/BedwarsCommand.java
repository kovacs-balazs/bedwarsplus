package me.koba1.bedwars.commands.cmds;

import me.koba1.bedwars.commands.MainCommand;
import me.koba1.bedwars.commands.argumentMatchers.ContainingAllCharsOfStringArgumentMatcher;
import me.koba1.bedwars.commands.subcmds.bedwars.JoinCommand;
import me.koba1.bedwars.commands.subcmds.bedwars.LeaveComand;
import me.koba1.bedwars.commands.subcmds.bedwars.ReloadCommand;
import me.koba1.bedwars.commands.subcmds.bedwars.ShopCommand;
import me.koba1.bedwars.configs.messages.Messages;

public class BedwarsCommand extends MainCommand {

    public BedwarsCommand() {
        super(Messages.NO_PERMISSION.message().queue(), new ContainingAllCharsOfStringArgumentMatcher());
    }

    @Override
    protected void registerSubCommands() {
        subCommands.add(new ReloadCommand());
        subCommands.add(new ShopCommand());
        subCommands.add(new JoinCommand());
        subCommands.add(new LeaveComand());
    }
}