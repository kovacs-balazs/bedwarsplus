package me.koba1.bedwars.database.mongodb;

import lombok.Getter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.database.DatabaseConnector;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
public class MongoDBConnector implements DatabaseConnector {

    private MongoClient client = null;
    @Getter private MongoDatabase database = null;

    @Override
    public void initConnection(){
        Logger logger = Logger.getLogger("org.mongodb");
        logger.setLevel(Level.SEVERE);
        try {
            ConnectionString connectionString = new ConnectionString("mongodb+srv://idbideveloper:Csutora20014@countingbot.q2is5ou.mongodb.net/?retryWrites=true&w=majority");
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .applyToSocketSettings(builder -> builder.connectTimeout(5000, TimeUnit.MILLISECONDS))
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();
            client = MongoClients.create(settings);

            database = client.getDatabase("Bedwars");
            MongoIterable<String> collection = database.listCollectionNames();
            for (MongoCollections s : MongoCollections.values()) {
                if (!isCollectionExists(s.getName(), collection)) {
                    database.createCollection(s.getName());
                }
            }
        } catch (Exception e) {
            Main.sendError(e.toString() + "Connector");
        }
    }
    private boolean isCollectionExists(String name, MongoIterable<String> iterable){
        for (String s : iterable)
            if(s.equalsIgnoreCase(name))
                return true;
        return false;
    }
}
