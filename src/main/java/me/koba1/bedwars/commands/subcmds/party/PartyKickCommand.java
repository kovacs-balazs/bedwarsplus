package me.koba1.bedwars.commands.subcmds.party;

import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.Party;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyKickCommand implements SubCommand {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Kicks the selected player";
    }

    @Override
    public String getSyntax() {
        return "/party kick <player>";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.party.kick";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player p){
            BedwarsPlayer player = BedwarsPlayer.getPlayer(p);
            Player t = Bukkit.getPlayer(args[0]);
            Party party = player.getCurrentParty();
            if(t == null){
                p.sendMessage(Messages.NOT_FOUND_PLAYER.language(p).toString());
                return;
            }
            BedwarsPlayer target = BedwarsPlayer.getPlayer(t);
            if(party == null) {
                p.sendMessage(Messages.NOT_IN_A_PARTY.language(p).queue());
                return;
            }
            if(player != party.getPartyLeader()){
                p.sendMessage(Messages.NOT_PARTY_LEADER.language(p).queue());
                return;
            }
            if(player == target) {
                p.sendMessage(Messages.CANT_KICK_YOURSELF.language(p).queue());
                return;
            }
            if(!party.getPartyMembers().contains(target)){
                p.sendMessage(Messages.NOT_IN_THE_SAME_PARTY.language(p).queue());
                return;
            }
            party.kickMember(target);
            for (BedwarsPlayer partyMember : party.getPartyMembers()) {
                Messages.PARTY_MEMBER_KICKED.language(partyMember).setPlayer(t).send();
            }
            t.sendMessage(Messages.PARTY_KICKED.language(t).queue());

        }
    }
}
