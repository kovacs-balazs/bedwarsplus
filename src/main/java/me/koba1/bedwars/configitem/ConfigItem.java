package me.koba1.bedwars.configitem;

import lombok.Getter;
import me.koba1.bedwars.utils.Formatter;
import me.koba1.bedwars.utils.Playertools;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ConfigItem {

    @Getter private ConfigurationSection section;
    private ItemStack is;
    private ItemMeta im;
    @Getter private int slot;
    @Getter private boolean autoEquip;
    private Integer[] slots;

    public ConfigItem(ConfigurationSection section) {
        this.section = section;

        String slotLine = section.getString("slot");
        if(slotLine == null)
            slotLine = section.getString("slots");

        if(slotLine != null) {
            if (slotLine.contains("-")) {
                int minSlot = Integer.parseInt(slotLine.split("-")[0]);
                int highSlot = Integer.parseInt(slotLine.split("-")[1]);

                List<Integer> ints = new ArrayList<>();
                for (int i = minSlot; i <= highSlot; i++) {
                    ints.add(i);
                }

                this.slots = ints.toArray(new Integer[0]);
            } else {
                slot = Integer.parseInt(slotLine);
            }
        }

        autoEquip = section.getBoolean("auto_equip");

        create();
        setup();
    }

    private void create() {
        Material material = Material.matchMaterial(section.getString("material").toUpperCase());
        if(material == null)
            material = Material.STONE;
        int amount = section.getInt("amount");
        this.is = new ItemStack(material, amount);
        this.im = this.is.getItemMeta();
    }

    private void setup() {
        String displayName = section.getString("display_name");
        if(displayName != null)
            im.displayName(Component.text(Formatter.applyColor(displayName)));

        List<String> lore = section.getStringList("lore");
        im.lore(Formatter.format(lore).applyColor().componentList());

        int customModelData = section.getInt("custom_model_data");
        im.setCustomModelData(customModelData);

        boolean glowing = section.getBoolean("enchanted");
        if(glowing) {
            Glow glow = new Glow(Playertools.getKey("glowenchantment"));
            im.addEnchant(glow, 1, true);
        }

        boolean unbreakable = section.getBoolean("unbreakable");
        if(unbreakable) {
            im.setUnbreakable(true);
        }

        List<String> enchantments = section.getStringList("enchantments");
        if(!enchantments.isEmpty()) {
            for (String enchantment : enchantments) {
                if(!enchantment.contains(";")) continue;

                int level = Integer.parseInt(enchantment.split(";")[1]);
                Enchantment enc = Enchantment.getByName(Formatter.format(enchantment.split(";")[0].toUpperCase()).string());
                if(enc == null) continue;

                im.addEnchant(enc, level, true);
            }
        }
    }

    public ConfigItem setLore(List<String> lore) {
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ConfigItem setDisplayName(String displayName) {
        im.displayName(Component.text(displayName));
        is.setItemMeta(im);
        return this;
    }

    public ConfigItem setDisplayNameColor(String color) {
        String displayName = ChatColor.stripColor(im.getDisplayName());
        im.displayName(Component.text(color + displayName));
        is.setItemMeta(im);
        return this;
    }

    public ConfigItem build() {
        is.setItemMeta(im);
        return this;
    }

    public ItemStack getItemStack() {
        is.setItemMeta(im);
        return is;
    }

    public Inventory add(Inventory inv) {
        if(hasMultipleSlot()) {
            for (int i : slots) {
                inv.setItem(i, getItemStack());
            }
        } else {
            if(slot > inv.getSize() - 1) return inv;
            inv.setItem(slot, getItemStack());
        }
        return inv;
    }

    public boolean hasMultipleSlot() {
        return this.slots.length != 0;
    }
}
