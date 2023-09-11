package me.koba1.bedwars.database.sql.apitypes;
import lombok.Getter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.utils.LambdaThrower;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class SQLApi {
    private static final Main m = Main.getPlugin(Main.class);

    @Getter private static SQLApi instance;
    @Getter private Connection connection;

    public SQLApi(Connection connection) {
        instance = this;
        this.connection = connection;
    }

    private ResultSet Poll_internal(String query,Object... args) {
        try {
            PreparedStatement statement = connection.prepareStatement(query,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String param)
                    statement.setString(i + 1, param);
                if (args[i] instanceof Integer param)
                    statement.setInt(i + 1, param);
                if (args[i] instanceof Boolean param)
                    statement.setBoolean(i + 1, param);
                if (args[i] instanceof Float param)
                    statement.setFloat(i + 1, param);
                if (args[i] instanceof Double param)
                    statement.setDouble(i + 1, param);
                if (args[i] instanceof Long param)
                    statement.setLong(i + 1, param);
                if (args[i] instanceof Time param)
                    statement.setTime(i + 1, param);
                if (args[i] instanceof Timestamp param)
                    statement.setTimestamp(i + 1, param);
                if (args[i] instanceof Array param)
                    statement.setArray(i + 1, param);
                if (args[i] instanceof Short param)
                    statement.setShort(i + 1, param);
                System.out.println(args[i]);
            }
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            return statement.executeQuery();
        }catch (SQLException e){
            Main.sendError(e.toString() + "POLL");
        }
        return null;
    }
    public CompletableFuture<ResultSet> Poll(String query, Object... args) {
        return CompletableFuture.supplyAsync(LambdaThrower.rethrowSupplier(() -> Poll_internal(query,args)));
    }
    private int Execute_internal(String query,Object... args) {
        try {
            PreparedStatement statement = connection.prepareStatement(query,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String param)
                    statement.setString(i + 1, param);
                if (args[i] instanceof Integer param)
                    statement.setInt(i + 1, param);
                if (args[i] instanceof Boolean param)
                    statement.setBoolean(i + 1, param);
                if (args[i] instanceof Float param)
                    statement.setFloat(i + 1, param);
                if (args[i] instanceof Double param)
                    statement.setDouble(i + 1, param);
                if (args[i] instanceof Long param)
                    statement.setLong(i + 1, param);
                if (args[i] instanceof Time param)
                    statement.setTime(i + 1, param);
                if (args[i] instanceof Timestamp param)
                    statement.setTimestamp(i + 1, param);
                if (args[i] instanceof Array param)
                    statement.setArray(i + 1, param);
                if (args[i] instanceof Short param)
                    statement.setShort(i + 1, param);
            }

            return statement.executeUpdate();

        }catch (SQLException e){
            Main.sendError(e.toString() + "EXECICA");
        }
        return  0;
    }
    public CompletableFuture<Integer> Execute(String query, Object... args) {
        return CompletableFuture.supplyAsync(LambdaThrower.rethrowSupplier(() -> Execute_internal(query,args)));
    }
}
