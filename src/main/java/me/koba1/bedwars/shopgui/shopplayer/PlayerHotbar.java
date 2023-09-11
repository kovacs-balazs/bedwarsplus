package me.koba1.bedwars.shopgui.shopplayer;

import me.koba1.bedwars.shopgui.ShopCategoryManager;
import me.koba1.bedwars.shopgui.categories.ShopCategory;
import me.koba1.bedwars.utils.Playertools;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerHotbar {

    private BedwarsPlayer player;
    private HashMap<Integer, ShopCategory> items;

    public PlayerHotbar(BedwarsPlayer player) {
        this.player = player;
    }

    public void set(ItemStack is, int slot) {

    }
}
