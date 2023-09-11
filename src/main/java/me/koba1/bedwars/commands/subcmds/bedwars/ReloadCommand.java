package me.koba1.bedwars.commands.subcmds.bedwars;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.configs.messages.Messages;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand implements SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload all configuration files";
    }

    @Override
    public String getSyntax() {
        return "/bedwars reload";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.admin.reload";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    private static Main m = Main.getPlugin(Main.class);

    @Override
    public void perform(CommandSender sender, String[] args) {
        Main.getInstance().reload();
        sender.sendMessage(Messages.CONFIG_RELOADED.language(sender).queue());

    }
}
