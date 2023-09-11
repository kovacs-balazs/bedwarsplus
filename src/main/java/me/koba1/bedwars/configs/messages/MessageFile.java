package me.koba1.bedwars.configs.messages;

import lombok.Getter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.utils.Formatter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MessageFile {

    private static Main m = Main.getPlugin(Main.class);
    @Getter private File cfg;
    @Getter private FileConfiguration file;
    @Getter private MessageFile config;
    @Getter private final File ymlFile;

    public MessageFile(String ymlFile) {
        config = this;
        this.ymlFile = new File(m.getDataFolder(), ymlFile);
        setup();
    }

    public void setup() {
        cfg = ymlFile;

        if (!cfg.exists()) {
            try {
                ymlFile.getParentFile().mkdirs();
                ymlFile.createNewFile();

            } catch (IOException e) {
            }
        }
        file = YamlConfiguration.loadConfiguration(cfg);
    }

    public FileConfiguration getFile() {
        return file;
    }

    public void save() {
        try {
            file.save(cfg);
        } catch (IOException e) {
            System.out.println("Can't save language file");
        }
    }

    public void reload() {
        file = YamlConfiguration.loadConfiguration(cfg);
    }

    public FileConfiguration get() {
        return this.getFile();
    }

    public String getString(String path) {
        if(get().getString(path) == null)
            return null;
        return Formatter.applyColor(get().getString(path));
    }

    public List<String> getStringList(String path) {
        return get().getStringList(path).stream().map(Formatter::applyColor).toList();
    }

    /*public static MessageFile getConfig() {
        return config;
    }*/
}
