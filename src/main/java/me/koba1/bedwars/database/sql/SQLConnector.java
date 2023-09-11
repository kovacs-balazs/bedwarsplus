package me.koba1.bedwars.database.sql;

import java.sql.Connection;
import java.sql.*;

import lombok.Getter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.database.DatabaseConnector;
import me.koba1.bedwars.database.sql.exception.DatabaseConnectionException;

public class SQLConnector implements DatabaseConnector {

    @Getter private Connection connection;

    @Override
    public void initConnection() {
        try {

            /*Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + "mysqlgame.clans.hu" + "/" + "bedwars49", "bedwars49", "NUBugUGudyRApeG");
            if (connection.isValid(0)) {
            } else {
                throw new DatabaseConnectionException("Unknown Error (Invalid database file!)");
            }*/
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + getMain().getDataFolder() + "/database.db");
            if (!connection.isValid(0)) {

                throw new DatabaseConnectionException("Unknown Error (Invalid database file!)");
            }
        } catch (SQLException | ClassNotFoundException | DatabaseConnectionException e) {
            Main.sendError(e.toString() + "Con err");
        }
    }
}
