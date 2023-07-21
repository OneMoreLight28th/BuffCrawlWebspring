package com.example.demo.Service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    private static final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    public MongoDatabase getDatabase(String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }
}
