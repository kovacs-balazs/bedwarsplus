package me.koba1.bedwars.database.sql.exception;

public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException(){}
    public DatabaseConnectionException(String msg){
        super(msg);
    }
}
