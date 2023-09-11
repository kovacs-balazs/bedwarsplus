package me.koba1.bedwars.utils.objects;

import lombok.Getter;
import lombok.Setter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.files.GeneratorConfigFile;
import me.koba1.bedwars.holograms.Hologram;
import me.koba1.bedwars.shopgui.ShopItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@SerializableAs("Generator")
public class Generator implements Cloneable, ConfigurationSerializable  {

    private static final Main m = Main.getPlugin(Main.class);

    @Getter @Setter private Location location;
    @Getter @Setter private int maxStack;
    @Getter @Setter private int spawnCooldown;
    @Getter @Setter private int spawnAmount;
    @Getter @Setter private int currentTier;
    @Getter @Setter private long nextSpawn;
    @Getter @Setter private Hologram holo;
    @Getter private BukkitTask generatorTask;
    @Getter @Setter private ShopItem.CostCurrency type;
    @Getter @Setter private ItemStack spawnItem;
    @Getter @Setter private BedwarsArenaManager manager;
    @Getter private final HashMap<Integer, Long> upgradeTime;

    public Generator(Location location, ShopItem.CostCurrency type) {
        this.location = location;
        this.maxStack = 0;
        this.type = type;
        this.spawnCooldown = 0;
        this.upgradeTime = new HashMap<>();
        this.nextSpawn = 0;
        this.spawnAmount = 1;
        this.currentTier = 1;
        this.holo = Main.getHologramManager().createHologram(this.location.clone().add(0, 3, 0), getHologramName());
        if(this.holo == null) {
            this.holo = Main.getHologramManager().getHologram(getHologramName());
        }
        switch (type){
            case IRON -> spawnItem = new ItemStack(Material.IRON_INGOT);
            case GOLD -> spawnItem = new ItemStack(Material.GOLD_INGOT);
            case EMERALD -> spawnItem = new ItemStack(Material.EMERALD);
            case DIAMOND -> spawnItem = new ItemStack(Material.DIAMOND);
            case NETHERITE -> spawnItem = new ItemStack(Material.NETHERITE_INGOT);
        }
    }

    public void nextTier(int tier) {
        ConfigurationSection modeSection = GeneratorConfigFile.get().getConfigurationSection(manager.getArena().getMode().getName());
        if(modeSection == null) {
            modeSection = GeneratorConfigFile.get().getConfigurationSection("Default");
        }
        if(modeSection == null) return;

        ConfigurationSection genSec = modeSection.getConfigurationSection(this.type.name().toLowerCase() + "." + tier);
        if(genSec == null) return;

        nextSpawn = System.currentTimeMillis() + genSec.getInt("delay") * 1000L;
        maxStack = genSec.getInt("spawn-limit");

        switch (type) {
            case IRON -> spawnItem = new ItemStack(Material.IRON_INGOT, spawnAmount);
            case GOLD -> spawnItem = new ItemStack(Material.GOLD_INGOT, spawnAmount);
            case EMERALD -> spawnItem = new ItemStack(Material.EMERALD, spawnAmount);
            case DIAMOND -> spawnItem = new ItemStack(Material.DIAMOND, spawnAmount);
            case NETHERITE -> spawnItem = new ItemStack(Material.NETHERITE_INGOT);
        }
        restart();
    }

    public void register(BedwarsArenaManager manager) {
        this.manager = manager;

        ConfigurationSection modeSection = GeneratorConfigFile.get().getConfigurationSection(manager.getArena().getMode().getName());
        if(modeSection == null) {
            modeSection = GeneratorConfigFile.get().getConfigurationSection("Default");
        }
        if(modeSection == null) return;

        ConfigurationSection genSec = modeSection.getConfigurationSection(this.type.name().toLowerCase());
        if(genSec == null) return;

        for (String key : genSec.getKeys(false)) {
            if(key.matches("^[0-9]+$")) {
                if(genSec.contains(key + ".start")) {
                    upgradeTime.put(Integer.parseInt(key), (genSec.getInt(key + ".start") * 1000L + System.currentTimeMillis()));
                } else {
                    spawnCooldown = genSec.getInt("delay");
                    spawnAmount = genSec.getInt("amount");
                    maxStack = genSec.getInt("spawn-limit");
                }
            } else {
                spawnCooldown = genSec.getInt("delay");
                spawnAmount = genSec.getInt("amount");
                maxStack = genSec.getInt("spawn-limit");
            }
        }

        if(nextSpawn == 0 && spawnAmount == 1 && maxStack == 0) {
            spawnCooldown = genSec.getInt("delay");
            spawnAmount = genSec.getInt("amount");
            maxStack = genSec.getInt("spawn-limit");
        }

        createHologram();
        checker();
        startSpawning();
    }
    //Todo: Config
    public void createHologram() {
        holo.addLine("Type: " + type.name());
        holo.addLine("Time: " + ((nextSpawn - System.currentTimeMillis()) / 1000));
        holo.addLine("Tier: " + currentTier);
    }

    public void updateHologram() {
        holo.setLine(0, "Type: " + type.name());
        holo.setLine(1, "Time: " + ((nextSpawn - System.currentTimeMillis()) / 1000));
        holo.setLine(2,"Tier: " + currentTier);
    }

    public void destroyHologram() {
        Main.getHologramManager().deleteHologram(getHologramName());
    }

    public void checker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long unixTime = System.currentTimeMillis();

                for (Map.Entry<Integer, Long> hash : upgradeTime.entrySet()) {
                    if(hash.getKey() <= currentTier) continue;
                    if(hash.getValue() <= unixTime ) {
                        nextTier(hash.getKey());
                    }
                }

                updateHologram();
            }
        }.runTaskTimer(m, 0L, 1L);
    }

    public String getHologramName() {
        return location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    }

    public void startSpawning() {
        nextSpawn = System.currentTimeMillis() + spawnCooldown * 1000L;
        this.generatorTask = new BukkitRunnable(){
            @Override
            public void run() {
                
                nextSpawn = System.currentTimeMillis() + spawnCooldown * 1000L;
                int droppedCount = location.getNearbyEntitiesByType(Item.class,1)
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(element -> element.getItemStack().getType() == spawnItem.getType())
                        .toList()
                        .size();
                if(droppedCount < maxStack) {
                    Item item = location.getWorld().dropItem(location, spawnItem);
                }

            }
        }.runTaskTimer(m,spawnCooldown * 20L,spawnCooldown * 20L);
    }
    public void stop() {
        this.generatorTask.cancel();
    }
    public void restart() {
        stop();
        startSpawning();
    }

    public @NotNull Map<String,Object> serialize(){
        LinkedHashMap<String,Object> result = new LinkedHashMap<>();
        result.put("location",location.serialize());
        result.put("type", type.name());


        return result;
    }
    public static Generator deserialize(Map<String,Object> args) {
        Location location = null;
        ShopItem.CostCurrency type = ShopItem.CostCurrency.getTypeByName(args.get("type") + "");
        if(args.containsKey("location")){
            location = (Location) args.get("location");
        }
        return new Generator(location, type);
    }
}
