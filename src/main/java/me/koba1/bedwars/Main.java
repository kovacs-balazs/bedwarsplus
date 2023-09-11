package me.koba1.bedwars;

;
import lombok.Getter;
import lombok.Setter;
import me.koba1.bedwars.commands.cmds.BedwarsCommand;
import me.koba1.bedwars.commands.MainCommand;
import me.koba1.bedwars.commands.cmds.PartyCommand;
import me.koba1.bedwars.configitem.Glow;
import me.koba1.bedwars.configs.Config;
import me.koba1.bedwars.configs.ConfigFile;
import me.koba1.bedwars.configs.configmanagers.MessagesTool;
import me.koba1.bedwars.configs.files.BedwarsArenaFile;
import me.koba1.bedwars.configs.files.GeneratorConfigFile;
import me.koba1.bedwars.configs.files.ShopCategoryFile;
import me.koba1.bedwars.configs.messages.MessageFile;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.database.DatabaseConnector;
import me.koba1.bedwars.database.flatfile.FlatFileConnector;
import me.koba1.bedwars.database.mongodb.MongoDBConnector;
import me.koba1.bedwars.database.mongodb.api.MongoApi;
import me.koba1.bedwars.database.sql.SQLConnector;
import me.koba1.bedwars.database.sql.SQLGenerator;
import me.koba1.bedwars.database.sql.apitypes.SQLApi;
import me.koba1.bedwars.events.PlayerDamageByPlayerListener;
import me.koba1.bedwars.events.PlayerDamageListener;
import me.koba1.bedwars.events.PlayerJoinListener;
import me.koba1.bedwars.events.PlayerLeaveListener;
import me.koba1.bedwars.holograms.HologramFactory;
import me.koba1.bedwars.holograms.HologramManager;
import me.koba1.bedwars.holograms.protocollib.PlibHologramFactory;
import me.koba1.bedwars.scoreboard.BedwarsScoreboard;
import me.koba1.bedwars.shopgui.ShopCategoryManager;
import me.koba1.bedwars.shopgui.categories.ShopCategory;
import me.koba1.bedwars.shopgui.categories.ShopQuickBuyCategory;
import me.koba1.bedwars.shopgui.events.ShopClickEvent;
import me.koba1.bedwars.utils.Playertools;
import me.koba1.bedwars.utils.datastorages.DataStorage;
import me.koba1.bedwars.utils.datastorages.FileStorage;
import me.koba1.bedwars.utils.datastorages.MongoDBStorage;
import me.koba1.bedwars.utils.datastorages.SQLStorage;
import me.koba1.bedwars.utils.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.data.type.Bed;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;


import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class  Main extends JavaPlugin {
    static {
        ConfigurationSerialization.registerClass(PlayerStatistics.class,"PlayerStatistics");
        ConfigurationSerialization.registerClass(Generator.class,"Generator");
    }
    @Getter private final int playerCacheLimit = 500;
    @Getter private final int maxPartySize = 4; //Todo: Config
    @Getter private final int partyAcceptTime = 30; //Todo: Config (Seconds)
    @Getter private final int arenaTotalMatchTime = 300; //Todo: Config (Seconds)
    @Getter private final int maxServerPerMap = 5; //Todo: Config

    @Getter private Playertools playertools;
    @Getter private static HologramManager hologramManager;
    @Getter private static HashMap<UUID, BedwarsPlayer> bedwarsPlayers;
    @Getter private static HashMap<String, BedwarsArena> bedwarsArena;
    @Getter private static HashMap<String, MessageLanguage> languages;
    @Getter private SQLApi sqlApi;
    @Getter private MongoApi mongoApi;
    @Getter private static Main instance;
    @Getter private static DataStorage dataStorage;
    @Getter private static DatabaseConnector databaseConnector;
    @Getter @Setter
    private static MessageLanguage defaultLanguage;
    @Getter private static FileConfiguration statisticsConfig;

    @Getter private World hub;

    public void onEnable() {
        // Plugin startup logic
        instance = this;
        languages = new HashMap<>();
        bedwarsPlayers = new HashMap<>();
        bedwarsArena = new HashMap<>();


        HologramFactory plibFactory = new PlibHologramFactory();
        hologramManager = new HologramManager(plibFactory);

        //new ConfigManager(this).setup();
        statisticsConfig = new YamlConfiguration();
        new ConfigFile("config.yml");
        new BedwarsArenaFile("arenas/template.yml");
        new GeneratorConfigFile("generator_config.yml");

        Arrays.stream(Config.values()).forEach(values -> values.load(false));

        registerGlow();

        MessageFile messageFile = new MessageFile("messages/messages_" + Config.LANGUAGE.string() + ".yml");
        defaultLanguage = new MessageLanguage(Config.LANGUAGE.string(), messageFile);
        defaultLanguage.setDefault();

        MessagesTool.loadNewMessages();

        Messages.loadAll();
        messageFile.getConfig().save();

        ConfigFile.getConfig().save();

        Playertools.cleanupConfigFile(ConfigFile.getConfig());

        for (MessageLanguage value : Main.getLanguages().values()) {
            Playertools.cleanupMessages(value.getFile().getYmlFile());
        }
        playertools = new Playertools();

        new BedwarsScoreboard(this);

        registerCommands();
        registerShop();
        registerEvents();

        setupDatas();

        File[] files = new File(getDataFolder(), "arenas").listFiles();
        if(files != null) {
            for (File arena : files) {
                new BedwarsArena(new BedwarsArenaFile("arenas/" + arena.getName()));
            }
        }
        for(BedwarsArena arena : bedwarsArena.values()){

            for(int i = 0;i<=maxServerPerMap;i++) {
                System.out.println(arena.getDisplayName() + " Created new game");
                arena.newGame();
            }
        }
        hub = Bukkit.getWorld("world");
//BRO BSAZODJÁÁÁ ?MEGGEGGGG//
        new BukkitRunnable() {
            @Override
            public void run() { ///
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    BedwarsPlayer online = BedwarsPlayer.getPlayer(onlinePlayer);

                    online.getScoreboard().update();
                }
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    public void setupDatas() {
        switch (Config.STORAGE_METHOD.storageMethod()) {
            case MONGODB -> {

                databaseConnector = new MongoDBConnector();
            }
            case FLATFILE -> {
                databaseConnector = new FlatFileConnector();
            }
            default -> {
                databaseConnector = new SQLConnector();
            }
        }
        System.out.println("MEHET A BW GECIIIIIIIIIIIIII"); // joooooooyoooo
        databaseConnector.initConnection();
        switch (Config.STORAGE_METHOD.storageMethod()) {
            case MONGODB -> {
                if(databaseConnector instanceof MongoDBConnector mongocon) {
                    mongoApi = new MongoApi(mongocon.getDatabase());
                    dataStorage = new MongoDBStorage();
                }
            }
            case FLATFILE -> {
                dataStorage = new FileStorage();
            }
            default -> {
                if(databaseConnector instanceof SQLConnector sqlConn) {
                    sqlApi = new SQLApi( sqlConn.getConnection());
                    new SQLGenerator(sqlConn.getConnection());
                    dataStorage = new SQLStorage();

                } else {
                    dataStorage = new FileStorage();
                }
            }
        }
        dataStorage.cachePlayers();
        //Load online players moved into cachePlayers
    }
    public void setupSerialization() {

    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerLeaveListener(), this);
        pm.registerEvents(new ShopClickEvent(), this);
        pm.registerEvents(new PlayerDamageByPlayerListener(), this);
        pm.registerEvents(new PlayerDamageListener(), this);
    }

    public void registerCommands() {
        MainCommand bedwars = new BedwarsCommand();
        bedwars.registerMainCommand(this, "bedwars");

        MainCommand party = new PartyCommand();
        party.registerMainCommand(this, "party");
    }

    public void registerShop() {
        new ShopCategoryFile("shop.yml");

        new ShopCategoryManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (BedwarsArena value : Main.getBedwarsArena().values()) {
            for (Generator generator : value.getGenerators()) {
                generator.destroyHologram();
            }
        }
    }

    public void reload() {
        reloadConfig();

        ConfigFile.getConfig().reload();
        Arrays.stream(Config.values()).forEach(values -> values.load(false));

        //MessageFile messageFile = new MessageFile("messages/messages_" + Config.LANGUAGE.string() + ".yml");

        if(!defaultLanguage.getLang().equalsIgnoreCase(Config.LANGUAGE.string())) {
            if(Main.getLanguages().containsKey(Config.LANGUAGE.string())) {
                defaultLanguage = Main.getLanguages().get(Config.LANGUAGE.string());
                defaultLanguage.setDefault();
            } else {
                MessageFile messageFile = new MessageFile("messages/messages_" + Config.LANGUAGE.string() + ".yml");
                defaultLanguage = new MessageLanguage(Config.LANGUAGE.string(), messageFile);
                defaultLanguage.setDefault();
            }
        }

        MessagesTool.loadNewMessages();
        Main.getLanguages().values().stream().map(MessageLanguage::getFile).forEach(MessageFile::reload);

        Messages.loadAll();

        Playertools.cleanupConfigFile(ConfigFile.getConfig());

        for (MessageLanguage value : Main.getLanguages().values()) {
            Playertools.cleanupMessages(value.getFile().getYmlFile());
        }

        ShopCategoryManager.getInstance().getCategories().values().forEach(ShopCategory::reload);

        ShopCategoryManager.getInstance().reload();
    }
    public static void sendError(String msg){
        Bukkit.getLogger().severe(msg);
    }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            NamespacedKey key = new NamespacedKey(this, getDescription().getName());

            Glow glow = new Glow(key);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}