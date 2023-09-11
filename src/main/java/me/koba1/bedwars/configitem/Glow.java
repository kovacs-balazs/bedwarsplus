package me.koba1.bedwars.configitem;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Glow extends Enchantment {
    public Glow(NamespacedKey i) {
        super(i);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean canEnchantItem(ItemStack arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public @NotNull Component displayName(int i) {
        return null;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        return null;
    }

    @Override
    public float getDamageIncrease(int i, @NotNull EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getStartLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isCursed() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTreasure() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public @NotNull String translationKey() {
        return null;
    }
}
