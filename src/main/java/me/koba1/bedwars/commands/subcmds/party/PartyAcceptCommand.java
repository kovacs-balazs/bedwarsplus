package me.koba1.bedwars.commands.subcmds.party;

import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.Party;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;

public class PartyAcceptCommand implements SubCommand {
    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/party accept <name>";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.party.accept";
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
            if(t == null) {
                p.sendMessage(Messages.NOT_FOUND_PLAYER.language(p).toString());
                return;
            }
            if(player.isInParty()) {
                //Már partyba van
                p.sendMessage(Messages.PARTY_ALREADY_IN.language(p).queue());
                return;
            }
            BedwarsPlayer target = BedwarsPlayer.getPlayer(t);
            if(!target.isInParty()) {
                p.sendMessage(Messages.PARTY_INVITE_EXPIRED.language(p).queue());
                return;
            }
            if(!target.getCurrentParty().getInvitedPlayers().containsKey(player)) {
                //not invited
                p.sendMessage(Messages.PARTY_NOT_INVITED.language(p).queue());
                return;
            }
            if(!target.getCurrentParty().addMember(player)) {
                p.sendMessage(Messages.PARTY_FULL.language(p).queue());
                return;
            }
            //Join party
            p.sendMessage(Messages.PARTY_JOIN.language(p).queue());
            for (BedwarsPlayer partyMember : player.getCurrentParty().getPartyMembers()) {
                Messages.PARTY_MEMBER_JOIN.language(partyMember).setPlayer(p).send();
            }
        }
    }
}
