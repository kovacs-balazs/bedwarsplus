package me.koba1.bedwars.utils.datastorages;

import me.koba1.bedwars.database.flatfile.PlayerDataFile;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.MessageLanguage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FileStorage implements DataStorage {
    @Override
    public void loadPlayer(Player player) {
        FileConfiguration config = PlayerDataFile.get();

        MessageLanguage language = MessageLanguage.getLanguage(config.getString(player.getUniqueId() + ".language"));
        long lastOnline = config.getLong(player.getUniqueId() + ".lastOnline");

        new BedwarsPlayer(player.getName(), player.getUniqueId(), language);
    }

    /**
     * call this function for only offline player
     * @param uuid
     */
    public void loadPlayer(String uuid) {
        FileConfiguration config = PlayerDataFile.get();

        MessageLanguage language = MessageLanguage.getLanguage(config.getString(uuid + ".language"));
        long lastOnline = config.getLong(uuid + ".lastOnline");

        OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
        new BedwarsPlayer(off.getName(), off.getUniqueId(), language);
    }

    @Override
    public void cachePlayers() {
        FileConfiguration config = PlayerDataFile.get();
        for (String key : config.getKeys(false)) {
            loadPlayer(key);
        }
    }

    @Override
    public void savePlayer(BedwarsPlayer bedwarsPlayer) {
        FileConfiguration config = PlayerDataFile.get();

        config.set(bedwarsPlayer.getUUID().toString() + ".name", bedwarsPlayer.getName());
        config.set(bedwarsPlayer.getUUID().toString() + ".language", bedwarsPlayer.getLanguage());

        // ToDo: Statistics save
        config.set(bedwarsPlayer.getUUID().toString() + ".statistics.kills", bedwarsPlayer.getStatistics().getKills());
        config.set(bedwarsPlayer.getUUID().toString() + ".statistics.deaths", bedwarsPlayer.getStatistics().getDeaths());
    }

    @Override
    public void saveAll() {

    }
}
