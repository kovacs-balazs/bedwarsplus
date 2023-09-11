package me.koba1.bedwars.utils.objects;

import lombok.Getter;
import lombok.Setter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.files.BedwarsArenaFile;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsModes;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsTeams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class BedwarsArena {
    @Getter private String name; // &6&oMUSHROOM ISLANDS
    @Getter private String displayName;
    @Getter @Setter private Location lobbySpawn;
    @Getter @Setter private Location deathLocation;
    @Getter @Setter private World world;
    @Getter @Setter private ArrayList<Generator> generators;
    @Getter @Setter private HashMap<BedwarsTeams, Location> bedSpawnLocations;
    @Getter @Setter private HashMap<BedwarsTeams, Location> teamSpawnLocations;
    @Getter @Setter private int spawnBlockProtection;
    @Getter private final BedwarsArenaFile file;
    @Getter private HashMap<Integer, BedwarsArenaManager> managers;
    @Getter @Setter private int minPlayer;
    @Getter @Setter private int maxPlayers;
    @Getter @Setter private int startTime;
    @Getter @Setter private int minY;
    @Getter @Setter private BedwarsModes mode;
    @Getter @Setter private int maxY;
    @Getter @Setter private int respawnTime;

    public BedwarsArena(BedwarsArenaFile file) {
        this.file = file;

        managers = new HashMap<>();
        this.name = file.getYmlFile().getName().replace(".yml", "");
        reload();
        Main.getBedwarsArena().put(this.name, this);
        System.out.println("Created arena: " + this.name);
    }
    /*
            for (Map.Entry<BedwarsTeams, Location> hash : bedSpawnLocations.entrySet()) {
            file.get().set("bed_spawns." + hash.getKey(), hash.getValue());
        }
        for (Map.Entry<BedwarsTeams, Location> hash : teamSpawnLocations.entrySet()) {
            file.get().set("team_spawns." + hash.getKey(), hash.getValue());
        }
                file.save();
     */
    public void reload() {
        file.reload();
        this.generators = new ArrayList<>();
        this.bedSpawnLocations = new HashMap<>();
        this.teamSpawnLocations = new HashMap<>();

        lobbySpawn = file.get().getLocation("lobby-spawn");
        this.displayName = file.getString("display-name");
        world = Bukkit.getWorld(file.get().getString("world"));

        List<?> generators = file.get().getList("generators");
        if(generators != null) {
            for (Object gen : generators) {
                if (gen instanceof Generator g) {
                    this.generators.add(g);
                }
            }
        }

        for (String teamName : file.get().getConfigurationSection("bed_spawns").getKeys(false)) {
            bedSpawnLocations.put(BedwarsTeams.getTeamByName(teamName),file.get().getLocation("bed_spawns." + teamName));
        }
        for (String teamName : file.get().getConfigurationSection("team_spawns").getKeys(false)) {
            //System.out.println("Put team: " + teamName + "Location: " + file.get().getLocation("team_spawns." + teamName));
            teamSpawnLocations.put(BedwarsTeams.getTeamByName(teamName),file.get().getLocation("team_spawns." + teamName));
        }
        spawnBlockProtection = file.get().getInt("spawn-block-protection");
        this.minY = file.get().getInt("minY");
        this.maxY = file.get().getInt("maxY");
        this.minPlayer = file.get().getInt("minPlayer");
        this.maxPlayers = file.get().getInt("maxPlayers");
        this.startTime = file.get().getInt("startTime");
        this.mode = BedwarsModes.getModeByName(file.get().getString("mode")) != null
                ?  BedwarsModes.getModeByName(file.get().getString("mode"))
                : BedwarsModes.MODE_SOLO ;
        this.deathLocation = file.get().getLocation("deathLocation");
        this.respawnTime = file.get().getInt("respawnTime");
        //System.out.println("Arena set mode: " + this.mode);
    }

    public BedwarsArenaManager newGame() {
        Random rand = new Random();
        int id;
        do {
            id = rand.nextInt(0, 999999);
        } while (managers.containsKey(id));

        BedwarsArenaManager manager = new BedwarsArenaManager(this, id);
        managers.put(id, manager);
        manager.prepare();
        return manager;
    }
}
