package me.koba1.bedwars.database.flatfile;

import lombok.Getter;
import me.koba1.bedwars.database.DatabaseConnector;
import me.koba1.bedwars.utils.datastorages.FileStorage;

import java.sql.Connection;

public class FlatFileConnector implements DatabaseConnector {
    @Getter
    private FileStorage storage;

    @Override
    public void initConnection() {

    }
}
