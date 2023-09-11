package me.koba1.bedwars.utils.objects;

import lombok.Getter;
import lombok.Setter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.configs.messages.MessageFile;
import me.koba1.bedwars.configs.messages.Messages;

public class MessageLanguage {

    @Getter private String lang;
    @Getter private MessageFile file;
    @Getter @Setter private boolean defaultLanguage;

    public MessageLanguage(String lang, MessageFile file) {
        this.lang = lang;
        this.file = file;
        this.defaultLanguage = false;
        Main.getLanguages().put(lang, this);
    }

    public void setDefault() {
        for (MessageLanguage value : Main.getLanguages().values()) {
            value.setDefaultLanguage(false);
        }
        this.defaultLanguage = true;
        Main.setDefaultLanguage(this);
    }

    public static MessageLanguage getDefault() {
        return Main.getDefaultLanguage();
    }

    public static MessageLanguage getLanguage(String key) {
        for (MessageLanguage s : Main.getLanguages().values()) {
            if(s.getLang().equalsIgnoreCase(key))
                return s;
        }
        return null;
    }

    public static MessageLanguage getLanguage(MessageFile file) {
        for (MessageLanguage s : Main.getLanguages().values()) {
            if(s.getFile().equals(file))
                return s;
        }
        return null;
    }
}
