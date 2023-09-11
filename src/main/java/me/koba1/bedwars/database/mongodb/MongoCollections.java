package me.koba1.bedwars.database.mongodb;

public enum MongoCollections {
    USERS("Users");
    private String name;

    MongoCollections(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
