package me.koba1.bedwars.database.mongodb.api;
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Getter;
import me.koba1.bedwars.Main;
import me.koba1.bedwars.utils.LambdaThrower;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
public class MongoApi {
    @Getter
    private final MongoDatabase database;

    public MongoApi(MongoDatabase database){
        this.database = database;
    }


    private InsertOneResult Insert_internal(String collection, Document document){
        MongoCollection<Document> col = database.getCollection(collection);
        return col.insertOne(document);
    }

    private UpdateResult Update_internal(String collection, Bson where, Bson updates){
        MongoCollection<Document> col = database.getCollection(collection);
        UpdateOptions options = new UpdateOptions().upsert(false);
        return col.updateOne(where,updates,options);
    }
    private UpdateResult UpdateMany_internal(String collection, Bson where, Bson updates){
        MongoCollection<Document> col = database.getCollection(collection);
        return col.updateMany(where,updates);
    }
    private Document Find_internal(String collection,Bson where){
        MongoCollection<Document> col = database.getCollection(collection);
        Document doc = col.find(where).sort(Sorts.ascending("_id")).first();
        return doc == null ? new Document() : doc;
    }
    private ArrayList<Document> FindAll_internal(String collection, Bson where){
        MongoCollection<Document> col = database.getCollection(collection);
        ArrayList<Document> retvalue = new ArrayList<>();
        try {
            FindIterable<Document> res = col.find(where).sort(Sorts.ascending("_id"));

            res.cursor().forEachRemaining(retvalue::add);
        }catch (Exception e){
            Main.sendError(e.toString());
        }
        return retvalue;
    }
    private DeleteResult Delete_internal(String collection, Bson where){
        MongoCollection<Document> col = database.getCollection(collection);
        return col.deleteMany(where);
    }

    public CompletableFuture<InsertOneResult> Insert(String collection, Document document) {
        return CompletableFuture.supplyAsync(LambdaThrower.rethrowSupplier(() -> Insert_internal(collection,document)));
    }
    /**
     * @param collection the collection
     * @param where MYSQL Where Statement
     * @return found Documents in an ArrayList, or empty ArrayList
     * <p>
     * Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */
    public CompletableFuture<UpdateResult> Update(String collection, Bson where, Bson updates) {
        return CompletableFuture.supplyAsync(LambdaThrower.rethrowSupplier(() -> Update_internal(collection,where,updates)));
    }
    /**
     * @param collection the collection
     * @param where MYSQL Where Statement
     * @return found Documents in an ArrayList, or empty ArrayList
     * <p>
     * Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */
    public CompletableFuture<UpdateResult> UpdateMany(String collection,Bson where, Bson updates) {
        return CompletableFuture.supplyAsync(LambdaThrower.rethrowSupplier(() -> UpdateMany_internal(collection,where,updates)));
    }
    /**
     * @param collection the collection
     * @param where MYSQL Where Statement
     * @return found Documents in an ArrayList, or empty ArrayList
     * <p>
     * Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */
    public CompletableFuture<Document> Find(String collection, Bson where) {
        return CompletableFuture.supplyAsync(LambdaThrower.rethrowSupplier(() -> Find_internal(collection,where)));
    }
    /**
     * @param collection the collection
     * @param where MYSQL Where Statement
     * @return found Documents in an ArrayList, or empty ArrayList
     * <p>
     * Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */
    public CompletableFuture<ArrayList<Document>> FindAll(String collection, Bson where) {
        return CompletableFuture.supplyAsync(LambdaThrower.rethrowSupplier(() -> FindAll_internal(collection,where)));
    }
    /**
     * @param collection the collection
     * @param where MYSQL Where Statement
     * @return found Documents in an ArrayList, or empty ArrayList
     * <p>
     * Cheat sheet:
     *                Comparison
     * <p>
     *                  eq("_id", 1); == Equals<p>
     *                  gt("_id", 1); == Greater<p>
     *                  gte("_id", 1); == Greater or equal<p>
     *                  lt("_id", 1); == Less<p>
     *                  lte("_id", 1); == Less or equal<p>
     *                  ne("_id", 1); == not Equals<p>
     *                  in("array", 1); == OBJ in array<p>
     *                  nin("array", 1); == not in Array<p>
     *                  empty("array", 1); == ALL<p>
     * <p>
     *                Boolean Logic:
     * <p>
     *                  and(eq(),eq())          AND operator with Comp methods      LOGICAL AND<p>
     *                  or(eq(),eq())           OR operator with Comp methods       LOGICAL OR<p>
     *                  not(eq())               NOT operator with Comp methods      LOGICAL NOT<p>
     *                  nor(eq(),eq()...)       NOR operator with Comp methods      LOGICAL NOR<p>
     * <p>
     *                Sort options:
     *                  ascending("_id") -- ASC MYSQL<p>
     *                  descending("_id") -- DESC MYSQL<p>
     *                  orderBy(ascending("_id"),descending("xy")) -- ORDER BY MYSQL<p>
     * <p>
     *
     *                set("_id", 1); == Set Colum - Value<p>
     *                addToSet("vendor", "C"); == Array add Set Colum(ARRAY) - Value<p>
     *                pull("vendor", "D"); == Remove ALL Set Colum(ARRAY) - Value<p>
     *                push("vendor", "C"); == Array append Set Colum(ARRAY) - Value<p>
     *                combine(set(),pull()) Combined update<p>
     */
    public CompletableFuture<DeleteResult> Delete(String collection, Bson where) {
        return CompletableFuture.supplyAsync(LambdaThrower.rethrowSupplier(() -> Delete_internal(collection,where)));
    }
}
