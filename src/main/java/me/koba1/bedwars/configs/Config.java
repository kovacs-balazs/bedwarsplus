package me.koba1.bedwars.configs;

import lombok.Getter;
import me.koba1.bedwars.database.StorageMethods;
import me.koba1.bedwars.utils.Playertools;
import me.koba1.bedwars.utils.objects.MessageLanguage;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public enum Config {

    // %bedwars_level%
    // %bedwars_level_formatted%
    LANGUAGE("language", "en", "This will be the default language on your server"),
    TIMEZONE("timezone", "Europe/Budapest", "All TimeZones available here:", "https://code2care.org/pages/java-timezone-list-utc-gmt-offset"),
    STORE_LINK("storeLink", "https://donation.link",
            "This is the place where you can put your donation link.",
            "When players try to join full arenas they'll receive a message saying that they can donate for VIP KICK",
            "permission in order to join full arenas and this link will be opened in browser if they click on the message."),
    LOBBY_SERVER("lobbyServer", "hub",
            "This is the server connected to your bungee network where players are teleported at the end of the game if",
            "serverType is set to BUNGEE. It is also used for the \"Back to lobby\" item in serverType MULTIARENA."),
    SERVER_IP("server-ip", "play.yourserver.com"),
    UPDATE_NOTIFICATION("update-notification", true, "If this enabled player's (who has permission) will get update notification"),
    STORAGE_METHOD("storage.storage-method", "SQLite", "Supported formarts: SQLite, MongoDB, Flatfile"),
    MONGODB_DATABASE("storage.mongodb-database", "BedwarsPlus", "Comments, 123aaa", "comment2"),
    MONGODB_URL("storage.mongodb-url", "URL", "Enter the MongoDB url, and select your database"),
    GLOBAL_CHAT("globalChat", false, "Set this option to true if you want the chat to be global.", "Players from arenas and from the lobby will see all messages. Spectator messages are excluded."),
    FORMAT_CHAT("chat.formatChat", true, "Set this option to true if you want BedWars1058 to manage the chat format."),
    CHAT_FORMAT_LOBBY("chat.lobby", "%bedwars_level_formatted% %luckperms_prefix% %player%&f: &f%message%", "You can define your chat format in lobby"),
    CHAT_FORMAT_ARENA_LOBBY("chat.arena-lobby", "%luckperms_prefix% %player%&f: &f%message%", "You can define your chat format in lobby"),
    CHAT_FORMAT_ARENA_ALL("chat.arena-all", "&6[ALL] %bedwars_level_formatted% %bedwars_team% %luckperms_prefix% %player%&f: &f%message%", "You can define your chat format in lobby"),
    BEDWARS_LEVEL_FORMATS("chat.bedwars-level-format", Map.of(
            "200", "&a[Lv. %bedwars_level%]",
            "100", "&e[Lv. %bedwars_level%]",
            "0", "&f[Lv. %bedwars_level%]"
    ), true,
            "A szám az azt jelenti, hogy attól a számtól a következőig milyen legyen"),
    SCOREBOARD_SIDEBAR_TITLE_TICK_REFRESH("scoreboard-settings.sidebar.title-refresh-interval", 2, "Scoreboard title refresh interval", "Time in ticks. Set to 0 to disable."),
    ENABLE_ABILTIY("enable-ability", true),
    ENABLE_GEN_SPLIT("enable-gen-split", true,  "Allow generator splitting between teammates");

    private final ConfigValues configValue;

    Config(ConfigValues value) {
        this.configValue = value;
    }

    Config(String section, Object value, String... comments) {
        configValue = new ConfigValues(section, value, comments);
    }

    Config(String section, Object value, boolean hashMap, String... comments) {
        configValue = new ConfigValues(section, value, hashMap, comments);
    }

    Config(String section, Object value) {
        configValue = new ConfigValues(section, value);
    }

    public void load(boolean save) {
        FileConfiguration config = ConfigFile.get();
        if (config.contains(configValue.getSection())) {
            configValue.setValue(config.get(configValue.getSection()));
        } else {
            config.set(configValue.getSection(), configValue.getValue());
            if (configValue.hasComment()) {
                config.setComments(configValue.getSection(), Arrays.asList(configValue.getComments()));
            }
        }

        if(save)
            ConfigFile.getConfig().save();
    }


    public String string() {
        return (String) this.configValue.getValue();
    }

    public StorageMethods storageMethod() {
        return StorageMethods.getMethodByName(string());
    }
    public MessageLanguage language() {
        return MessageLanguage.getLanguage(string());
    }

    public int integer() {
        return (int) this.configValue.getValue();
    }
    public HashMap<Integer, String> bedwarsLevelFormats() {
        Object obj = this.configValue.getValue();
        HashMap<Integer, String> formats = new HashMap<>();
        if(obj instanceof HashMap<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if((entry.getKey() instanceof Integer || entry.getKey() instanceof String) && entry.getValue() instanceof String value) {
                    formats.put(Integer.parseInt((String) entry.getKey()), value);
                }
            }
        }

        return formats;
    }

    public boolean bool() {
        return (boolean) this.configValue.getValue();
    }
}
