package me.koba1.bedwars.shopgui.categories;

import lombok.Getter;
import me.koba1.bedwars.configitem.ConfigItem;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.shopgui.ShopCategories;
import me.koba1.bedwars.shopgui.ShopCategoryManager;
import me.koba1.bedwars.shopgui.ShopItem;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class ShopQuickBuyCategory implements ShopCategories {

    @Getter private ConfigItem item;

    public ShopQuickBuyCategory(ConfigItem item) {
        this.item = item;
    }

    @Override
    public Inventory getInventory(Player p) {
        Inventory inv = ShopCategoryManager.getInstance().getHeader(p);
        BedwarsPlayer bedwars = BedwarsPlayer.getPlayer(p);

        for (Map.Entry<Integer, ShopItem> hash : bedwars.getQuickbuyManager().getItems().entrySet()) {
           if(hash.getValue().isUpgradable()) {
                if(hash.getValue().getTierItem(p) == null) continue;
                inv.setItem(hash.getKey(), hash.getValue().getTierItem(p).language(p).getItemStack());
            } else {
                inv.setItem(hash.getKey(), hash.getValue().language(p).getItemStack());
            }
        }

        int[] availableSlots = new int[]{19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

        for (int slot : availableSlots) {
            if(inv.getItem(slot) == null) {
                inv.setItem(slot, ShopCategoryManager.getInstance().getQuickbuyEmpty()
                        .setDisplayName(Messages.QUICK_BUY_EMPTY_ITEM_NAME.language(p).queue())
                        .setLore(Messages.QUICK_BUY_EMPTY_ITEM_LORE.language(p).queueList())
                        .getItemStack());
            }
        }

        return inv;
    }
}
