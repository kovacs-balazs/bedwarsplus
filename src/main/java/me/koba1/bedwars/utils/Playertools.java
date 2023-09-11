package me.koba1.bedwars.utils;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.Config;
import me.koba1.bedwars.configs.ConfigFile;
import me.koba1.bedwars.utils.objects.BedwarsArena;
import me.koba1.bedwars.utils.objects.BedwarsArenaManager;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.gameenums.BedwardGameStates;
import me.koba1.bedwars.utils.objects.gameenums.BedwarsModes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Playertools {

    private static Main m = Main.getPlugin(Main.class);
    public static boolean equals(ItemStack is1, ItemStack is2) {
        if(!is1.displayName().equals(is2.displayName())) return false;
        ItemMeta im1 = is1.getItemMeta();
        ItemMeta im2 = is2.getItemMeta();
        if(im1.getCustomModelData() != im2.getCustomModelData()) return false;
        if(is1.getEnchantments().equals(is2.getEnchantments())) return false;

        if(is1.getType() != is2.getType()) return false;
        return true;
    }

    public static void cleanupConfigFile(ConfigFile config) {
        try {
            config.reload();
            StringBuilder builder = new StringBuilder();
            File file = config.getYmlFile();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String beforeLine = "null";
            String line = null;

            HashMap<String, String> watchingHashmaps = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\n", "");

                String section = getSection(config, Formatter.format(line).strip(":").strip(" ").string());
                boolean blankLine = true;

                if (!beforeLine.replaceAll(" ", "").startsWith("#")
                        && line.replaceAll(" ", "").startsWith("#")
                        && !beforeLine.equalsIgnoreCase("null")) {

                    builder.append("\n");
                }

                if (section != null) {
                    for (Config value : Config.values()) {
                        if (value.getConfigValue().getSection().equalsIgnoreCase(section)) {
                            try {
                                watchingHashmaps.putAll((Map<String, String>) value.getConfigValue().getValue());
                            } catch (ClassCastException e) {
                                watchingHashmaps.putAll(value.getConfigValue().getHashMap());
                            }
                        }
                    }
                } else {
                    //System.out.println("NULL");
                    // SECTION előtt legyen plus sor
                   /* if (!line.replaceAll(" ", "").startsWith("#") && line.endsWith(":")) {
                        System.out.println(line);
                        builder.append("\n");
                    }*/
                }

                // ha blank line ne adjon hozzá többet, ezért continue
                if(line.replaceAll(" ", "").equalsIgnoreCase("")) continue;

                for (Map.Entry<String, String> entry : watchingHashmaps.entrySet()) {
                    if(line.contains(entry.getKey()) && line.contains(entry.getValue())) {
                        blankLine = false;
                    }
                }

                if(blankLine) {
                    if (!beforeLine.replaceAll(" ", "").startsWith("#")
                            && !line.replaceAll(" ", "").startsWith("#")) {
                        builder.append("\n");
                    }
                }

                builder.append(line + "\n");
                beforeLine = line;
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getSection(ConfigFile config, String key) {
        for (String s : config.getFile().getKeys(true)) {
            if(s.endsWith(key)) {
                return s;
            }
        }

        return null;
    }

    public static void cleanupMessages(File file) {
        try {
            StringBuilder builder = new StringBuilder();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String beforeLine = "null";
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("\n", "");
/*                if(!beforeLine.replaceAll(" ", "").startsWith("#")
                        && line.replaceAll(" ", "").startsWith("#")
                        && !beforeLine.equalsIgnoreCase("null")) {

                    builder.append("\n");
                }*/

                // SECTION előtt legyen plus sor
                if(!line.replaceAll(" ", "").startsWith("#") && line.endsWith(":"))
                    builder.append("\n");

                if(line.replaceAll(" ", "").equalsIgnoreCase("")) continue;
/*                if(line.replaceAll(" ", "").equalsIgnoreCase("")) continue;

                if(!beforeLine.replaceAll(" ", "").startsWith("#") && !line.replaceAll(" ", "").startsWith("#"))
                    builder.append("\n");*/

                builder.append(line + "\n");
                beforeLine = line;
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(m, key);
    }

    /**
     * Sends a clickable message to the player
     * @param player the player to send to
     * @param message The message
     * @param command On click command
     */
    public void sendClickableMessage(Player player, String message, String command,String hoverText) {
        TextComponent component = Component.text(message)
                .clickEvent(ClickEvent.runCommand(command))
                .hoverEvent(HoverEvent.showText(Component.text(hoverText)));

        player.sendMessage(component);
    }

    public static boolean isSimilar(ItemStack is1, ItemStack is2) {
        if (is2 == null) {
            return false;
        } else if (is2 == is1) {
            return true;
        } else {
            Material comparisonType = is1.getType().isLegacy() ? Bukkit.getUnsafe().fromLegacy(is1.getData(), true) : is1.getType();
            return comparisonType == is2.getType() && is1.hasItemMeta() == is2.hasItemMeta() && (!is1.hasItemMeta() || Bukkit.getItemFactory().equals(is1.getItemMeta(), is2.getItemMeta()));
        }
    }

    public static String IntegerToRomanNumeral(int input)
    {
        if (input < 1 || input > 3999)
            return "";

        String s = "";

        while (input >= 1000) {
            s += "M";
            input -= 1000;
        } while (input >= 900) {
        s += "CM";
        input -= 900;
    } while (input >= 500) {
        s += "D";
        input -= 500;
    } while (input >= 400) {
        s += "CD";
        input -= 400;
    } while (input >= 100) {
        s += "C";
        input -= 100;
    } while (input >= 90) {
        s += "XC";
        input -= 90;
    } while (input >= 50) {
        s += "L";
        input -= 50;
    } while (input >= 40) {
        s += "XL";
        input -= 40;
    } while (input >= 10) {
        s += "X";
        input -= 10;
    } while (input >= 9) {
        s += "IX";
        input -= 9;
    } while (input >= 5) {
        s += "V";
        input -= 5;
    } while (input >= 4) {
        s += "IV";
        input -= 4;
    } while (input >= 1) {
        s += "I";
        input -= 1;
    }

        return s;
    }


    public BedwarsArenaManager getRandomGame(BedwarsPlayer player, BedwarsModes gameMode) {
        Random rand = new Random();

        List<BedwarsArena> arenas = Main.getBedwarsArena().values().stream().toList();
        int retryLimit = (Main.getInstance().getMaxServerPerMap() * arenas.size()) * 2;

        BedwarsArena arena = arenas.get(rand.nextInt(0,arenas.size()-1));
        while(!arena.getMode().equals(gameMode)) {
            if(retryLimit == 0) { arena = null; break; };
            arena = arenas.get(rand.nextInt(0,arenas.size()-1));
            retryLimit--;
        }

        if(arena == null) return null;

        List<BedwarsArenaManager> managers = arena.getManagers().values().stream().toList();
        retryLimit = (Main.getInstance().getMaxServerPerMap() * managers.size()) * 2;
        BedwarsArenaManager manager = managers.get(rand.nextInt(0,managers.size()-1));

        while(!manager.isMatchAvailable(player)) {
            if(retryLimit == 0) { manager = null; break; };
            manager = managers.get(rand.nextInt(0,managers.size()-1)); // majd managerbe kell playernek a gameben lévő kill, final, bed destroys. egy új stat külön?  mehet Oké
            retryLimit--;
        }

        return manager;
    }
    // idbi, honnan tudom, hogy a teamnek van-e ágya??? Illetve de, managerbe bedspawns location, jo akkor majd kell egy külön function. xd




}

