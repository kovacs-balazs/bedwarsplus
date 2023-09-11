package me.koba1.bedwars.events;

import me.koba1.bedwars.utils.objects.BedwarsArenaManager;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsTeams;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class PlayerDamageByPlayerListener implements Listener {
    @EventHandler
    public void onDamageByPlayer(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player victim) {
            BedwarsPlayer victimPlayer = BedwarsPlayer.getPlayer(victim);
            BedwarsArenaManager bwManager = victimPlayer.getCurrentGame();
            if (e.getDamager() instanceof Player damager) {
                BedwarsPlayer damagerPlayer = BedwarsPlayer.getPlayer(damager);

                if (bwManager != null) {
                    //Friendly Fire
                    if (victimPlayer.getCurrentTeam() == damagerPlayer.getCurrentTeam()) {
                        e.setCancelled(true);
                        return;
                    }

                    if (victim.getHealth() - e.getFinalDamage() > 0)
                        return;
                }
                e.setCancelled(true);
                bwManager.killPlayer(victimPlayer, damagerPlayer);
            } else if(e.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player damager) {
                BedwarsPlayer damagerPlayer = BedwarsPlayer.getPlayer(damager);

                if (bwManager != null) {
                    //Friendly Fire
                    if (victimPlayer.getCurrentTeam() == damagerPlayer.getCurrentTeam()) {
                        e.setCancelled(true);
                        return;
                    }

                    if (victim.getHealth() - e.getFinalDamage() > 0)
                        return;
                }
                e.setCancelled(true);
                bwManager.killPlayer(victimPlayer, damagerPlayer);
            }
        }
    }
}
