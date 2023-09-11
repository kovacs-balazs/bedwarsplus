package me.koba1.bedwars.shopgui.categories;

import lombok.Getter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.configitem.ConfigItem;
import me.koba1.bedwars.configs.files.ShopCategoryFile;
import me.koba1.bedwars.shopgui.ShopCategories;
import me.koba1.bedwars.shopgui.ShopCategoryManager;
import me.koba1.bedwars.shopgui.ShopItem;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.MessageLanguage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.*;
import java.util.*;

public class ShopCategory implements ShopCategories {

    private static Main m = Main.getPlugin(Main.class);
    @Getter private String name;
    @Getter private ConfigItem item;
    @Getter private int slot;
    @Getter private List<ShopItem> items;

    public ShopCategory(String name) {
        this.name = name;
        this.items = new ArrayList<>();

        reload();
    }

    public void reload() {
        item = new ConfigItem(ShopCategoryFile.get().getConfigurationSection(name + ".category_item"));
        slot = ShopCategoryFile.get().getInt(name + ".category_slot");

        for (String key : ShopCategoryFile.get().getConfigurationSection(name + ".category_content").getKeys(false)) {
            for (String s : ShopCategoryFile.get().getConfigurationSection(name + ".category_content." + key + ".content_tiers").getKeys(false)) {
                ShopItem shopItem = new ShopItem(
                        this,
                        ShopCategoryFile.get().getConfigurationSection(name + ".category_content." + key +".content_tiers." + s),
                        key,
                        s.substring(s.length() - 1).matches("[0-9]+$") ? Integer.parseInt(s.substring(s.length() - 1)) : 0
                );
                items.add(shopItem);
            }
        }

        ShopCategoryManager.getInstance().getCategories().put(name, this);

        setMessages();
    }

    @Override
    public Inventory getInventory(Player player) {
        Inventory inv = ShopCategoryManager.getInstance().getHeader(player, getMessage(player, "inventory_name"));

        List<String> alreadyAdded = new ArrayList<>();
        for (ShopItem shopItem : items) {
            if(alreadyAdded.contains(shopItem.getName())) continue;
            if(shopItem.getTierItem(player) != null) {
                shopItem = shopItem.getTierItem(player);
            }
            inv.setItem(shopItem.getSlot(), shopItem.language(player).getItemStack());
            alreadyAdded.add(shopItem.getName());
        }
        return inv;
    }

    public String getMessage(Player player, String path) {
        BedwarsPlayer bwPlayer = BedwarsPlayer.getPlayer(player);
        return bwPlayer.getLanguage().getFile().getString("shop_items_messages." + name + "." + path);
    }
    

    private HashMap<String, Object> setMessages() {
        HashMap<String, Object> map = new HashMap<>();

        File ymlFile = new File(m.getDataFolder(), "temporary_shop_messages.yml");

        try {
            InputStream in = m.getResource(ymlFile.getName());
            FileOutputStream out = new FileOutputStream(ymlFile);

            if(in == null) return map;
            try {
                int n;
                while ((n = in.read()) != -1) {
                    out.write(n);
                }
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

        } catch (IOException e) {
        }


        YamlConfiguration config = YamlConfiguration.loadConfiguration(ymlFile);

        for (MessageLanguage value : Main.getLanguages().values()) {
            for (String key : config.getConfigurationSection("shop_items_messages." + name).getKeys(false)) {
                if(value.getFile().get().contains("shop_items_messages." + name + "." + key)) continue;
                value.getFile().get().set("shop_items_messages." + name + "." + key, config.get("shop_items_messages." + name + "." + key));
            }

            value.getFile().save();
        }

        ymlFile.delete();

        return map;
    }
}
