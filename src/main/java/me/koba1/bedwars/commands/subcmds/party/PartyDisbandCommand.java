package me.koba1.bedwars.commands.subcmds.party;

import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.Party;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyDisbandCommand implements SubCommand {
    @Override
    public String getName() {
        return "disband";
    }

    @Override
    public String getDescription() {
        return "Disbands the party. It will kick all the party members";
    }

    @Override
    public String getSyntax() {
        return "/party disband";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.party.disband";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player p) {
            BedwarsPlayer player = BedwarsPlayer.getPlayer(p);
            Party party = player.getCurrentParty();
            if(party == null) {
                p.sendMessage(Messages.NOT_IN_A_PARTY.language(p).queue());
                return;
            }
            if(player != party.getPartyLeader()){
                p.sendMessage(Messages.NOT_PARTY_LEADER.language(p).queue());
                return;
            }
            for (BedwarsPlayer partyMember : party.getPartyMembers()) {
                partyMember.setCurrentParty(null);
                Messages.PARTY_KICKED.language(partyMember).send();
            }
            party.getPartyMembers().clear();
            p.sendMessage(Messages.PARTY_DISBAND.language(p).queue());

        }
    }
}
