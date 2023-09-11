package me.koba1.bedwars.utils.datastorages;

import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface DataStorage {

    public void loadPlayer(Player player);
    public void cachePlayers();

    public void savePlayer(BedwarsPlayer bedwarsPlayer);
    public void saveAll();

}
