package me.koba1.bedwars.database;

public enum StorageMethods {

    SQLITE,
    MONGODB,
    FLATFILE;

    public static StorageMethods getMethodByName(String name) {
        for (StorageMethods value : values()) {
            if(value.name().equalsIgnoreCase(name))
                return value;
        }

        return SQLITE;
    }
}
