package me.koba1.bedwars.configs.messages;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.utils.Formatter;
import me.koba1.bedwars.utils.Playertools;
import me.koba1.bedwars.utils.objects.BedwarsPlayer;
import me.koba1.bedwars.utils.objects.MessageLanguage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public enum Messages {

    PREFIX("prefix", "&8[&aBedWars+&8]", c("Base prefix etc.")),
    NO_PERMISSION("Error.no_permission", "%prefix% &cCan't find this player.", c("&cYou don't have permission to do that!")),
    CANT_KICK_YOURSELF("Error.cant_kick_yourself", "%prefix% &cYou can't kick yourself!"),
    CONFIG_RELOADED("Admin.configuration_reloaded", "%prefix% &aConfigurations has been reloaded"),
    NOT_FOUND_PLAYER("Error.not_found_player", "%prefix% &cThis player cannot be founded!.", c("Can't find this player because the player never seen it before")),
    PARTY_INVITE_EXPIRED("Party.party_invite_expired", "%prefix% &cThe party request has expired!"),
    PARTY_ALREADY_INVITED("Party.party_already_invited", "%prefix% &cThis player is already invited!"),
    PARTY_MEMBER_INVITED("Party.party_member_invited", "%prefix% &a%player% is invited to the party!"),
    PARTY_INVITED("Party.party_invited", "%prefix% &a%player% invited you to join the party!"),
    PARTY_MEMBER_LEAVE("Party.party_member_leave", "%prefix% &a%player% left the party!"),
    PARTY_LEAVE("Party.leave", "%prefix% &aYou left the party!"),
    NOT_IN_A_PARTY("Party.not_in_a_party", "%prefix% &cYou are not in a party!"),
    NOT_IN_THE_SAME_PARTY("Party.not_in_the_same_party", "%prefix% &cThe player is not in the same party!"),
    NOT_PARTY_LEADER("Party.not_party_leader", "%prefix% &cYou are not the party leader!"),
    PARTY_CHANGED_LEADER("Party.changed_leader", "%prefix% &cThe party leader left! The new leader is &o%player%"),
    PARTY_LEADER_TRANSFER("Party.leader_transfer", "%prefix% &aThe new leader is &o%player%!"),
    PARTY_NEW_LEADER("Party.new_leader", "%prefix% &aYou are now the party leader!"),
    PARTY_KICKED("Party.party_kicked", "%prefix% &cYou are kicked from the party!"),
    PARTY_MEMBER_KICKED("Party.party_member_kicked", "%prefix% &c&o%player%&r&c was kicked from the party!"),
    PARTY_DISBAND("Party.party_disband", "%prefix% &cThe party was disbanded!"),
    PARTY_JOIN("Party.party_join", "%prefix% &aYou joined into the party!"),
    PARTY_MEMBER_JOIN("Party.party_member_join", "%prefix% &o&a%player%&r&a joined into the party!"),
    PARTY_FULL("Party.party_full", "%prefix% &cThe party is full! :("),
    PARTY_NOT_INVITED("Party.party_not_invited", "%prefix% &cYou are not invited to the party!"),
    PARTY_ALREADY_IN("Party.party_already_in", "%prefix% &cYou are already in a party!"),

    ARENA_JOIN("Arena.arena_join", "%prefix% &7%player%&6 has joined! (&b%current_player%&6/&b%max_player%&6)"),
    ARENA_LEAVE("Arena.arena_leave", "%prefix% &7%player%&6 has left! (&b%current_player%&6/&b%max_player%&6)"),
    ARENA_LEAVE_IN_MATCH("Arena.arena_in_match", "%prefix% &7%player%&6 has left!"),
    ARENA_REJOIN("Arena.arena_rejoin", "%prefix% %team_color%%player% has rejoined!"),
    ARENA_START_PREPARE("Arena.arena_start_prepare", "%prefix% &6The match will start in &e%time% seconds!"),
    ARENA_START_CANCELLED("Arena.arena_start_cancelled", "%prefix% &6The match start has been cancelled!"),
    ARENA_START_DESCRIPTION("Arena.arena_start_description",Arrays.asList(
            "<<<<<<< BEDWARS >>>>>>>",
            "Legjobb game",
            "Amm nem"
    )),
    ARENA_PLAYER_DEATH("Arena.arena_player_death","%team_color%%player%&7 died!"),
    ARENA_PLAYER_DEATH_BY_PLAYER("Arena.arena_player_death_by_player","%team_color%%player%&7 was killed by %enemy_color%%killer%&7!"),

    ARENA_PLAYER_RESPAWN_TITLE("Arena.arena_player_respawn_title","&cYou died!"),
    ARENA_PLAYER_RESPAWN_SUBTITLE("Arena.arena_player_respawn_subtitle","&eYou will respawn in &c%time%&e seconds!"),
    ARENA_PLAYER_RESPAWN("Arena.arena_player_respawn","&aRespawned!"),


    // SHOP
    SHOP_INSUFF_MONEY("shop_insuff_money", "%prefix% &cYou don't have enough %currency%! Need %amount% more!"),
    SHOP_NEW_PURCHASE("shop_new_purchase", "%prefix% &aYou purchased &6%item%"),
    SHOP_ALREADY_BOUGHT("shop_already_bought", "%prefix% &cYou've already bought that!"),
    SHOP_UTILITY_SILVERFISH("shop_utility_silverfish", "%TeamColor%&l%TeamName% &r%TeamColor%Silverfish"),
    SHOP_UTILITY_IRON_GOLEM("shop_utility_iron_golem", "%TeamColor%%despawn%s &8[ %TeamColor%%health%&8]"),
    SHOP_LORE_STATUS_CAN_BUY("shop_lore_status_can_buy", "&eClick to purchase!"),
    SHOP_LORE_STATUS_CANT_AFFORD("shop_lore_status_cant_afford", "&cYou don't have enough %currency%!"),
    SHOP_LORE_STATUS_TIER_MAXED("shop_lore_status_tier_maxed", "&aMAXED!"),
    SHOP_LORE_STATUS_ARMOR("shop_lore_status_armor", "&aEQUIPPED!"),
    SHOP_LORE_QUICK_ADD("shop_lore_quick_add", "&bSneak Click to add to Quick Buy"),
    SHOP_LORE_QUICK_REMOVE("shop_lore_quick_remove", "&bSneak Click to remove from Quick Buy!"),
    MEANING_NO_TRAP("meaning_no_trap", "No trap!"),
    FORMAT_UPGRADE_TRAP_COST("format_upgrade_trap_cost", "&7Cost: %currencyColor%%cost% %currency%"),
    FORMAT_UPGRADE_COLOR_CAN_AFFORD("format_upgrade_color_can_afford", "&e"),
    FORMAT_UPGRADE_COLOR_CANT_AFFORD("format_upgrade_color_cant_afford", "&c"),
    FORMAT_UPGRADE_COLOR_UNLOCKED("format_upgrade_color_unlocked", "&a"),
    FORMAT_TIER_COLOR_LOCKED("format_tier_color_locked", "&7"),
    FORMAT_TIER_COLOR_UNLOCKED("format_tier_color_unlocked", "&a"),
    UPGRADES_LORE_CLICK_BUY("upgrades_lore_click_buy", "&eClick to purchase!"),
    UPGRADES_LORE_INSUFF_MONEY("upgrades_lore_insuff_money", "%color%You don't have enough %currency%"),
    UPGRADES_LORE_LOCKED("upgrades_lore_locked", "&cLOCKED"),
    UPGRADES_LORE_UNLOCKED("upgrades_lore_unlocked", "&aUNLOCKED"),
    UPGRADES_NEW_PURCHASE("upgrades_new_purchase", "&a%player% purchased &6%upgradeName%"),
    UPGRADES_UPGRADE_NAME_FORGE_TIER_1("upgrades_upgrade_name_forge_tier_1", "%color%Iron Forge"),
    UPGRADES_UPGRADE_NAME_FORGE_TIER_2("upgrades_upgrade_name_forge_tier_2", "%color%Golden Forge"),
    UPGRADES_UPGRADE_NAME_FORGE_TIER_3("upgrades_upgrade_name_forge_tier_3", "%color%Emerald Forge"),
    UPGRADES_UPGRADE_NAME_FORGE_TIER_4("upgrades_upgrade_name_forge_tier_4", "%color%Molten Forge"),
    UPGRADES_CATEGORY_ITEM_NAME_TRAPS("upgrades_category_item_name_traps", "&eBuy a trap"),
    UPGRADES_CATEGORY_ITEM_LORE_TRAPS("upgrades_category_item_lore_traps", Arrays.asList(
            "&7Purchased traps will be",
            "&7queued on the right.",
            "",
            "&eClick to browse!")),
    UPGRADES_UPGRADE_NAME_SWORDS_TIER_1("upgrades_upgrade_name_swords_tier_1", "%color%Sharpened Swords"),
    UPGRADES_UPGRADE_LORE_SWORDS("upgrades_upgrade_lore_swords", Arrays.asList(
            "&7Your team permanently gains",
            "&7Sharpness I on all swords and",
            "&7axes!",
            "",
            "%tier_1_color%Cost: &b%tier_1_cost% %tier_1_currency%")),
    UPGRADES_UPGRADE_NAME_ARMOR_TIER_1("upgrades_upgrade_name_armor_tier_1", "%color%Reinforced Armor I"),
    UPGRADES_UPGRADE_LORE_ARMOR("upgrades_upgrade_lore_armor", Arrays.asList(
            "&7Your team permanently gains",
            "&7Protection on all armor pieces!",
            "",
            "%tier_1_color%Tier 1: Protection I, &b%tier_1_cost% %tier_1_currency%",
            "%tier_2_color%Tier 2: Protection II, &b%tier_2_cost% %tier_2_currency%",
            "%tier_3_color%Tier 3: Protection III, &b%tier_3_cost% %tier_3_currency%",
            "%tier_4_color%Tier 4: Protection IV, &b%tier_4_cost% %tier_4_currency%")),
    UPGRADES_UPGRADE_NAME_ARMOR_TIER_2("upgrades_upgrade_name_armor_tier_2", "%color%Reinforced Armor II"),
    UPGRADES_UPGRADE_NAME_ARMOR_TIER_3("upgrades_upgrade_name_armor_tier_3", "%color%Reinforced Armor III"),
    UPGRADES_UPGRADE_NAME_ARMOR_TIER_4("upgrades_upgrade_name_armor_tier_4", "%color%Reinforced Armor IV"),
    UPGRADES_UPGRADE_NAME_MINER_TIER_1("upgrades_upgrade_name_miner_tier_1", "%color%Maniac Miner I"),
    UPGRADES_UPGRADE_LORE_MINER("upgrades_upgrade_lore_miner", Arrays.asList(
            "&7All players on your team",
            "&7permanently gain Haste.",
            "",
            "%tier_1_color%Tier 1: Haste I, &b%tier_1_cost% %tier_1_currency%",
            "%tier_2_color%Tier 2: Haste II, &b%tier_2_cost% %tier_2_currency%",
            "")),
    UPGRADES_UPGRADE_NAME_MINER_TIER_2("upgrades_upgrade_name_miner_tier_2", "%color%Maniac Miner II"),
    UPGRADES_UPGRADE_NAME_HEAL_POOL_TIER_1("upgrades_upgrade_name_heal_pool_tier_1", "%color%Heal Pool"),
    UPGRADES_UPGRADE_LORE_HEAL_POOL("upgrades_upgrade_lore_heal_pool", Arrays.asList(
            "&7Creates a Regeneration field",
            "&7around your base!",
            "",
            "%tier_1_color%Cost: &b%tier_1_cost% %tier_1_currency%",
            "")),
    UPGRADES_UPGRADE_NAME_DRAGON_TIER_1("upgrades_upgrade_name_dragon_tier_1", "%color%Dragon Buff"),
    UPGRADES_UPGRADE_LORE_DRAGON("upgrades_upgrade_lore_dragon", Arrays.asList(
            "&7Your team will have 2 dragons",
            "&7instead of 1 during deathmatch!",
            "",
            "%tier_1_color%Cost: &b%tier_1_cost% %tier_1_currency%",
            "")),
    UPGRADES_SEPARATOR_ITEM_NAME_GLASS("upgrades_separator_item_name_glass", "&8⬆&7Purchasable"),
    UPGRADES_SEPARATOR_ITEM_LORE_GLASS("upgrades_separator_item_lore_glass", Arrays.asList(
            "&8⬇&7Traps Queue")),
    UPGRADES_TRAP_SLOT_ITEM_NAME_FIRST("upgrades_trap_slot_item_name_first", "%color%Trap #1: %name%"),
    UPGRADES_TRAP_SLOT_ITEM_LORE1_FIRST("upgrades_trap_slot_item_lore1_first", Arrays.asList(
            "&7The first enemy to walk",
            "&7into your base will trigger",
            "&7this trap!")),
    UPGRADES_TRAP_SLOT_ITEM_LORE2_FIRST("upgrades_trap_slot_item_lore2_first", Arrays.asList(
            "",
            "&7Purchasing a trap will",
            "&7queue it here. Its cost",
            "&7will scale based on the",
            "&7number of traps queued.",
            "",
            "&7Next trap: &b%cost% %currency%")),
    UPGRADES_TRAP_SLOT_ITEM_NAME_SECOND("upgrades_trap_slot_item_name_second", "%color%Trap #2: %name%"),
    UPGRADES_TRAP_SLOT_ITEM_LORE1_SECOND("upgrades_trap_slot_item_lore1_second", Arrays.asList(
            "&7The second enemy to walk",
            "&7into your base will trigger",
                                                 "&7this trap!")),
    UPGRADES_TRAP_SLOT_ITEM_LORE2_SECOND("upgrades_trap_slot_item_lore2_second", Arrays.asList(
            "",
            "&7Purchasing a trap will",
            "&7queue it here. Its cost",
            "&7will scale based on the",
            "&7number of traps queued.",
            "",
            "&7Next trap: &b%cost% %currency%")),
    UPGRADES_TRAP_SLOT_ITEM_NAME_THIRD("upgrades_trap_slot_item_name_third", "%color%Trap #3: %name%"),
    UPGRADES_TRAP_SLOT_ITEM_LORE1_THIRD("upgrades_trap_slot_item_lore1_third", Arrays.asList(
            "&7The third enemy to walk",
            "&7into your base will trigger",
            "&7this trap!")),
    UPGRADES_TRAP_SLOT_ITEM_LORE2_THIRD("upgrades_trap_slot_item_lore2_third", Arrays.asList(
            "",
            "&7Purchasing a trap will",
            "&7queue it here. Its cost",
            "&7will scale based on the",
            "&7number of traps queued.",
            "",
            "&7Next trap: &b%cost% %currency%")),
    UPGRADES_BASE_TRAP_NAME_1("upgrades_base_trap_name_1", "%color%It's a trap!"),
    UPGRADES_BASE_TRAP_LORE_1("upgrades_base_trap_lore_1", Arrays.asList(
            "&7Inflicts Blindness and Slowness",
            "&7for 5 seconds.",
            "")),
    UPGRADES_BASE_TRAP_NAME_2("upgrades_base_trap_name_2", "%color%Counter_Offensive Trap"),
    UPGRADES_BASE_TRAP_LORE_2("upgrades_base_trap_lore_2", Arrays.asList(
            "&7Grants Speed I for 15 seconds to",
            "&7allied players near your base.",
            "")),
    UPGRADES_BASE_TRAP_NAME_3("upgrades_base_trap_name_3", "%color%Alarm Trap"),
    UPGRADES_BASE_TRAP_LORE_3("upgrades_base_trap_lore_3", Arrays.asList(
            "&7Reveals invisible players as",
            "&7well as their name and team.",
            "")),
    UPGRADES_BASE_TRAP_NAME_4("upgrades_base_trap_name_4", "%color%Miner Fatigue Trap"),
    UPGRADES_BASE_TRAP_LORE_4("upgrades_base_trap_lore_4", Arrays.asList(
            "&7Inflicts Mining Fatigue for 10",
            "&7seconds.",
            "")),
    UPGRADES_SEPARATOR_ITEM_NAME_BACK("upgrades_separator_item_name_back", "&aBack"),
    UPGRADES_SEPARATOR_ITEM_LORE_BACK("upgrades_separator_item_lore_back", Arrays.asList(
            "&7To Upgrades & Traps")),
    UPGRADES_CATEGORY_GUI_NAME_TRAPS("upgrades_category_gui_name_traps", "&8Queue a trap"),
    UPGRADES_TRAP_QUEUE_FULL("upgrades_trap_queue_full", "&cTrap queue full!"),
    UPGRADES_TRAP_DEFAULT_MSG("upgrades_trap_default_msg", "&c&l%trap% was set off!"),
    UPGRADES_TRAP_DEFAULT_TITLE("upgrades_trap_default_title", "&cTRAP TRIGGERED!"),
    UPGRADES_TRAP_DEFAULT_SUBTITLE("upgrades_trap_default_subtitle", "&fYour %trap% has been triggered!"),
    UPGRADES_BASE_TRAP_MSG_3("upgrades_base_trap_msg_3", "&c&lAlarm trap set off by &7&l%player% &c&lfrom %color%&l%team% &c&lteam!"),
    UPGRADES_BASE_TRAP_TITLE_3("upgrades_base_trap_title_3", "&c&lALARM!!!"),
    UPGRADES_BASE_TRAP_SUBTITLE_3("upgrades_base_trap_subtitle_3", "&fAlarm trap set off by %color%%team% &fteam!"),
    INVENTORY_NAME("shop_items_messages.inventory_name", "&8Quick Buy"),
    QUICK_BUY_ADD_INVENTORY_NAME("shop_items_messages.quick_buy_add_inventory_name", "&8Adding to Quick Buy..."),
    SEPARATOR_ITEM_NAME("shop_items_messages.separator_item_name", "&8⇧ Categories"),
    SEPARATOR_ITEM_LORE("shop_items_messages.separator_item_lore", Arrays.asList(
            "&8⇩ Items")),
    QUICK_BUY_ITEM_NAME("shop_items_messages.quick_buy_item_name", "&bQuick Buy"),
    QUICK_BUY_ITEM_LORE("shop_items_messages.quick_buy_item_lore", Arrays.asList(
            // Leave this list empty as per your YAML
    )),
    QUICK_BUY_EMPTY_ITEM_NAME("shop_items_messages.quick_buy_empty_item_name", "&cEmpty slot!"),
    QUICK_BUY_EMPTY_ITEM_LORE("shop_items_messages.quick_buy_empty_item_lore", Arrays.asList(
            "&7This is a Quick Buy Slot!",
            "&bSneak Click &7any item in",
            "&7the shop to add it here."
    )),
    CAN_BUY_COLOR("shop_items_messages.can_buy_color", "&a"),
    CANT_BUY_COLOR("shop_items_messages.cant_buy_color", "&c"),

    // SCOREBOARD
    FORMAT_SB_DATE("format-sb-date", "dd/MM/yy"),
    FORMAT_SB_TEAM_GENERIC("format-sb-team-generic", "%team_color%%team_letter%&f %team_name%: %team_status%"),
    FORMAT_SB_TEAM_ALIVE("format-sb-team-alive", "&a&l✓"),
    FORMAT_SB_TEAM_ELIMINATED("format-sb-team-alive", "&c&l✘"),
    FORMAT_SB_YOU("format-sb-you", "&7 YOU"),
    FORMAT_SB_BED_DESTROYED("format-sb-bed-destroyed", "&a%remaining_players%"),
    DEFAULT_WAITING("scoreboard.default.waiting", Arrays.asList(
            "&f&lBED WARS",
            "&7%date% &8%server%",
            "",
            "&fMap: &a%map%",
            "",
            "&fPlayers: &a%on%/%max%",
            "",
            "&fWaiting...",
            "",
            "§fMode: &a%group%",
            "&fVersion: &7%version%",
            "",
            "&e%serverIp%"
    )),
    DEFAULT_STARTING("scoreboard.default.starting", Arrays.asList(
            "&f&lBED WARS",
            "&7%date% &8%server%",
            "",
            "&fMap: &a%map%",
            "",
            "&fPlayers: &a%on%/%max%",
            "",
            "&fStarting in &a%time%s",
            "",
            "§fMode: &a%group%",
            "&fVersion: &7%version%",
            "",
            "&e%serverIp%"
    )),
    DEFAULT_PLAYING("scoreboard.default.playing", Arrays.asList(
            "&e&lBED WARS",
            "&7%date%",
            "",
            "&f%nextEvent% in &a%time%",
            "",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "",
            "&e%serverIp%"
    )),
    DOUBLES_PLAYING("scoreboard.doubles.playing", Arrays.asList(
            "&e&lBED WARS",
            "&7%date%",
            "",
            "&f%nextEvent% in &a%time%",
            "",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "",
            "&e%serverIp%"
    )),
    TRIPLES_PLAYING("scoreboard.3v3v3v3.playing", Arrays.asList(
            "&e&lBED WARS",
            "&7%date%",
            "",
            "&f%nextEvent% in &a%time%",
            "",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "",
            "&fKills: &a%kills%",
            "&fFinal Kills: &a%finalKills%",
            "&fBeds Broken: &a%beds%",
            "",
            "&e%serverIp%"
    )),
    QUADS_PLAYING("scoreboard.4v4v4v4.playing", Arrays.asList(
            "&e&lBED WARS",
            "&7%date%",
            "",
            "&f%nextEvent% in &a%time%",
            "",
            "%team%",
            "%team%",
            "%team%",
            "%team%",
            "",
            "&fKills: &a%kills%",
            "&fFinal Kills: &a%finalKills%",
            "&fBeds Broken: &a%beds%",
            "",
            "&e%serverIp%"
    )),
    LOBBY("scoreboard.lobby", Arrays.asList(
            "&6&lBedWars,&4&lB&6edWars,&6&lB&4e&6dWars,&6&lBe&4d&6Wars,&6&lBed&4W&6ars,&6&lBedW&4a&6rs,&6&lBedWa&4r&6s,&6&lBedWar&4s,&6&lBedWars",
            "&fYour Level: %level%",
            "",
            "&fProgress: &a%currentXp%&7/&b%requiredXp%",
            "%progress%",
            "",
            "&7%player%",
            "",
            "&fCoins: &a%money%",
            "",
            "&fTotal Wins: &a%wins%",
            "&fTotal Kills: &a%kills%",
            "",
            "&e%serverIp%"
    ));







    ;

    private MessageValue value;

    Messages(String section, String message, String... comments) {
        value = new MessageValue(section, message, comments);
    }

    Messages(String section, String message) {
        value = new MessageValue(section, message);
    }

    Messages(String section, List<String> listMessage, String... comments) {
        value = new MessageValue(section, listMessage, comments);
    }

    Messages(String section, List<String> listMessage) {
        value = new MessageValue(section, listMessage);
    }

    public MessageValue.PlayerMessage message() {
        MessageFile file = Main.getDefaultLanguage().getFile();
        String prefix = file.getString("prefix");
        if (value.isString()) {
            String msg = file.getString(value.getSection());
            msg = msg.replace("%prefix%", prefix);
            return new MessageValue.PlayerMessage(msg);
        } else {
            List<String> msgs = file.getStringList(value.getSection());
            msgs = Formatter.format(msgs).replace("%prefix%", prefix).applyColor().list();
            return new MessageValue.PlayerMessage(msgs);
        }
    }

    public MessageValue.PlayerMessage language(Player player) {
        BedwarsPlayer bedwars = BedwarsPlayer.getPlayer(player);
        if (bedwars == null) {
            return language(MessageLanguage.getDefault());
        }
        return language(bedwars.getLanguage());
    }

    public MessageValue.PlayerMessage language(BedwarsPlayer bedwars) {
        if (bedwars == null) {
            return language(MessageLanguage.getDefault());
        }
        return language(bedwars, bedwars.getLanguage());
    }

    public MessageValue.PlayerMessage language(CommandSender sender) {
        MessageFile file = Main.getDefaultLanguage().getFile();
        String prefix = file.getString("prefix");
        if (sender instanceof Player p) {
            BedwarsPlayer bedwars = BedwarsPlayer.getPlayer(p);
            if (bedwars == null) {
                return language(MessageLanguage.getDefault());
            }
            return language(bedwars, bedwars.getLanguage());
        } else {
            if (value.isString()) {
                String msg = file.getString(value.getSection());
                msg = msg.replace("%prefix%", prefix);
                return new MessageValue.PlayerMessage(msg);
            } else {
                List<String> msgs = file.getStringList(value.getSection());
                msgs = Formatter.format(msgs).replace("%prefix%", prefix).applyColor().list();
                return new MessageValue.PlayerMessage(msgs);
            }
        }
    }

    public MessageValue.PlayerMessage language(MessageLanguage language) {
        MessageFile file = language.getFile();
        String prefix = file.getString("prefix");
        if (value.isString()) {
            String msg = file.getString(value.getSection());
            msg = msg.replace("%prefix%", prefix);
            return new MessageValue.PlayerMessage(msg);
        } else {
            List<String> msgs = file.getStringList(value.getSection());
            msgs = Formatter.format(msgs).replace("%prefix%", prefix).applyColor().list();
            return new MessageValue.PlayerMessage(msgs);
        }
    }

    public MessageValue.PlayerMessage language(BedwarsPlayer player, MessageLanguage language) {
        MessageFile file = language.getFile();
        String prefix = file.getString("prefix");
        if (value.isString()) {
            String msg = file.getString(value.getSection());
            msg = msg.replace("%prefix%", prefix);
            return new MessageValue.PlayerMessage(player, msg);
        } else {
            List<String> msgs = file.getStringList(value.getSection());
            msgs = Formatter.format(msgs).replace("%prefix%", prefix).applyColor().list();
            return new MessageValue.PlayerMessage(player, msgs);
        }
    }

    public void load(boolean save) {
        List<MessageFile> configs = Main.getLanguages().values().stream().map(MessageLanguage::getFile).toList();
        for (MessageFile message : configs) {
            FileConfiguration config = message.getFile();
            if (!config.contains(value.getSection())) {
                config.set(value.getSection(), value.getMessageObject());
                if (value.hasComment()) {
                    config.setComments(value.getSection(), Arrays.asList(value.getComments()));
                }
            }

            message.save();
        }

        MessageFile messageFile = Main.getDefaultLanguage().getFile();
        FileConfiguration config = messageFile.getFile();
        if (!config.contains(value.getSection())) {
            config.set(value.getSection(), value.getMessageObject());
            if (value.hasComment()) {
                config.setComments(value.getSection(), Arrays.asList(value.getComments()));
            }
        }

        messageFile.reload();
        if (messageFile.equals(Main.getDefaultLanguage().getFile()) && config.contains(value.getSection())) {
            if (value.isString()) {
                value.setMessage(config.getString(value.getSection()));
            } else
                value.setListMessage(config.getStringList(value.getSection()));
        }

        if(save)
            messageFile.save();
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
    }
}
