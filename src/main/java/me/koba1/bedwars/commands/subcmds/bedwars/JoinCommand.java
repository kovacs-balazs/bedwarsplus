package me.koba1.bedwars.commands.subcmds.bedwars;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.Playertools;
import me.koba1.bedwars.utils.objects.BedwarsArena;
import me.koba1.bedwars.utils.objects.BedwarsArenaManager;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsModes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class JoinCommand implements SubCommand {
    //Test command
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Reload all configuration files";
    }

    @Override
    public String getSyntax() {
        return "/bedwars join";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.join";
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
            BedwarsModes mode = BedwarsModes.getModeByName(args[0]);
            BedwarsPlayer player = BedwarsPlayer.getPlayer(p);
            if(mode == null)
                mode = BedwarsModes.MODE_SOLO;
            if(player.isInParty() && !player.isPartyLeader()) {
                p.sendMessage("not party leader!");
                return;
            }
            // So cute
            switch (mode) {
                case MODE_SOLO -> {
                    if(player.isInParty()) {
                        p.sendMessage("Party too big for this game mode!");
                        return;
                    }
                }
                case MODE_DOUBLES -> {
                    if(player.isInParty() && player.getCurrentParty().getPartyMembers().size() > 2) {
                        p.sendMessage("Party too big for this game mode!");
                        return;
                    }
                }
                case MODE_3v3v3v3 -> {
                    if(player.isInParty() && player.getCurrentParty().getPartyMembers().size() > 3) {
                        p.sendMessage("Party too big for this game mode!");
                        return;
                    }
                }
                case MODE_4v4v4v4 -> {
                    if(player.isInParty() && player.getCurrentParty().getPartyMembers().size() > 4) {
                        p.sendMessage("Party too big for this game mode!");
                        return;
                    }
                }
            }
            BedwarsArenaManager manager = Main.getInstance().getPlayertools().getRandomGame(player,mode);
            if(manager == null){
                p.sendMessage("No game found");
                return;
            }
            manager.join(player);
        }
    }
}
