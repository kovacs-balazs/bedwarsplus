package me.koba1.bedwars.configs;

import lombok.Getter;
import me.koba1.bedwars.configs.messages.Messages;
import org.bukkit.configuration.MemorySection;

import java.util.HashMap;

public class ConfigValues {

    @Getter private String section;
    @Getter private Object value;
    @Getter private String[] comments;
    @Getter private boolean hashMap;

    public ConfigValues(String section, Object value, String... comments) {
        this.section = section;
        this.value = value;
        this.comments = comments.clone();
        this.hashMap = false;
    }

    public ConfigValues(String section, Object value, boolean hashMap, String... comments) {
        this.section = section;
        this.value = value;
        this.comments = comments.clone();
        this.hashMap = hashMap;
    }

    public HashMap<String, String> getHashMap() {
        HashMap<String, String> map = new HashMap<>();
        if(this.value instanceof MemorySection ms) {
            for (String key : ms.getKeys(false)) {
                map.put(key, ms.getString(key));
            }
        }

        return map;
    }

    public boolean hasComment() {
        return this.comments != null;
    }

    public void setValue(Object obj) {
        this.value = obj;
    }
}
