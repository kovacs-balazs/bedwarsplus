package me.koba1.bedwars.commands.subcmds.party;

import me.koba1.bedwars.commands.SubCommand;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class PartyListCommand implements SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Prints the party members";
    }

    @Override
    public String getSyntax() {
        return "/party list";
    }

    @Override
    public String getPermission() {
        return "bedwars.commands.party.list";
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
            if(party == null){
                p.sendMessage(Messages.NOT_IN_A_PARTY.language(p).queue());
                return;
            }
            String members = party.getPartyMembers().stream().map(BedwarsPlayer::getName)
                            .collect(Collectors.joining(", "));
            p.sendMessage("Party members:" + members);
        }

    }
}
