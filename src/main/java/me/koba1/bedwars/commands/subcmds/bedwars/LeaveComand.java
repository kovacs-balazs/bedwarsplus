package me.koba1.bedwars.commands.subcmds.bedwars;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.utils.objects.BedwarsArena;
import me.koba1.bedwars.utils.objects.BedwarsArenaManager;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class LeaveComand implements SubCommand {
    //Test command
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Reload all configuration files";
    }

    @Override
    public String getSyntax() {
        return "/bedwars leave";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.leave";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        switch (index) {
            case 0:
                return Main.getBedwarsArena().values().stream().map(BedwarsArena::getName).toList();

        }

        return null;
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player p) {
            BedwarsPlayer player =  BedwarsPlayer.getPlayer(p);
            BedwarsArenaManager manager = player.getCurrentGame();
            if(manager == null){
                p.sendMessage("Not in game!");
                return;
            }
            if(player.isPartyLeader()){
                for(BedwarsPlayer member : player.getCurrentParty().getPartyMembers())
                    manager.leave(member);
            }else {
                manager.leave(player);
            }

        }
    }
}
