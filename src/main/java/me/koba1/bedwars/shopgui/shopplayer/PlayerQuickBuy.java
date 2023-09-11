package me.koba1.bedwars.shopgui.shopplayer;

import lombok.Getter;
import me.koba1.bedwars.configs.files.ShopCategoryFile;
import me.koba1.bedwars.shopgui.ShopItem;
import me.koba1.bedwars.utils.Playertools;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerQuickBuy {

    private BedwarsPlayer player;
    @Getter
    private HashMap<Integer, ShopItem> items;

    public PlayerQuickBuy(BedwarsPlayer player) {
        this.player = player;
        items = new HashMap<>();
        setupDefault();
    }

    public void add(ItemStack is, int slot) {
    }

    public void setupDefault() {
        for (String defaults : ShopCategoryFile.get().getConfigurationSection("quick_buy_defaults").getKeys(false)) {
            String path = ShopCategoryFile.get().getString("quick_buy_defaults." + defaults + ".path");
            int slot = ShopCategoryFile.get().getInt("quick_buy_defaults." + defaults + ".slot");

            ShopItem item = ShopItem.getShopItem(path);
            if(item != null) {
                items.put(slot, item);
            }
        }
    }

    public void remove(ItemStack is) {
        for (Map.Entry<Integer, ShopItem> hash : new HashMap<Integer, ShopItem>(items).entrySet()) {
            if(Playertools.equals(is, hash.getValue().getShopItem())) {
                this.items.remove(hash.getKey());
            }
        }
    }

    public void load(String json) {

    }

    public String getAsJSON() {
        return null;
    }
}
