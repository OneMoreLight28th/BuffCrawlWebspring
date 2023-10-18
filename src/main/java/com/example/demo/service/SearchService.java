package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 搜索
 */
@Service
public class SearchService {

    private final ObjectMapper objectMapper;
    private final DatabaseService databaseService;

    public SearchService(ObjectMapper objectMapper, DatabaseService databaseService) {
        this.objectMapper = objectMapper;
        this.databaseService = databaseService;
    }

    public String search(String query, Integer page, Integer pageSize) throws JsonProcessingException {
        MongoDatabase database = databaseService.getDatabase("csgo_items");
        MongoCollection<Document> collection = database.getCollection("newcsgo_items");

        String escapedQuery = Pattern.quote(query);
        System.out.println(escapedQuery);

        Bson filter = Filters.regex("name", escapedQuery, "i");
        List<Document> pageItems = executeQuery(filter, page, pageSize);

        long totalItems = collection.countDocuments(filter);
        long totalPages = (totalItems + pageSize - 1) / pageSize;

        Map<String, Object> result = new HashMap<>();
        result.put("items", pageItems);
        result.put("totalItems", totalItems);
        result.put("totalPages", totalPages);
        return objectMapper.writeValueAsString(result);
    }

    private List<Document> executeQuery(Bson filter, Integer page, Integer pageSize) {
        MongoDatabase database = databaseService.getDatabase("csgo_items");
        MongoCollection<Document> collection = database.getCollection("newcsgo_items");

        List<Document> pageItems = new ArrayList<>();
        if (page != 0) {
            List<Document> docs = collection.find(filter)
                    .skip((page - 1) * pageSize)
                    .limit(pageSize)
                    .into(new ArrayList<>());
            pageItems.addAll(docs);
        }
        return pageItems;
    }
}
