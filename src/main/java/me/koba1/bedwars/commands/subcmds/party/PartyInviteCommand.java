package me.koba1.bedwars.commands.subcmds.party;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.Playertools;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.Party;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyInviteCommand implements SubCommand {
    private final Playertools tools = Main.getInstance().getPlayertools();
    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Invites a player to the party. If you are not in a party, then it will create a party!";
    }

    @Override
    public String getSyntax() {
        return "/party invite <name>";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.party.invite";
    }

    @Override
    public List<String> getTabCompletion(int index, String[] args) {
        return null;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player p) {
            BedwarsPlayer player = BedwarsPlayer.getPlayer(p);
            Player t = Bukkit.getPlayer(args[0]);
            Party party = player.getCurrentParty();
            if(t == null){
                p.sendMessage(Messages.NOT_FOUND_PLAYER.language(p).toString());
                return;
            }
            BedwarsPlayer target = BedwarsPlayer.getPlayer(t);
            if(party == null){
                party = new Party(player);
            }
            if(party.getInvitedPlayers().containsKey(target)){
                player.chat(Messages.PARTY_ALREADY_INVITED);
                return;
            }
            if(!party.inviteMember(target)){
                player.chat(Messages.PARTY_ALREADY_INVITED);
                return;
            }
            for (BedwarsPlayer partyMember : party.getPartyMembers()) {
                Messages.PARTY_MEMBER_INVITED.language(partyMember).setPlayer(target.getName()).send();
            }
            tools.sendClickableMessage(t,Messages.PARTY_INVITED.language(t).setPlayer(p).queue(),"/party accept " + p.getName(),"/party accept " + p.getName());
            //t.sendMessage(Messages.PARTY_INVITED.language(t).setPlayer(p).queue());
        }
    }
}
