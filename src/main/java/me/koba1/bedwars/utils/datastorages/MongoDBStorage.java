package me.koba1.bedwars.utils.datastorages;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.database.mongodb.api.MongoApi;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.MessageLanguage;
import me.koba1.bedwars.utils.objects.PlayerStatistics;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import static com.mongodb.client.model.Filters.*;
import java.util.UUID;

public class MongoDBStorage implements DataStorage {
    private final MongoApi mongoApi = Main.getInstance().getMongoApi();
    @Override
    public void loadPlayer(Player player) {
        if(Main.getBedwarsPlayers().containsKey(player.getUniqueId())){
            //DO EVERYTHING AS YOU SHOULD
            Main.getBedwarsPlayers().get(player.getUniqueId()).setStatistics((PlayerStatistics) Main.getStatisticsConfig().get(player.getUniqueId().toString()));
            BedwarsPlayer p = BedwarsPlayer.getPlayer(player);
            if(p.isInGame()){
                p.getCurrentGame().join(p);
            }
        } else {
            //Player does not do exists Maybe not cached
            mongoApi.Find("Users",eq("UUID",player.getUniqueId().toString())).thenAcceptAsync(res -> {
                try {
                    if (res.isEmpty()) {
                        //New player
                        Document insert = new Document();
                        insert.append("Name", player.getName());
                        insert.append("UUID",player.getUniqueId().toString());
                        insert.append("language", MessageLanguage.getDefault().getLang());
                        insert.append("lastonline",System.currentTimeMillis());
                        mongoApi.Insert("Users",insert).thenAcceptAsync(_c -> {
                            new BedwarsPlayer(
                                    player.getName(),
                                    player.getUniqueId(),
                                    MessageLanguage.getDefault()
                            );
                            loadPlayer(player);
                        });
                    } else {
                        //cached
                        new BedwarsPlayer(
                                res.getString("Name"),
                                UUID.fromString(res.getString("UUID")),
                                MessageLanguage.getLanguage(res.getString("language"))
                        );

                        loadPlayer(player);
                    }
                }catch (Exception e){
                    Main.sendError(e.toString());
                }
            });

        }
    }

    @Override
    public void cachePlayers() {

        mongoApi.FindAll("Users", empty()).thenAcceptAsync(rs -> {
            try {
                for(int i = 0;i<rs.size();i++){
                    if(i > Main.getInstance().getPlayerCacheLimit()) break;
                    BedwarsPlayer player = new BedwarsPlayer(
                            rs.get(i).getString("Name"),
                            UUID.fromString(rs.get(i).getString("UUID")),
                            MessageLanguage.getLanguage(rs.get(i).getString("language"))
                    );
                    Main.getBedwarsPlayers().put( UUID.fromString(rs.get(i).getString("UUID")),player);
                }
            }catch (Exception e){
                Main.sendError(e.toString());
            }

            //Load all online player
            for (Player p : Bukkit.getOnlinePlayers()) {
                loadPlayer(p);
            }
        });
    }

    @Override
    public void savePlayer(BedwarsPlayer bedwarsPlayer) {

    }

    @Override
    public void saveAll() {

    }


}
