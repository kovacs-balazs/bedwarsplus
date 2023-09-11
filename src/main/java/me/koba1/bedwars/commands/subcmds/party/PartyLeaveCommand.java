package me.koba1.bedwars.commands.subcmds.party;

import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyLeaveCommand implements SubCommand {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Leaves the current party, if you are the leader then the leader goes to a random member!";
    }

    @Override
    public String getSyntax() {
        return "/party leave";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.party.leave";
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
            if(party == null) {
                p.sendMessage(Messages.NOT_IN_A_PARTY.language(p).queue());
                return;
            }
            for (BedwarsPlayer partyMember : party.getPartyMembers()) {
                Messages.PARTY_MEMBER_LEAVE.language(partyMember).setPlayer(p).send();
            }
            //Leader changed
            if(party.kickMember(player)){
                for (BedwarsPlayer partyMember : party.getPartyMembers()) {
                    Messages.PARTY_CHANGED_LEADER.language(partyMember).setPlayer(party.getPartyLeader().getName()).send();
                }
            }
            p.sendMessage(Messages.PARTY_LEAVE.language(p).queue());

        }
    }
}
