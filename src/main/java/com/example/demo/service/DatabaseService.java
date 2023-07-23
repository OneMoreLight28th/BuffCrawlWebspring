package com.example.demo.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.stereotype.Service;


/**
 * mongodb配置
 */

@Service
public class DatabaseService {

    private static final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    public MongoDatabase getDatabase(String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }
}
