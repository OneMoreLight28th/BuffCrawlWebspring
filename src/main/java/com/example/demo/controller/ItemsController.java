package com.example.demo.controller;

import com.example.demo.service.ItemsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主页数据
 */

@RestController
public class ItemsController {

    private final ItemsService itemsService;

    public ItemsController(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(path = "/api/getItemData", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getItemData(
            @RequestParam(value="page", required=false, defaultValue="1") Integer page,
            @RequestParam(value="pageSize", required=false, defaultValue="24") Integer pageSize
    ) throws JsonProcessingException {
        return itemsService.getItemData(page, pageSize);
    }
}






