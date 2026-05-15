package br.com.monitoria;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class PersistenciaNoSql {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private static final String URI =
            "mongodb://root:root@localhost:27017/nosql-monitores-mongo?authSource=admin";
    private static final String DB_NAME = "nosql-monitores";

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(URI);
        }
        if (database == null) {
            database = mongoClient.getDatabase(DB_NAME);
        }
        return database;
    }

    public static void fechar() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
}