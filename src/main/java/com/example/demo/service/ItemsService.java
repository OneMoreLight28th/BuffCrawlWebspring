package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 商品主界面
 */

@Service
public class ItemsService {


    private final ObjectMapper objectMapper;
    private final DatabaseService databaseService;

    public ItemsService(ObjectMapper objectMapper, DatabaseService databaseService) {
        this.objectMapper = objectMapper;
        this.databaseService = databaseService;
    }

    public String getItemData(Integer page, Integer pageSize) throws JsonProcessingException {
        MongoDatabase database = databaseService.getDatabase("csgo_items");
        MongoCollection<Document> collection = database.getCollection("newcsgo_items");


        long totalItems = collection.countDocuments();
        long totalPages = (totalItems + pageSize - 1) / pageSize;

        List<Document> pageItems = new ArrayList<>();
        if (page != 0) {
            // 第一页数据随机返回
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
