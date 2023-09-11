package me.koba1.bedwars.shopgui.events;

import me.koba1.bedwars.configs.messages.MessageFile;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.shopgui.ShopCategoryManager;
import me.koba1.bedwars.shopgui.ShopItem;
import me.koba1.bedwars.shopgui.categories.ShopCategory;
import me.koba1.bedwars.utils.Playertools;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShopClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getCurrentItem() == null) return;
        if(e.getWhoClicked() instanceof Player) {
            Player p = (((Player) e.getWhoClicked()).getPlayer());
            if(p == null) return;
            BedwarsPlayer bwPlayer = BedwarsPlayer.getPlayer(p);
            if(bwPlayer == null) return;

            MessageFile msgs = bwPlayer.getLanguage().getFile().getConfig();

            for (ShopCategory value : ShopCategoryManager.getInstance().getCategories().values()) {
                if(Playertools.isSimilar(value.getItem().getItemStack(), e.getCurrentItem())) {
                    bwPlayer.setSelectedShopCategory(value);
                    p.openInventory(value.getInventory(p));
                    return;
                }
            }

            if(Playertools.isSimilar(ShopCategoryManager.getInstance().getQuickBuyCategory().getItem().getItemStack(), e.getCurrentItem())) {
                bwPlayer.setSelectedShopCategory(ShopCategoryManager.getInstance().getQuickBuyCategory());
                p.openInventory(ShopCategoryManager.getInstance().getQuickBuyCategory().getInventory(p));
                return;
            }

            if(e.getView().getTitle().equalsIgnoreCase(msgs.getString("shop_items_messages.inventory_name"))) {
                e.setCancelled(true);
                if(e.getClickedInventory() != e.getView().getTopInventory()) return;
                //ShopQuickBuyCategory cat = ShopCategoryManager.getInstance().getQuickBuyCategory();
                ShopItem item = ShopItem.getShopItemByItemStack(e.getCurrentItem());
                if(item == null) {
                    return;
                }

                if(!item.canBuy(p).isCanBuy()) {
                    if(item.canBuy(p) == ShopItem.BuyState.INSUFF_MONEY) {
                        p.sendMessage(Messages.SHOP_INSUFF_MONEY.language(p)
                                .setCurrency(item.getCurrency())
                                .setAmount(item.getRemainingItem(p))
                                .queue());
                    }
                    return;
                }

                item.buy(p);
                return;
            }
            for (String category : msgs.get().getConfigurationSection("shop_items_messages").getKeys(false)) {
                String title = msgs.getString("shop_items_messages." + category + ".inventory_name");

                if(e.getView().getTitle().equalsIgnoreCase(title)) {
                    e.setCancelled(true);
                    if(e.getClickedInventory() != e.getView().getTopInventory()) return;
                    ShopItem item = ShopItem.getShopItemByItemStack(e.getCurrentItem());
                    if(item == null) {
                        return;
                    }


                    if(!item.canBuy(p).isCanBuy()) {
                        if(item.canBuy(p) == ShopItem.BuyState.INSUFF_MONEY) {
                            p.sendMessage(Messages.SHOP_INSUFF_MONEY.language(p)
                                    .setCurrency(item.getCurrency())
                                    .setAmount(item.getRemainingItem(p))
                                    .queue());
                        }
                        return;
                    }

                    item.buy(p);
                }
            }
        }
    }
}
