package me.koba1.bedwars.utils.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("PlayerStatistics")
public class PlayerStatistics implements Cloneable, ConfigurationSerializable  {

    @Getter private final BedwarsPlayer player;
    @Getter @Setter private int bedwarsLevel;
    @Getter @Setter private int kills;
    @Getter @Setter private int deaths;
    @Getter @Setter private int matchPlayed;
    @Getter @Setter private int bedsDestroy;
    @Getter @Setter private int finalKills;
    @Getter @Setter private long firstPlay;
    @Getter @Setter private long lastPlay;
    @Getter @Setter private int wins;
    @Getter @Setter private int winstreak;//szija SZIA szia

    public PlayerStatistics(BedwarsPlayer player) {
        this.player = player;
    }
    public @NotNull Map<String,Object> serialize(){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();
        result.put("player",player.getUUID().toString());
        result.put("kills",kills);
        result.put("deaths",deaths);
        result.put("matchPlayed",matchPlayed);
        result.put("bedsDestroy",bedsDestroy);
        result.put("finalKills",finalKills);
        result.put("firstPlay",firstPlay);
        result.put("lastPlay",lastPlay);
        result.put("wins",wins);
        result.put("winstreak",winstreak);
        result.put("bedwarsLevel",bedwarsLevel);
        return result;
    }
    public static PlayerStatistics deserialize(Map<String,Object> args){
        BedwarsPlayer player = null;
        int kills= 0;
        int deaths = 0;
        int matchPlayed= 0;
        int bedsDestroy= 0;
        int finalKills= 0;
        long firstPlay= 0;
        long lastPlay= 0;
        int wins= 0;
        int winstreak = 0;
        int bedwarsLevel = 0;
        if(args.containsKey("player")){
            player = BedwarsPlayer.getPlayer(UUID.fromString((String) args.get("player")));
        }
        if(args.containsKey("kills")){
           kills = (int)args.get("kills");
        }
        if(args.containsKey("deaths")){
            deaths = (int)args.get("deaths");
        }
        if(args.containsKey("matchPlayed")){
            matchPlayed = (int)args.get("matchPlayed");
        }
        if(args.containsKey("bedsDestroy")){
            bedsDestroy = (int)args.get("bedsDestroy");
        }
        if(args.containsKey("finalKills")){
            finalKills = (int)args.get("finalKills");
        }
        if(args.containsKey("firstPlay")){
            firstPlay = (long)args.get("firstPlay");
        }
        if(args.containsKey("lastPlay")){
            lastPlay = (long)args.get("lastPlay");
        }
        if(args.containsKey("wins")){
            wins = (int)args.get("wins");
        }
        if(args.containsKey("winstreak")){
            winstreak = (int)args.get("winstreak");
        }
        if(args.containsKey("bedwarsLevel")){
            bedwarsLevel = (int)args.get("bedwarsLevel");
        }
        PlayerStatistics _temp = new PlayerStatistics(player);
        _temp.setDeaths(deaths);
        _temp.setKills(kills);
        _temp.setWins(wins);
        _temp.setBedsDestroy(bedsDestroy);
        _temp.setWinstreak(winstreak);
        _temp.setLastPlay(lastPlay);
        _temp.setFirstPlay(firstPlay);
        _temp.setFinalKills(finalKills);
        _temp.setMatchPlayed(matchPlayed);
        _temp.setBedwarsLevel(bedwarsLevel);
        return _temp;
    }
}
