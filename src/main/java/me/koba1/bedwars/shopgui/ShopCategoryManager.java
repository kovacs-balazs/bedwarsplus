package me.koba1.bedwars.shopgui;

import lombok.Getter;
import me.koba1.bedwars.configitem.ConfigItem;
import me.koba1.bedwars.configs.files.ShopCategoryFile;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.shopgui.categories.ShopCategory;
import me.koba1.bedwars.shopgui.categories.ShopQuickBuyCategory;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class ShopCategoryManager {

    @Getter private HashMap<String, ShopCategory> categories;
    private ConfigItem selectedSeparator;
    private ConfigItem regularSeparator;
    @Getter private ConfigItem quickbuyEmpty;
    private ConfigItem quickBuyItem;
    @Getter private ShopQuickBuyCategory quickBuyCategory;
    @Getter private static ShopCategoryManager instance;

    public ShopCategoryManager() {
        instance = this;
        this.categories = new HashMap<>();

        reload();
    }

    public void reload() {
        ShopCategoryFile.getConfig().reload();
        selectedSeparator = new ConfigItem(ShopCategoryFile.get().getConfigurationSection("shop_settings.selected_separator_item"));
        regularSeparator = new ConfigItem(ShopCategoryFile.get().getConfigurationSection("shop_settings.regular_separator_item"));
        quickbuyEmpty = new ConfigItem(ShopCategoryFile.get().getConfigurationSection("shop_settings.quick_buy_empty_item"));
        quickBuyItem = new ConfigItem(ShopCategoryFile.get().getConfigurationSection("shop_settings.quick_buy_category"));

        for (String key : ShopCategoryFile.get().getKeys(false)) {
            if(!key.endsWith("_category")) continue;

            ShopCategory category = new ShopCategory(key);

        }

        this.quickBuyCategory = new ShopQuickBuyCategory(quickBuyItem);
    }

    public Inventory getHeader(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, Messages.INVENTORY_NAME.language(p).queue());


        BedwarsPlayer bedwars = BedwarsPlayer.getPlayer(p);

        inv.setItem(quickBuyCategory.getItem().getSlot(), quickBuyCategory.getItem()
                .setDisplayName(Messages.QUICK_BUY_ITEM_NAME.language(p).queue())
                .setLore(Messages.QUICK_BUY_ITEM_LORE.language(p).queueList())
                .getItemStack());
        if(bedwars.getSelectedShopCategory() == ShopCategoryManager.getInstance().getQuickBuyCategory()) {
            inv.setItem(quickBuyCategory.getItem().getSlot() + 9, selectedSeparator
                    .setDisplayName(Messages.SEPARATOR_ITEM_NAME.language(p).queue())
                    .setLore(Messages.SEPARATOR_ITEM_LORE.language(p).queueList())
                    .getItemStack());
        } else {
            inv.setItem(quickBuyCategory.getItem().getSlot() + 9, regularSeparator
                    .setDisplayName(Messages.SEPARATOR_ITEM_NAME.language(p).queue())
                    .setLore(Messages.SEPARATOR_ITEM_LORE.language(p).queueList())
                    .getItemStack());
        }

        for (ShopCategory value : categories.values()) {

            inv.setItem(value.getSlot(), value.getItem()
                    .setDisplayName(bedwars.getLanguage().getFile().getString("shop_items_messages." + value.getName() + ".category_item_name"))
                    .setLore(bedwars.getLanguage().getFile().getStringList("shop_items_messages." + value.getName() + ".category_item_lore"))
                    .getItemStack());
            if(bedwars.getSelectedShopCategory().equals(value)) {
                inv.setItem(value.getSlot() + 9, selectedSeparator
                        .setDisplayName(Messages.SEPARATOR_ITEM_NAME.language(p).queue())
                        .setLore(Messages.SEPARATOR_ITEM_LORE.language(p).queueList())
                        .getItemStack());
            } else {
                inv.setItem(value.getSlot() + 9, regularSeparator
                        .setDisplayName(Messages.SEPARATOR_ITEM_NAME.language(p).queue())
                        .setLore(Messages.SEPARATOR_ITEM_LORE.language(p).queueList())
                        .getItemStack());
            }
        }

        return inv;
    }

    public Inventory getHeader(Player p, String title) {
        Inventory inv = Bukkit.createInventory(null, 54, title);


        BedwarsPlayer bedwars = BedwarsPlayer.getPlayer(p);

        inv.setItem(quickBuyCategory.getItem().getSlot(), quickBuyCategory.getItem().getItemStack());
        inv.setItem(quickBuyCategory.getItem().getSlot() + 9, selectedSeparator
                .setDisplayName(Messages.SEPARATOR_ITEM_NAME.language(p).queue())
                .setLore(Messages.SEPARATOR_ITEM_LORE.language(p).queueList())
                .getItemStack());

        for (ShopCategory value : categories.values()) {
            inv.setItem(value.getSlot(), value.getItem().getItemStack());
            if(bedwars.getSelectedShopCategory().equals(value)) {
                inv.setItem(value.getSlot() + 9, selectedSeparator
                        .setDisplayName(Messages.SEPARATOR_ITEM_NAME.language(p).queue())
                        .setLore(Messages.SEPARATOR_ITEM_LORE.language(p).queueList())
                        .getItemStack());
            } else {
                inv.setItem(value.getSlot() + 9, regularSeparator
                        .setDisplayName(Messages.SEPARATOR_ITEM_NAME.language(p).queue())
                        .setLore(Messages.SEPARATOR_ITEM_LORE.language(p).queueList())
                        .getItemStack());
            }
        }

        return inv;
    }

}
