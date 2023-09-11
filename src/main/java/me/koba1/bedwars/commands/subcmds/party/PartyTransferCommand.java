package me.koba1.bedwars.commands.subcmds.party;

import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.Party;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyTransferCommand implements SubCommand {
    @Override
    public String getName() {
        return "transfer";
    }

    @Override
    public String getDescription() {
        return "Transfers the party leader to the selected player!";
    }

    @Override
    public String getSyntax() {
        return "/party transfer <player>";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.party.transfer";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player p){
            BedwarsPlayer player = BedwarsPlayer.getPlayer(p);
            Party party = player.getCurrentParty();
            Player t = Bukkit.getPlayer(args[0]);
            if(party == null) {
                p.sendMessage(Messages.NOT_IN_A_PARTY.language(p).queue());
                return;
            }
            if(player != party.getPartyLeader()){
                p.sendMessage(Messages.NOT_PARTY_LEADER.language(p).queue());
                return;
            }
            if(t == null){
                p.sendMessage(Messages.NOT_FOUND_PLAYER.language(p).toString());
                return;
            }
            BedwarsPlayer target = BedwarsPlayer.getPlayer(t);
            if(player == target) {
                return;
            }
            if(target.getCurrentParty() != party) {
                p.sendMessage(Messages.NOT_IN_THE_SAME_PARTY.language(p).queue());
                return;
            }
            party.transferLeader(target);
            p.sendMessage(Messages.PARTY_NEW_LEADER.language(p).queue());//Todo: New message
            for (BedwarsPlayer partyMember : party.getPartyMembers()) {
                Messages.PARTY_LEADER_TRANSFER.language(partyMember).setPlayer(target.getName()).send();
            }
        }
    }
}
