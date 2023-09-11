package me.koba1.bedwars.configs.files;

import lombok.Getter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.utils.Formatter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GeneratorConfigFile {

    private static Main m = Main.getPlugin(Main.class);
    private static File cfg;
    @Getter private static FileConfiguration file;

    @Getter static private GeneratorConfigFile config;
    @Getter private final File ymlFile;

    public GeneratorConfigFile(String ymlFile) {
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
                InputStream in = m.getResource(ymlFile.getName());
                FileOutputStream out = new FileOutputStream(cfg);

                if(in == null) return;
                try {
                    int n;
                    while ((n = in.read()) != -1) {
                        out.write(n);
                    }
                }
                finally {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                }

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

    public static FileConfiguration get() {
        return getConfig().getFile();
    }

    public static String getString(String path) {
        return Formatter.applyColor(get().getString(path));
    }

    public static List<String> getStringList(String path) {
        return get().getStringList(path).stream().map(Formatter::applyColor).toList();
    }
}
