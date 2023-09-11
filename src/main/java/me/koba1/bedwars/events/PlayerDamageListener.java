package me.koba1.bedwars.events;

import me.koba1.bedwars.utils.objects.BedwarsArenaManager;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        //Fall damage etc
        if(e.getEntity() instanceof Player p) {
            BedwarsPlayer bedwarsPlayer = BedwarsPlayer.getPlayer(p);
            BedwarsArenaManager bwManager = bedwarsPlayer.getCurrentGame();
            if(bwManager != null){
                if(p.getHealth() - e.getFinalDamage() > 0)
                    return;
            }
            e.setCancelled(true);

            bwManager.killPlayer(bedwarsPlayer,null);
        }
    }
}
