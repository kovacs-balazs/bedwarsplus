package me.koba1.bedwars.utils.objects;

import lombok.Getter;
import lombok.Setter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.Tasks;
import me.koba1.bedwars.utils.objects.gameenums.BedwardGameStates;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsPlayerState;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsTeams; // adbi meg van a megoldás
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player; // az ágy ott van, azonban ha nem tesz oda teamet a pluginnal kiszedjük.
import org.bukkit.event.player.PlayerTeleportEvent; // tehát ja
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.util.*;

public class BedwarsArenaManager {

    @Getter private BedwarsArena arena;
    @Getter private long prepareTime;
    @Getter private long lastTickBeforeMessage;
    @Getter private long matchTime;
    //@Getter private ArrayList<BedwarsPlayer> players;
    @Getter private HashMap<BedwarsTeams, ArrayList<BedwarsPlayer>> players;
    @Getter private BukkitTask task;
    @Getter private BedwardGameStates state;
    private final Plugin m = Main.getPlugin(Main.class);
    @Getter private final ArrayList<BedwarsPlayer> rejoinPlayers;
    @Getter @Setter private World currentWorld;


    public BedwarsArenaManager(BedwarsArena arena,int id) {
        this.state = BedwardGameStates.OFFLINE;
        this.arena = arena;
        this.currentWorld = copyArena(this.arena.getWorld(),id);
        this.players = new HashMap<>();
        for (BedwarsTeams val : arena.getBedSpawnLocations().keySet()) {
            this.players.put(val,new ArrayList<BedwarsPlayer>());
        }
        for (Location val : arena.getBedSpawnLocations().values()) {
            val.setWorld(this.currentWorld);
        }
        for (Location val : arena.getTeamSpawnLocations().values()) {
            val.setWorld(this.currentWorld);
        }
        arena.getDeathLocation().setWorld(this.currentWorld);
        arena.getGenerators().stream().map(Generator::getLocation).forEach(loc -> loc.setWorld(this.currentWorld));
        arena.getLobbySpawn().setWorld(this.currentWorld);
        this.players.put(BedwarsTeams.NONE,new ArrayList<BedwarsPlayer>());
        this.players.put(BedwarsTeams.SPECTATOR,new ArrayList<BedwarsPlayer>());
        this.rejoinPlayers = new ArrayList<>();
    }

    public void start() {
        //Start the main game Match
        //Player team select
        for(BedwarsPlayer player : getAllPlayersInArena()) {
            //setting basic player infos
            player.setBedwarsPlayerState(BedwarsPlayerState.IN_GAME);
            //Search for party's
            if(player.isPartyLeader()) {
                for (Map.Entry<BedwarsTeams, ArrayList<BedwarsPlayer>> teams : players.entrySet()) {
                    if(teams.getValue().size() + player.getCurrentParty().getPartyMembers().size() <= arena.getMode().getTeamSize()
                            && !(teams.getKey().equals(BedwarsTeams.SPECTATOR) || teams.getKey().equals(BedwarsTeams.NONE))) {
                        teams.getValue().addAll(player.getCurrentParty().getPartyMembers());
                        player.setCurrentTeam(teams.getKey());
                        for(BedwarsPlayer members : player.getCurrentParty().getPartyMembers()){
                            members.setCurrentTeam(teams.getKey());
                            Player target = members.asPlayer();
                            if(target == null) continue;

                            Tasks.executeLater(1,() ->
                                    target.teleport(arena.getTeamSpawnLocations().get(teams.getKey()), PlayerTeleportEvent.TeleportCause.PLUGIN));
                        }
                        break;
                    }
                }
            }
        }

        for(BedwarsPlayer player : getAllPlayersInArena()) {
            //Search for solos
            if(!player.isInParty()) {
                for (Map.Entry<BedwarsTeams, ArrayList<BedwarsPlayer>> teams : players.entrySet()) {
                    if(teams.getValue().size() + 1 <= arena.getMode().getTeamSize() && !(teams.getKey().equals(BedwarsTeams.SPECTATOR) || teams.getKey().equals(BedwarsTeams.NONE))) {
                        teams.getValue().add(player);
                        player.setCurrentTeam(teams.getKey());
                        Player target = player.asPlayer();
                        if(target == null) continue;
                        Tasks.executeLater(1,()->
                                target.teleport(arena.getTeamSpawnLocations().get(teams.getKey()), PlayerTeleportEvent.TeleportCause.PLUGIN));
                        break;
                    }
                }
            }
        }
        //Clear the None team
        players.get(BedwarsTeams.NONE).clear();
        //Teleport everyone to the team spawn, set the game mode to running
        state = BedwardGameStates.RUNNING;

        //Start generators
        for(Generator gen : arena.getGenerators()) {
            gen.register(this);
        }

        matchTime = System.currentTimeMillis() + Main.getInstance().getArenaTotalMatchTime();

        for(BedwarsPlayer player : getAllPlayersInArena()) {
            Player target = player.asPlayer();
            if(target == null) continue;
            target.sendMessage(Messages.ARENA_START_DESCRIPTION.language(target).queueArray());
        }
    }
    public boolean isMatchAvailable(){
        return state.equals(BedwardGameStates.STARTING) || state.equals(BedwardGameStates.PREPARING);
    }
    // Check party, can the whole party fit in?
    public boolean isMatchAvailable(BedwarsPlayer player) {
        if(!isMatchAvailable())
            return false;
        if(player.isInParty()){
            return getAllPlayersInArena().size() + player.getCurrentParty().getPartyMembers().size() <= arena.getMaxPlayers();
        }else {
            return getAllPlayersInArena().size() + 1 <= arena.getMaxPlayers();
        }
    }
    public ArrayList<BedwarsPlayer> getPlayersInTeam(BedwarsTeams team){
        return players.get(team);
    }
    public boolean isTeamAlive(BedwarsTeams team){
        return !players.get(team).isEmpty();
    }
    public ArrayList<BedwarsPlayer> getAllPlayersInArena() {
        ArrayList<BedwarsPlayer> players = new ArrayList<>();
        this.players.values().forEach(players::addAll);
        return players;
    }
    public void addPlayerToTeam(BedwarsTeams team,BedwarsPlayer player){
        getPlayersInTeam(team).add(player);
    }
    public void removePlayerFromTeam(BedwarsTeams team,BedwarsPlayer player){
        getPlayersInTeam(team).remove(player);
    }

    public void prepare() {
        state = BedwardGameStates.PREPARING;
        mainLoop();
        lastTickBeforeMessage = 0;
    }
    public World copyArena(World world, int id){
        File worldDir = world.getWorldFolder();
        String newName = world.getName() + "_"+id;
        FileUtil.copy(worldDir, new File(worldDir.getParent(), newName));

        WorldCreator creator = new WorldCreator(newName);
       return Bukkit.createWorld(creator);
    }
    public void selfDelete() {
        File worldDir = this.currentWorld.getWorldFolder();
        Bukkit.getServer().unloadWorld(currentWorld,false);
        worldDir.delete();
    }

    public void leave(BedwarsPlayer player) {

        //lobby leave
        if(state == BedwardGameStates.PREPARING || state == BedwardGameStates.STARTING) {
            player.setCurrentGame(null);
            player.setBedwarsPlayerState(BedwarsPlayerState.NOT_IN_GAME);
            removePlayerFromTeam(BedwarsTeams.NONE,player);
            if(getAllPlayersInArena().size() < arena.getMinPlayer()){
                state = BedwardGameStates.PREPARING;
                for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                    bedwarsPlayer.sendMessage(Messages.ARENA_START_CANCELLED.language(bedwarsPlayer).queue());
                }
            }
            for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                bedwarsPlayer.sendMessage(Messages.ARENA_LEAVE.language(bedwarsPlayer).setPlayer(player.getName()).setMaxPlayer(arena.getMaxPlayers()).setCurrentPlayer(getAllPlayersInArena().size()).queue());
            }
        }else if(state == BedwardGameStates.RUNNING) {
            this.rejoinPlayers.add(player);
            for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                bedwarsPlayer.sendMessage(Messages.ARENA_LEAVE_IN_MATCH.language(bedwarsPlayer)
                        .setPlayer(player.getName())
                        .setTeamColor(player.getCurrentTeam().getColor())
                        .queue());
            }
        } else if(state == BedwardGameStates.ENDING) {
            player.setCurrentGame(null);
            player.setBedwarsPlayerState(BedwarsPlayerState.NOT_IN_GAME);
            removePlayerFromTeam(BedwarsTeams.NONE,player);

        }
        Player target = player.asPlayer();
        if(target == null) return;
        Tasks.executeLater(1,()->
                target.teleport(Main.getInstance().getHub().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN));

    }
    public void join(BedwarsPlayer player) {

        //lobby join
        if(state == BedwardGameStates.PREPARING || state == BedwardGameStates.STARTING) {
            if(player.isInParty()){
                for(BedwarsPlayer partyMembers : player.getCurrentParty().getPartyMembers()) {
                    partyMembers.setCurrentGame(this);
                    addPlayerToTeam(BedwarsTeams.NONE,partyMembers);
                    partyMembers.setBedwarsPlayerState(BedwarsPlayerState.WAITING_FOR_START);
                    Player target = partyMembers.asPlayer();
                    if(target == null) return;
                    Tasks.executeLater(1,() ->target.teleport(arena.getLobbySpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN));
                    for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                        bedwarsPlayer.sendMessage(Messages.ARENA_JOIN.language(bedwarsPlayer)
                                .setPlayer(target)
                                .setMaxPlayer(arena.getMaxPlayers())
                                .setCurrentPlayer(getAllPlayersInArena().size()).queue());
                    }
                }
            } else {
                player.setCurrentGame(this);
                addPlayerToTeam(BedwarsTeams.NONE,player);
                player.setBedwarsPlayerState(BedwarsPlayerState.WAITING_FOR_START);


                Player target = player.asPlayer();
                if(target == null) return;
                Tasks.executeLater(1,() ->target.teleport(arena.getLobbySpawn(), PlayerTeleportEvent.TeleportCause.PLUGIN));
                for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                    bedwarsPlayer.sendMessage(Messages.ARENA_JOIN.language(bedwarsPlayer)
                            .setPlayer(target)
                            .setMaxPlayer(arena.getMaxPlayers())
                            .setCurrentPlayer(getAllPlayersInArena().size()).queue());
                }
            }
            if (arena.getMinPlayer() <= getAllPlayersInArena().size() && !state.equals(BedwardGameStates.STARTING)) {
                //start prepare time
                prepareTime = System.currentTimeMillis() + arena.getStartTime() * 1000L;
                state = BedwardGameStates.STARTING;
                for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                    bedwarsPlayer.sendMessage(Messages.ARENA_START_PREPARE.language(bedwarsPlayer)
                            .setTime(arena.getStartTime()).queue());
                }
            }

        }else if(state == BedwardGameStates.RUNNING) {
            if(this.rejoinPlayers.contains(player)) {
                //Player rejoined
                player.setCurrentGame(this);
                this.rejoinPlayers.remove(player);
                for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                    bedwarsPlayer.sendMessage(Messages.ARENA_REJOIN.language(bedwarsPlayer)
                            .setPlayer(player.getName())
                            .setTeamColor(player.getCurrentTeam().getColor())
                            .queue());
                }
                //Kill player after rejoin
                killPlayer(player,null);
            }
        }

    }

    public void end() {
        task.cancel();
    }

    public void mainLoop() {
        /*Map<Integer, ChatColor> titles = new HashMap<>(Map.of( Büszke vagyok rád koba, de ott rohadjon el uwu
                10, ChatColor.YELLOW,
                5, ChatColor.GOLD,
                3, ChatColor.RED,
                2, ChatColor.RED,
                1, ChatColor.RED
        ));*/
        List<Integer> titles = Arrays.asList(30,20,15,10,5,4,3,2,1);
        task = new BukkitRunnable() {
            @Override
            public void run() {
                long unixTime = System.currentTimeMillis();
                switch (state) {
                    case STARTING -> {
                        if(prepareTime <= unixTime) {
                            start();
                        } else {
                            int diff = (int) ((prepareTime - unixTime) / 1000);
                            if(titles.contains(diff) && unixTime > lastTickBeforeMessage) {
                                for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                                    bedwarsPlayer.sendMessage(Messages.ARENA_START_PREPARE
                                            .language(bedwarsPlayer)
                                            .setTime(diff).queue());
                                }
                                lastTickBeforeMessage = unixTime + 1000;
                            }
                        }
                    }
                    case RUNNING -> {

                        if(unixTime > matchTime) {
                            //match end
                        }



                    }

                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class),0L,1L);
    }


    public void killPlayer(BedwarsPlayer player,BedwarsPlayer lastDamager) {

        if(lastDamager == null) {
            for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                bedwarsPlayer.sendMessage(Messages.ARENA_PLAYER_DEATH.language(bedwarsPlayer)
                        .setPlayer(player.getName())
                        .setTeamColor(player.getCurrentTeam().getColor())
                        .queue());
            }
        } else {
            for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
                bedwarsPlayer.sendMessage(Messages.ARENA_PLAYER_DEATH_BY_PLAYER.language(bedwarsPlayer)
                        .setPlayer(player.getName())
                        .setTeamColor(player.getCurrentTeam().getColor())
                        .setEnemyColor(lastDamager.getCurrentTeam().getColor())
                        .setKiller(lastDamager.getName())
                        .queue());
            }
        }
        Player p = player.asPlayer();
        p.setHealth(20);
        for (BedwarsPlayer bedwarsPlayer : getAllPlayersInArena()) {
            if(bedwarsPlayer.asPlayer() == null) continue;

            bedwarsPlayer.asPlayer().hidePlayer(m, p);
        }
        p.setAllowFlight(true);
        p.setFlying(true);
        p.setInvulnerable(true);
        p.setFireTicks(0);
        p.setArrowsStuck(0);
        p.setArrowsInBody(0);
        p.getActivePotionEffects()
                .stream()
                .map(PotionEffect::getType)
                .forEach(p::removePotionEffect);



        Tasks.executeLater(1,()->
                p.teleport(arena.getDeathLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN));
        player.setBedwarsPlayerState(BedwarsPlayerState.DEAD);
        new BukkitRunnable() {
            final List<Integer> titles = Arrays.asList(5,4,3,2,1);
            final long deathTime = System.currentTimeMillis() + arena.getRespawnTime() * 1000L;
            @Override
            public void run() {
                if(player.asPlayer() == null){ this.cancel(); return;}
                long unixTime = System.currentTimeMillis();
                int diff = (int) ((deathTime - unixTime) / 1000);
                if(titles.contains(diff) ) {
                    Component title = Component.text(Messages.ARENA_PLAYER_RESPAWN_TITLE.language(p).queue());
                    Component subtitle = Component.text(Messages.ARENA_PLAYER_RESPAWN_SUBTITLE.language(p).setTime(diff).queue());
                    Title t = Title.title(title,subtitle);
                    p.showTitle(t);
                    p.sendMessage(subtitle);
                }
                if(unixTime > deathTime){
                    this.cancel();
                    respawnPlayer(player);
                    Component title = Component.text(Messages.ARENA_PLAYER_RESPAWN.language(p).queue());
                    Title t = Title.title(title,Component.text(""));
                    p.showTitle(t);
                }
            }
        }.runTaskTimer(m,0L,20L);

    }

    public void respawnPlayer(BedwarsPlayer player){
        Player p = player.asPlayer();
        if(p == null) return;
        p.setGameMode(GameMode.SURVIVAL);
        Tasks.executeLater(1,()->
                p.teleport(arena.getTeamSpawnLocations().get(player.getCurrentTeam()), PlayerTeleportEvent.TeleportCause.PLUGIN));
        player.setBedwarsPlayerState(BedwarsPlayerState.IN_GAME);


    }

}
