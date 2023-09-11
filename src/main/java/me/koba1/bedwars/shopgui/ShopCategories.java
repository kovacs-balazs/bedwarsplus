package me.koba1.bedwars.shopgui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface ShopCategories {

    public abstract Inventory getInventory(Player player);
}
