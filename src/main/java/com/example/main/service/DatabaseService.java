package com.example.main.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * mongodb配置
 */

@Service
public class DatabaseService {

    private static final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private static final Map<String, MongoDatabase> databases = new HashMap<>();
    private static final Map<String, MongoCollection<Document>> collections = new HashMap<>();

    public MongoDatabase getDatabase(String databaseName) {
        return databases.computeIfAbsent(databaseName, k -> mongoClient.getDatabase(databaseName));
    }

    public MongoCollection<Document> getCollection(String databaseName, String collectionName) {
        String key = databaseName + "." + collectionName;
        return collections.computeIfAbsent(key, k -> getDatabase(databaseName).getCollection(collectionName));
    }
}
