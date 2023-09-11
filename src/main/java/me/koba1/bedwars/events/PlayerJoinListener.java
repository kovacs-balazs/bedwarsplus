package me.koba1.bedwars.events;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        Main.getDataStorage().loadPlayer(p);
        BedwarsPlayer bwPlayer = BedwarsPlayer.getPlayer(p);
        if(bwPlayer != null) {
            bwPlayer.join(p);
        }
    }
}
