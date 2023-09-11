package me.koba1.bedwars.utils.datastorages;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.database.sql.apitypes.SQLApi;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.MessageLanguage;
import me.koba1.bedwars.utils.objects.PlayerStatistics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class SQLStorage implements DataStorage {
    private final SQLApi sql = Main.getInstance().getSqlApi();

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
            sql.Poll("SELECT * FROM Users WHERE UUID=?",player.getUniqueId().toString()).thenAcceptAsync(res -> {
                try {
                    if (!res.next()) {//wait im dumb próba
                        //New player
                        sql.Execute("INSERT INTO Users (Name,UUID,language) VALUES (?,?,?)", player.getName(), player.getUniqueId().toString(), MessageLanguage.getDefault().getLang()).thenAcceptAsync(_c -> {
                            BedwarsPlayer bedwarsPlayer = new BedwarsPlayer(
                                    player.getName(),
                                    player.getUniqueId(),
                                    MessageLanguage.getDefault()
                            );
                            Main.getStatisticsConfig().set(player.getUniqueId().toString(),new PlayerStatistics(bedwarsPlayer));
                            this.loadPlayer(player);
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
            }catch (SQLException e){
                    Main.sendError(e.toString());
                    //nYOMOD GECI AZAZ KOBA
                    //Nyomj erre a file ra egy code reformatot pls
                }
            });

        }
    }

    @Override
    public void cachePlayers() {
        System.out.println("Caching");

            sql.Poll("SELECT * FROM Users LIMIT ?",Main.getInstance().getPlayerCacheLimit()).thenAcceptAsync(rs -> {
                try {
                   // System.out.println(rs.next());
                    while (rs.next()){
                        System.out.println("NYOMOD GECI CACHE" + rs.getString("Name"));
                        BedwarsPlayer player = new BedwarsPlayer(
                                rs.getString("Name"),
                                UUID.fromString(rs.getString("UUID")),
                                MessageLanguage.getLanguage(rs.getString("language"))

                        );

                    }//good or bad?
                }catch (SQLException e){
                   Main.sendError(e.toString() + "cacheplayer");
                }

                //Load all online player
                for (Player p : Bukkit.getServer().getOnlinePlayers()) { // odaírtam
                    loadPlayer(p); // itt van loadolva akk ez így wtf
                }
            });

    }
    @Override
    public void savePlayer(BedwarsPlayer bedwarsPlayer){
        sql.Execute("UPDATE Users SET Name = ?,language = ? WHERE UUID = ?",bedwarsPlayer.getName(),bedwarsPlayer.getLanguage().getLang(),bedwarsPlayer.getUUID().toString());
        //TODO: config playerStats
    }
    @Override
    public void saveAll(){

    }
}
