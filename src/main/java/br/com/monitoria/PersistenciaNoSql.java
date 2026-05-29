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

    public static synchronized MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(DB_NAME);
            } catch (Exception e) {
                System.err.println("Falha ao conectar com o MongoDB: " + e.getMessage());
                throw new RuntimeException("Não foi possível conectar ao banco de dados NoSQL.", e);
            }
        }
        return database;
    }

    public static synchronized void fechar() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
}