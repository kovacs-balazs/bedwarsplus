package me.koba1.bedwars.events;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.utils.objects.BedwarsArenaManager;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        BedwarsPlayer player = BedwarsPlayer.getPlayer(p);
        BedwarsArenaManager manager = player.getCurrentGame();
        if(manager != null) {
            manager.leave(player);
        }

    }
}
