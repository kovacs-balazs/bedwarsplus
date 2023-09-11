package me.koba1.bedwars.utils.objects;

import lombok.*;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Party {

    @Getter @Setter private BedwarsPlayer partyLeader;
    @Getter @Setter private ArrayList<BedwarsPlayer> partyMembers = new ArrayList<>();
    @Getter @Setter private HashMap<BedwarsPlayer, BukkitTask> invitedPlayers = new HashMap<>();
    public Party(BedwarsPlayer partyLeader){
        this.partyLeader = partyLeader;
        partyMembers.add(partyLeader);
        partyLeader.setCurrentParty(this);
    }

    public boolean addMember(BedwarsPlayer target){
        if(partyMembers.size() + 1 > Main.getInstance().getMaxPartySize()){
            return false;
        }
        partyMembers.add(target);
        invitedPlayers.get(target).cancel();
        invitedPlayers.remove(target);
        target.setCurrentParty(this);
        return true;
    }
    /**
        @param target The target Player
        @return true if the leader was changed
     **/
    public boolean kickMember(BedwarsPlayer target){
        if(target == partyLeader){
            partyMembers.remove(target);
            this.partyLeader = partyMembers.get(0);
            target.setCurrentParty(null);
            return true;
        }else{
            target.setCurrentParty(null);
            partyMembers.remove(target);
        }
        return false;
    }
    public boolean transferLeader(BedwarsPlayer target){
        if(target == this.partyLeader){
            return false;
        }
        this.partyLeader = target;
        return true;

    }

    public void chat(String message) {
        for (BedwarsPlayer partyMember : getPartyMembers()) {
            partyMember.sendMessage(message);
        }
    }
    public boolean inviteMember(BedwarsPlayer target) {
        if(invitedPlayers.containsKey(target)){
            return false;
        }
        BukkitTask t = new BukkitRunnable() {
            @Override
            public void run(){
                //Player didn't accept the request
                if(invitedPlayers.containsKey(target)){
                    return;
                }

                target.chat(Messages.PARTY_INVITE_EXPIRED);
                invitedPlayers.remove(target);
            }
        }.runTaskLater(Main.getPlugin(Main.class),20L * Main.getInstance().getPartyAcceptTime());
        invitedPlayers.put(target,t);
        return true;
    }
    public boolean uninviteMember(BedwarsPlayer target) {
        if(!invitedPlayers.containsKey(target)){
            return false;
        }
        invitedPlayers.get(target).cancel();
        invitedPlayers.remove(target);
        return true;
    }
}
