package me.koba1.bedwars.configs.guimessages;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.messages.MessageFile;
import me.koba1.bedwars.configs.messages.MessageValue;
import me.koba1.bedwars.configs.messages.Messages;
import me.koba1.bedwars.utils.Formatter;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.MessageLanguage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum GUIMessages {

    back_button("&cBack", new ArrayList<>()),
    leave_button("&cLeave", Arrays.asList(" ", "&7Click here to close the menu!")),
    save_button("&aSave All Changes", Arrays.asList(" ", "&7Click here to save the changes!")),
    discard_button("&cDiscard All Changes", Arrays.asList(" ", "&7Click here to discard the changes!")),
    member_head("&e%player%", new ArrayList<>()),
    faction_rename("&eRename Faction", Arrays.asList("&5", "&7Click here to rename your faction!")),
    faction_histories("&eFaction Histories", Arrays.asList("&5", "&7Click here to see the histories!")),
    faction_member_manager("&eManage Players", Arrays.asList("&5", "&7Click here to manage members!")),
    faction_rank_manager("&eManage Ranks", Arrays.asList("&5", "&7Click here to manage ranks!")),
    faction_invite_manager("&eInvite Manager", Arrays.asList("&5", "&7Click here to manage the invites!")),
    faction_invite_player("&eInvite Player", Arrays.asList("&5", "&7Click here to invite a player!")),
    faction_invited_players("&eInvited Players", Arrays.asList("&5", "&7Click here to show the invited players!")),
    faction_uninvite_player("&e%player%", Arrays.asList("&5", "&7Click here to &euninvite %player%&7!")),
    faction_player_rank_manager("&eManage Player's Rank", Arrays.asList("&5", "&7Click here to manage rank!")),
    faction_kick_player("&eKick Player", Arrays.asList("&5", "&7Click here to kick player!")),
    manage_rank("&e%rank%", Arrays.asList("&5", "&7Click here to manage rank!")),
    create_rank("&aCreate a New Rank", Arrays.asList("&5", "&7Click here to create a rank!")),
    rename_rank("&eRename Rank", Arrays.asList("&5", "&7Click here to rename rank!")),
    rank_permission_manager("&ePermission Manager", Arrays.asList("&5", "&7Click here to manage permissions!")),
    delete_rank("&cDelete Rank", Arrays.asList("&5", "&7Click here to delete rank!")),
    faction_ranks("&a%rank%", new ArrayList<>()),
    priority_toggle_button("&ePriority Manage", Arrays.asList("&5", "&7Click here to change the order!")),
    rank_manager_toggle_button("&eRank Manager", Arrays.asList("&5", "&7Click here to to go back!")),
    rank_priority_selected("", Arrays.asList("&5", "&aSELECTED!", "&7Click on another rank to swap their order!")),

    faction_toplist_page("&7Current Page: &c&o%page%", Arrays.asList()),
    faction_toplist_next("&cNext Page", Arrays.asList()),
    faction_toplist_previous("&cPrevious Page", Arrays.asList()),

    rollback_information("&e%player% &6#%id%", Arrays.asList(
            "&5",
            "&eDate &8» &6%date%",
            "&eDamage Cause &8» &6%damage_cause%",
            "&eEXP Level &8» &6%exp_level%",
            "&eType &8» &6%type%",
            "&eRolled Back &8» &6%rolled%",
            "&5",
            "&aClick here to view!",
            "&aDROP item to rollback!"
    )),

    faction_stats("&2&o%faction_name%", Arrays.asList(
            "&7┌──",
            "&7│ &aDTR &7/ &aMax DTR: &f%faction_dtr% &7/ &f%faction_dtr_max%",
            "&7│ &aDTR Regen: &f%faction_dtr_regen%",
            "&7│ &aBalance: &f$%faction_balance%",
            "&7│ &aPoints: &f%faction_points%",
            "&7│ &aMember Count: &f%faction_member_online%&7/&f%faction_member_count%",
            "&7│ &aHome Location: &f%faction_home%",
            "&7├──",
            "&7│ &aKills: &f%faction_kills%",
            "&7│ &aDeaths: &f%faction_deaths%",
            "&7└──"
    ));

    public String name, defaultName, tempName;
    public List<String> lore, defaultLore, tempLore;
    //public MessageSections section;

    GUIMessages(String name, List<String> lore) {
        this.name = name;
        this.defaultName = name;
        this.tempName = name;
        this.lore = lore;
        this.defaultLore = lore;
        this.tempLore = lore;
    }

/*    GUIMessages(MessageSections section, String name, List<String> lore) {
        this.section = section;
        this.name = name;
        this.defaultName = name;
        this.tempName = name;
        this.lore = lore;
        this.defaultLore = lore;
        this.tempLore = lore;
    }*/

/*    public GUIMessages language(Player p) {
        MessageLanguage language = BedwarsPlayer.getPlayer(p).getLanguage();
        String configMessage = language.getFile().getString(this + ".name");
        if (configMessage != null) {
            this.tempName = Formatter.applyColor(configMessage);
            //System.out.println(this.tempName);
            //return this;
        } else {}
        // MessagesTool.updateGuiMessageFiles();

        List<String> returnList = new ArrayList<>();
        List<String> configList = language.getFile().getStringList(this + ".lore");

        if (!configList.isEmpty()) {
            for (String message : configList) {
                returnList.add(
                        Formatter.applyColor(message)
                );
            }
        } else {
            //MessagesTool.updateGuiMessageFiles();
            for (String message : this.lore) {
                returnList.add(
                        Formatter.applyColor(message)
                );
            }
        }
        this.tempLore = returnList;

//,        this.tempL = Formatter.applyColor(this.message);
        return this;
    }

    public String getName() {
        if (this.tempName == null) {
            return Formatter.applyColor(name);
        }

        final String tempMessage = this.tempName;
        this.tempName = this.name;
        this.tempLore = this.lore;
        return Formatter.applyColor(tempMessage);
    }

   *//* public GUIMessages setDamageCause(String damageCause) {
        this.tempName = this.tempName.replace("%damage_cause%", damageCause);
        this.tempLore = replaceLore("%damage_cause%", damageCause);
        return this;
    }*//*

    public List<String> getLore() {
        final List<String> tempList = new ArrayList<>();
        for (String s : this.tempLore.isEmpty() ? this.lore : this.tempLore) {
            tempList.add(Formatter.applyColor(s));
        }
        this.tempName = this.name;
        this.tempLore = this.lore;
        return tempList;
    }

    public List<String> replaceLore(String... strings) {
        List<String> outputList = new ArrayList<>();
        for (String lore : this.tempLore) {
            outputList.add(replace(lore, strings));
        }

        return outputList;
    }

    public String replace(String s, String... strings) {
        String key = "";
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].startsWith("%") && strings[i].endsWith("%")) {
                key = strings[i];
                continue;
            }
            s = s.replace(key, strings[i]);
            key = "";
        }

        return s;
    }

    public MessageValue.PlayerMessage language(Player player) {
        BedwarsPlayer bedwars = BedwarsPlayer.getPlayer(player);
        if(bedwars == null) {
            return language(MessageLanguage.getDefault());
        }
        return language(bedwars.getLanguage());
    }

    public MessageValue.PlayerMessage language(MessageLanguage language) {
        MessageFile file = language.getFile();
        String prefix = file.getString("PREFIX");
        if(value.isString()) {
            String msg = file.getString(value.getSection());
            msg = msg.replace("%prefix%", prefix);
            return new MessageValue.PlayerMessage(msg);
        } else {
            List<String> msgs = file.getStringList(value.getSection());
            msgs = Formatter.format(msgs).replace("%prefix%", prefix).applyColor().list();
            return new MessageValue.PlayerMessage(msgs);
        }
    }

    public void load(boolean save) {
        List<MessageFile> configs = Main.getLanguages().values().stream().map(MessageLanguage::getFile).toList();
        for (MessageFile messageFile : configs) {
            FileConfiguration config = messageFile.getFile();
            if (!config.contains(value.getSection())) {
                config.set(value.getSection(), value.getMessageObject());
                if (value.hasComment()) {
                    config.setComments(value.getSection(), Arrays.asList(value.getComments()));
                }
            }

            if (messageFile.equals(Main.getDefaultLanguage()) && config.contains(value.getSection())) {
                if (value.isString())
                    value.setMessage(config.getString(value.getSection()));
                else
                    value.setListMessage(config.getStringList(value.getSection()));
            }
        }

        if(save)
            configs.forEach(MessageFile::save);
    }

    private static String[] c(String... args) {
        return args.clone();
    }

    public static void loadAll() {
        for (Messages value : values()) {
            value.load(false);
        }

        List<MessageFile> configs = Main.getLanguages().values().stream().map(MessageLanguage::getFile).toList();
        configs.forEach(MessageFile::save);
    }*/
}
