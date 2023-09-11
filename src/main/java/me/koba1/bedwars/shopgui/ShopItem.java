package me.koba1.bedwars.shopgui;

import lombok.Getter;
import me.koba1.bedwars.configitem.ConfigItem;
import me.koba1.bedwars.configs.files.ShopCategoryFile;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.shopgui.categories.ShopCategory;
import me.koba1.bedwars.utils.Formatter;
import me.koba1.bedwars.utils.Playertools;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ShopItem {

    @Getter
    private String name;
    @Getter
    private ConfigurationSection section;
    @Getter
    private ShopCategory category;
    @Getter
    private ConfigItem previewItem;
    @Getter
    private int slot;
    @Getter
    private boolean permanent;
    @Getter
    private boolean autoequip;
    @Getter
    private boolean downgradable;
    @Getter
    private ShopItem.CostCurrency currency;
    @Getter
    private int cost;
    /**
     * Priority 1 is lower than priority 100
     * Priority 1 < Priority 100
     */
    @Getter
    private int tier;
    @Getter
    private List<ConfigItem> buyItems;

    public ShopItem(ShopCategory category, ConfigurationSection section, String name, int priority) {
        this.buyItems = new ArrayList<>();
        this.section = section;
        this.category = category;
        this.name = name;
        this.tier = priority;
        reload();
    }

    public void reload() {
        ShopCategoryFile.getConfig().reload();
        this.section = ShopCategoryFile.get().getConfigurationSection(section.getCurrentPath());

        this.slot = ShopCategoryFile.get().getInt(
                category.getName() + ".category_content." + this.name + ".content_settings.content_slot");
        this.permanent = ShopCategoryFile.get().getBoolean(
                category.getName() + ".category_content." + this.name + ".content_settings.is_permanent");
        this.downgradable = ShopCategoryFile.get().getBoolean(
                category.getName() + ".category_content." + this.name + ".content_settings.is_downgradable");

        previewItem = new ConfigItem(section.getConfigurationSection("tier_item"));
        cost = section.getInt("tier_settings.cost");
        currency = ShopItem.CostCurrency.getTypeByName(section.getString("tier_settings.currency"));
        if (currency == null) {
            currency = ShopItem.CostCurrency.IRON;
        }

        for (String buyItem : section.getConfigurationSection("buy_items").getKeys(false)) {
            ConfigItem item = new ConfigItem(section.getConfigurationSection("buy_items." + buyItem));
            buyItems.add(item);
        }
    }

    public ConfigItem language(Player player) {
        BedwarsPlayer bwPlayer = BedwarsPlayer.getPlayer(player);
        if (bwPlayer == null) return this.previewItem;

        String displayName = bwPlayer.getLanguage().getFile().get().getString("shop_items_messages." + category.getName() + ".content_item_" + name + "_name");
        List<String> lore = bwPlayer.getLanguage().getFile().get().getStringList("shop_items_messages." + category.getName() + ".content_item_" + name + "_lore");

        String buyStatus;
        if (getHighestPriority() == this.tier && hasItem(player, getHighestTierItem()) && getHighestPriority() > 1) {
            buyStatus = Messages.SHOP_LORE_STATUS_TIER_MAXED.language(player).queue();
        } else if (isPermanent() && hasItem(player, this) && isArmor()) {
            buyStatus = Messages.SHOP_LORE_STATUS_ARMOR.language(player).queue();
        } else if (isPermanent() && isArmor() && hasHigherArmor(player)) {
            buyStatus = Messages.UPGRADES_LORE_UNLOCKED.language(player).queue();
        } else if (isPermanent() && hasHigherArmor(player) && isArmor()) {
            buyStatus = Messages.UPGRADES_LORE_UNLOCKED.language(player).queue();
        } else if (canBuy(player).isCanBuy()) {
            buyStatus = Messages.UPGRADES_LORE_CLICK_BUY.language(player).queue();
        } else {
            buyStatus = Messages.SHOP_LORE_STATUS_CANT_AFFORD.language(player).setCurrency(currency).queue();
        }

        lore = Formatter.format(lore)
                .replace("%cost%", currency.getColorCode() + this.cost + "")
                .replace("%currency%", Formatter.format(currency.name()).toFirstUpper().string())
                .replace("%quick_buy%", Messages.SHOP_LORE_QUICK_REMOVE.language(player).queue())
                .replace("%buy_status%", buyStatus)
                .applyColor().list();


        displayName = Formatter.format(displayName).replace("%color%",
                canBuy(player).isCanBuy()
                        ? Messages.CAN_BUY_COLOR.language(player).queue()
                        : Messages.CANT_BUY_COLOR.language(player).queue()
        ).applyColor().string();

        if (isUpgradable()) {
            displayName = Formatter.format(displayName)
                    .replace("%tier%", Playertools.IntegerToRomanNumeral(tier)).applyColor().string();

            lore = Formatter.format(lore).replace("%tier%", Playertools.IntegerToRomanNumeral(tier)).applyColor().list();
        }


        previewItem.setDisplayName(displayName);
        previewItem.setLore(lore);

        return this.previewItem;
    }

    public int getHighestPriority() {
        int prio = 0;
        for (ShopItem categoryItem : this.category.getItems()) {
            if (categoryItem.getName().equalsIgnoreCase(this.name)) {
                if (categoryItem.tier > prio) {
                    prio = categoryItem.getTier();
                }
            }
        }

        return prio;
    }

    public boolean isArmor() {
        for (ConfigItem buyItem : buyItems) {
            String name = buyItem.getItemStack().getType().name().toLowerCase();
            if (!name.contains("helmet") && !name.contains("chestplate") && !name.contains("leggings") && !name.contains("boots")) {
                return false;
            }
        }

        return true;
    }

    public ShopItem getHighestTierItem() {
        int prio = 0;
        ShopItem item = null;
        for (ShopItem categoryItem : this.category.getItems()) {
            if (categoryItem.getName().equalsIgnoreCase(this.name)) {
                if (categoryItem.getTier() > prio) {
                    prio = categoryItem.getTier();
                    item = categoryItem;
                }
            }
        }

        return item;
    }

    public boolean hasHigherArmor(Player player) {
        for (ShopItem categoryItem : category.getItems()) {
            int weight = ShopCategoryFile.get()
                    .getInt(category.getName() + ".category_content." + categoryItem.name + ".content_settings.weight");
            int weight2 = ShopCategoryFile.get()
                    .getInt(category.getName() + ".category_content." + this.name + ".content_settings.weight");
            //System.out.println(categoryItem.name + " " + categoryItem.tier + " " + this.tier);

            if (isArmor() && categoryItem.isArmor() && weight > weight2 && hasItem(player, categoryItem)) {
                return true;
            }
        }
        return false;
    }

    public ShopItem getHigher() {
        for (ShopItem categoryItem : category.getItems()) {
            if (categoryItem.getName().equalsIgnoreCase(this.name) && categoryItem.tier == this.tier + 1) {
                return categoryItem;
            }
        }
        return this;
    }

    public ShopItem getLowest() {
        int priority = Integer.MAX_VALUE;
        ShopItem lowest = this;
        for (ShopItem categoryItem : category.getItems()) {
            if (categoryItem.getName().equalsIgnoreCase(this.name) && categoryItem.tier < priority) {
                priority = categoryItem.tier;
                lowest = categoryItem;
            }
        }
        return lowest;
    }

    public static ShopItem getShopItemByType(Material mat) {
        for (ShopCategory value : ShopCategoryManager.getInstance().getCategories().values()) {
            for (ShopItem item : value.getItems()) {
                if (item.getShopItem().getType() == mat) {
                    return item;
                }
            }
        }

        return null;
    }

    public static ShopItem getShopItemByItemStack(ItemStack is) {
        for (ShopCategory value : ShopCategoryManager.getInstance().getCategories().values()) {
            for (ShopItem item : value.getItems()) {
                if (item.getShopItem().getType() == is.getType()) {
                    return item;
                }
            }
        }

        return null;
    }

    public static ShopItem getShopItemByBuyItems(Material type) {
        for (ShopCategory value : ShopCategoryManager.getInstance().getCategories().values()) {
            for (ShopItem item : value.getItems()) {
                for (ConfigItem buyItem : item.getBuyItems()) {
                    if (buyItem.getItemStack().getType() == type) {
                        return item;
                    }
                }
            }
        }

        return null;
    }

    public ShopItem getTierItem(Player player) {
        if (!isUpgradable()) return this;

        // WOODEN TOOL

        // current tier ami nála van
        ShopItem playerItem = null;
        for (ItemStack content : player.getInventory().getContents()) {
            if (content == null) continue;

            ShopItem item = ShopItem.getShopItemByBuyItems(content.getType());
            if (item == null) continue;

            if (item.getName().equalsIgnoreCase(this.name)) {
                playerItem = item;
            }
        }

        if (playerItem == null) {
            return null;
        }

        for (ShopItem categoryItem : category.getItems()) {
            if (categoryItem == null) continue;

            if (!categoryItem.name.equalsIgnoreCase(playerItem.name)) continue;


            if (getHighestPriority() == categoryItem.tier) {
                return getHighestTierItem();
            }

            if (playerItem.tier + 1 == categoryItem.tier) {
                return categoryItem;
            }
        }

        return null;
    }

    public boolean isUpgradable() {
        for (ShopItem categoryItem : category.getItems()) {
            if (categoryItem == this) continue;
            if (categoryItem.getName().equalsIgnoreCase(this.name)) {
                return true;
            }
        }


        return false;
    }

    public boolean hasItem(Player player, ShopItem item) {
        boolean vanNala = false;
        List<Material> contents = Arrays.stream(player.getInventory().getStorageContents())
                .filter(Objects::nonNull)
                .map(ItemStack::getType)
                .toList();
        for (ConfigItem buyItem : this.buyItems) {
            ShopItem item2 = getShopItemByBuyItems(buyItem.getItemStack().getType());
            if (item2 != null && item2.tier < item.tier) {
                continue;
            }
            for (Material content : contents) {
                if (content == buyItem.getItemStack().getType()) {
                    vanNala = true;
                }
            }
        }

        if (!vanNala) {
            List<Material> armors = Arrays.stream(player.getInventory().getArmorContents())
                    .filter(Objects::nonNull)
                    .map(ItemStack::getType)
                    .toList();
            boolean hasArmor = true;
            for (ConfigItem buyItem : item.getBuyItems()) {
                if (!hasArmor(player, buyItem.getItemStack())) {
                    hasArmor = false;
                }
            }

            return hasArmor;
        }
        return true;
    }

    private boolean hasArmor(Player player, ItemStack is) {
        if (is.getType().name().endsWith("BOOTS")) {
            return player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == is.getType();
        } else if (is.getType().name().endsWith("LEGGINGS")) {
            return player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType() == is.getType();
        } else if (is.getType().name().endsWith("CHESTPLATE")) {
            return player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == is.getType();
        } else if (is.getType().name().endsWith("HELMET")) {
            return player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() == is.getType();
        }

        return false;
    }

    public BuyState canBuy(Player player) {
        boolean hasCurrency;
        boolean hasHigher = false;
        boolean isMaxed = false;
        boolean isUnlocked = false;
        boolean isEquipped = false;
        ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);
        switch (currency) {
            case IRON:
                break;
            case GOLD:
                iron = new ItemStack(Material.GOLD_INGOT, 1);
                break;
            case DIAMOND:
                iron = new ItemStack(Material.DIAMOND, 1);
                break;
            case EMERALD:
                iron = new ItemStack(Material.EMERALD, 1);
                break;
            case NETHERITE:
                iron = new ItemStack(Material.NETHERITE_INGOT, 1);
                break;
        }
        hasCurrency = player.getInventory().containsAtLeast(iron, cost);

        if(hasHigherArmor(player)) {
            hasHigher = true;
        } else if (getHighestPriority() == this.tier && hasItem(player, getHighestTierItem()) && getHighestPriority() > 1) {
            isMaxed = true;
        } else if (isPermanent() && hasItem(player, this) && isArmor()) {
            isEquipped = true;
        } else if (isPermanent() && isArmor() && hasHigherArmor(player)) {
            isUnlocked = true;
        } else if (isPermanent() && hasHigherArmor(player) && isArmor()) {
            isUnlocked = true;
        }

        if(isUnlocked)
            return BuyState.UNLOCKED;
        if(isMaxed)
            return BuyState.MAXED;
        if(isEquipped)
            return BuyState.EQUIPPED;
        if(hasHigher)
            return BuyState.HAS_HIGHER_ARMOR;
        if(!hasCurrency)
            return BuyState.INSUFF_MONEY;

        return BuyState.SUCCESSFULLY;
    }

    public ItemStack getShopItem() {
        return this.previewItem.getItemStack();
    }

    public int getRemainingItem(Player player) {
        int amounts = 0;
        for (ItemStack items : player.getInventory().getContents()) {
            if (Playertools.isSimilar(new ItemStack(currency.getMaterial(), 1), items)) {
                amounts += items.getAmount();
            }
        }

        return this.cost - amounts;
    }

    private void removeItem(Player player, ShopItem.CostCurrency currency, int amount) {
        ItemStack item;
        switch (currency) {
            default -> item = new ItemStack(Material.IRON_INGOT, amount);
            case GOLD -> item = new ItemStack(Material.GOLD_INGOT, amount);
            case DIAMOND -> item = new ItemStack(Material.DIAMOND, amount);
            case EMERALD -> item = new ItemStack(Material.EMERALD, amount);
            case NETHERITE -> item = new ItemStack(Material.NETHERITE_INGOT, amount);
        }
        int counter = 0;
        int amount2 = amount;
        ItemStack[] contents = player.getInventory().getContents();
        boolean checkOffHand = false;
        if (player.getInventory().getItemInOffHand().getType() != Material.LEGACY_AIR) {
            List<ItemStack> stacks = new ArrayList<>(Arrays.stream(contents).toList());
            stacks.add(player.getInventory().getItemInOffHand());
            contents = stacks.toArray(new ItemStack[0]);
            checkOffHand = true;
        }

        for (ItemStack is : contents) {
            if (is == null) continue;
            if (is.getType() != item.getType()) continue;

            ItemStack clone = is.clone();
            clone.setAmount(Math.min(amount, clone.getAmount()));

            HashMap<Integer, ItemStack> hashMap = player.getInventory().removeItem(clone);
            if (!hashMap.isEmpty()) {
                if (checkOffHand) {
                    for (ItemStack value : hashMap.values()) {
                        ItemStack offHand = player.getInventory().getItemInOffHand();
                        int yes = offHand.getAmount();
                        yes -= value.getAmount();
                        offHand.setAmount(yes);
                        player.getInventory().setItemInOffHand(offHand);
                    }
                }
            }

            counter += clone.getAmount();
            amount -= clone.getAmount();
            if (counter >= amount2)
                break;
        }
    }

    public void buy(Player player) {
        boolean boughtItem = true;
        for (ConfigItem buyItem : buyItems) {
            if (!hasItem(player, this)) {
                if (buyItem.isAutoEquip()) {
                    int weight = ShopCategoryFile.get()
                            .getInt(category.getName() + ".category_content." + this.name + ".content_settings.weight");

                    boolean canBuy = true;
                    List<ItemStack> mat = Arrays.stream(player.getInventory().getArmorContents()).filter(Objects::nonNull).toList();
                    for (ItemStack itemStack : mat) {
                        ShopItem matItem = ShopItem.getShopItemByBuyItems(itemStack.getType());
                        if (matItem == null) {
                            continue;
                        }

                        if (!ShopCategoryFile.get()
                                .contains(category.getName() + ".category_content." + matItem.getName() + ".content_settings.weight")) {
                            continue;
                        }

                        int weight2 = ShopCategoryFile.get()
                                .getInt(category.getName() + ".category_content." + matItem.getName() + ".content_settings.weight");

                        if (weight2 > weight) {
                            canBuy = false;
                        }
                    }

                    if (canBuy) {
                        setArmor(player, buyItem.getItemStack());
                    }
                    continue;
                }
            }

            if (isUpgradable()) {
                int slot = 99999;
                for (ItemStack content : player.getInventory().getContents()) {
                    if (content == null) continue;
                    ShopItem shopItem = ShopItem.getShopItemByBuyItems(content.getType());
                    if (shopItem == null) continue;
                    if (shopItem.getName().equalsIgnoreCase(this.name)) {
                        slot = player.getInventory().first(content.getType());
                    }
                }
                if (slot == 99999) {
                    player.getInventory().addItem(buyItem.getItemStack());
                    boughtItem = true;
                } else {
                    player.getInventory().setItem(slot, buyItem.getItemStack());
                    boughtItem = true;
                }
            } else {
                if (isPermanent() && hasItem(player, this)) {
                    boughtItem = false;
                } else {
                    player.getInventory().addItem(buyItem.getItemStack());
                    boughtItem = true;
                }
            }
        }

        if (boughtItem) {
            String name = this.previewItem.getItemStack().hasItemMeta() && this.previewItem.getItemStack().getItemMeta().hasDisplayName()
                    ? this.previewItem.getItemStack().getItemMeta().getDisplayName()
                    : Formatter.format(this.previewItem.getItemStack().getType().name()).toFirstUpper().string();
            player.sendMessage(Messages.SHOP_NEW_PURCHASE.language(player)
                    .setItem(ChatColor.stripColor(name))
                    .queue());

            removeItem(player, currency, cost);
        }


        BedwarsPlayer bwPlayer = BedwarsPlayer.getPlayer(player);
        if (bwPlayer == null) return;
        player.openInventory(bwPlayer.getSelectedShopCategory().getInventory(player));
    }

    public static ShopItem getShopItem(String path) {
        for (ShopCategory value : ShopCategoryManager.getInstance().getCategories().values()) {
            for (ShopItem item : value.getItems()) {
                String itemPath = item.category.getName() + ".category_content." + item.getName();
                if (path.equalsIgnoreCase(itemPath)) {
                    return item;
                }
            }
        }

        return null;
    }

    public static ShopItem getShopItemFromGUI(ItemStack is) {
        for (ShopCategory value : ShopCategoryManager.getInstance().getCategories().values()) {
            for (ShopItem item : value.getItems()) {
                if (item.getShopItem().getType() == is.getType()) {
                    if (item.getShopItem().hasItemMeta()
                            && item.getShopItem().getItemMeta().hasDisplayName()
                            && is.hasItemMeta()
                            && is.getItemMeta().hasDisplayName()
                            && item.getShopItem().getItemMeta().getDisplayName().equalsIgnoreCase(is.getItemMeta().getDisplayName())) {
                        return item;
                    }
                }
            }
        }

        return null;
    }

    private void setArmor(Player p, ItemStack is) {
        if (is.getType().name().endsWith("BOOTS")) {
            p.getInventory().setBoots(is);
        } else if (is.getType().name().endsWith("LEGGINGS")) {
            p.getInventory().setLeggings(is);
        } else if (is.getType().name().endsWith("CHESTPLATE")) {
            p.getInventory().setChestplate(is);
        } else if (is.getType().name().endsWith("HELMET")) {
            p.getInventory().setHelmet(is);
        }
    }

    public static enum CostCurrency {
        IRON("§f", Material.IRON_INGOT),
        GOLD("§6", Material.GOLD_INGOT),
        DIAMOND("§b", Material.DIAMOND),
        EMERALD("§2", Material.EMERALD),
        NETHERITE("§7", Material.NETHERITE_INGOT);

        @Getter
        private String colorCode;
        @Getter
        private Material material;

        CostCurrency(String colorCode, Material material) {
            this.colorCode = colorCode;
            this.material = material;
        }

        public static CostCurrency getTypeByName(String name) {
            for (CostCurrency value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }

            return null;
        }
    }

    public static enum BuyState {

        INSUFF_MONEY(false),
        UNLOCKED(false),
        MAXED(false),
        HAS_HIGHER_ARMOR(false),
        EQUIPPED(false),
        SUCCESSFULLY(true);

        @Getter private boolean canBuy;
        @Getter private Messages message;

        BuyState(boolean value, Messages message) {
            this.message = message;
            this.canBuy = value;
        }

        BuyState(boolean value) {
            this.canBuy = value;
        }

        public static BuyState getStateByName(String name) {
            for (BuyState value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }

            return null;
        }

    }
}
