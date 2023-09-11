package me.koba1.bedwars.database;

import me.koba1.bedwars.Main;

public interface DatabaseConnector {
    default Main getMain() {
        return Main.getPlugin(Main.class);
    }
    public void initConnection();

}
