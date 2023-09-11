package me.koba1.bedwars.configs.configmanagers;

import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.Config;
import me.koba1.bedwars.configs.messages.MessageFile;
import me.koba1.bedwars.utils.objects.MessageLanguage;

import java.io.File;
import java.util.List;

public class MessagesTool {

    private static final Main m = Main.getPlugin(Main.class);

    public static void loadNewMessages() {
        File[] files = new File(m.getDataFolder(), "messages").listFiles();
        if(files == null) return;


        List<MessageLanguage> langs = Main.getLanguages().values().stream().toList();
        List<String> langStrings = langs.stream().map(MessageLanguage::getLang).toList();
        for (File file : files) {
            if (file.getName().startsWith("messages_") && file.getName().endsWith(".yml")) {
                String languageString = file.getName().replace("messages_", "").replace(".yml", "");

                if (!langStrings.contains(languageString)) {
                    MessageLanguage lang = new MessageLanguage(languageString, new MessageFile("messages/messages_" + languageString + ".yml"));
                    if(lang.getLang().equalsIgnoreCase(Config.LANGUAGE.string())) {
                        lang.setDefault();
                    }
                }
            }
        }
    }
/*
    public static void updateGuiMessageFiles() {
        File[] files = new File(m.getDataFolder(), "gui_messages").listFiles();
        if(files == null) return;
        for (File file : files) {
            new MessageLanguage(language.replace(".yml", ""), config);
            for (GUIMessages value : GUIMessages.values()) {
                value.loadOthers(config);
            }
        }
    }*/
}
