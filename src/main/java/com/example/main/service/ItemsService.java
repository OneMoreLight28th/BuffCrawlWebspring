package com.example.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 商品展示
 */

@Service
public class ItemsService {


    @Autowired
    private DatabaseService databaseService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getItemData(Integer page, Integer pageSize) throws JsonProcessingException {
        MongoCollection<Document> collection = databaseService.getCollection("csgo_items", "newcsgo_items");

        long totalItems = collection.countDocuments();
        long totalPages = (totalItems + pageSize - 1) / pageSize;

        List<Document> pageItems = new ArrayList<>();
        if (page != 0) {
            List<Document> sample = collection.aggregate(Arrays.asList(
                    new Document("$sample", new Document("size", pageSize))
            )).into(new ArrayList<>());
            pageItems.addAll(sample);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("items", pageItems);
        result.put("totalItems", totalItems);
        result.put("totalPages", totalPages);

        return objectMapper.writeValueAsString(result);
    }
}
