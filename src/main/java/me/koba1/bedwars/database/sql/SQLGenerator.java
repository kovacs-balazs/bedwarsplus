package me.koba1.bedwars.database.sql;

import me.koba1.bedwars.Main;

import java.sql.Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;

public class SQLGenerator {
    public SQLGenerator(Connection connection) {
        String[] tables = {
                "CREATE TABLE IF NOT EXISTS Users (ID INTEGER PRIMARY KEY ASC AUTOINCREMENT,Name TEXT,UUID TEXT UNIQUE ON CONFLICT REPLACE,language TEXT); ",
        };
        for (String data : tables) {
            PreparedStatement st = null;
            try {
                st = connection.prepareStatement(data);
                st.executeUpdate();

            } catch (SQLException c) {
                Main.sendError(c.toString());
            }
        }
    }
}
