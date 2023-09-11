package me.koba1.bedwars.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.koba1.bedwars.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class FormatterUtils {

    private static Main m = Main.getPlugin(Main.class);
    private String string;
    private List<String> list;

    public FormatterUtils(String string) {
        this.string = string;
    }

    public FormatterUtils(List<String> list) {
        this.list = list;
    }

    public FormatterUtils replace(String target, String replacement) {
        if (isString())
            this.string = string.replace(target, replacement);
        else {
            this.list = replaceList(target, replacement);
        }
        return this;
    }

    public FormatterUtils replaceFirst(String target, String replacement) {
        if (isString())
            this.string = string.replaceFirst(target, replacement);
        else {
            this.list = replaceListFirst(target, replacement);
        }
        return this;
    }

    public FormatterUtils toFirstUpper() {
        this.string = this.string.substring(0, 1).toUpperCase() + this.string.substring(1).toLowerCase();
        return this;
    }

    public FormatterUtils papi(Player player) {
        if(isString()) {
            this.string = PlaceholderAPI.setPlaceholders(player, this.string);
        } else {
            this.list = this.list.stream().map(line -> PlaceholderAPI.setPlaceholders(player, line)).toList();
        }

        return this;
    }

    public FormatterUtils change(List<String> list) {
        if(!isString()) {
            this.list = list;
        }

        return this;
    }

    public FormatterUtils applyColor() {
        if(isString()) {
            this.string = Formatter.applyColor(this.string);
        } else {
            this.list = this.list.stream().map(Formatter::applyColor).toList();
        }
        return this;
    }

    public FormatterUtils strip(String target) {
        replace(target, "");
        return this;
    }

    public String string() {
        return this.string;
    }

    public List<String> list() {
        return this.list;
    }

    public NamespacedKey namespacedKey() {
        return new NamespacedKey(m, this.string);
    }

    public List<Component> componentList() {
        List<Component> comps = new ArrayList<>();
        for (String s : this.list) {
            comps.add(Component.text(s));
        }
        return comps;
    }

    private boolean isString() {
        return this.string != null && this.list == null;
    }

    private List<String> replaceList(String target, String replacement) {
        List<String> str = new ArrayList<>();
        if(this.list == null) return str;
        for (String s : this.list) {
            str.add(s.replace(target, replacement));
        }

        return str;
    }

    private List<String> replaceListFirst(String target, String replacement) {
        List<String> str = new ArrayList<>();
        if(this.list == null) return str;
        boolean replacedFirst = false;
        for (String s : this.list) {
            if(s.contains(target) && !replacedFirst) {
                str.add(s.replace(target, replacement));
                replacedFirst = true;
            }
            str.add(s.replace(target, replacement));
        }

        return str;
    }

    public List<String> replaceList(List<String> original, String target, List<String> replacement) {
        List<String> newLore = new ArrayList<>();
        for (String s : original) {
            if (s.equalsIgnoreCase(target)) {
                newLore.addAll(replacement);
                continue;
            }

            newLore.add(s);
        }
        return newLore;
    }


    public static ItemStack replaceLore(ItemStack is, String target, List<String> replacement) {
        List<String> newLore = new ArrayList<>();
        if(is.hasItemMeta() && is.getItemMeta().hasLore()) {
            for (String s : is.getItemMeta().getLore()) {
                if (s.equalsIgnoreCase(target)) {
                    newLore.addAll(replacement);
                    continue;
                }

                newLore.add(s);
            }
        }
        ItemMeta im = is.getItemMeta();
        im.setLore(newLore);
        is.setItemMeta(im);

        return is;
    }
}
