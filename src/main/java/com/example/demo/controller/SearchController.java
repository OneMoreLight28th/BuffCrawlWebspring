package com.example.demo.controller;

import com.example.demo.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索
 */

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(path = "/api/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public String search(
            @RequestParam(value="searchKey", required=true) String query,
            @RequestParam(value="page", required=false, defaultValue="1") Integer page,
            @RequestParam(value="pageSize", required=false, defaultValue="24") Integer pageSize
    ) throws JsonProcessingException {
        return searchService.search(query, page, pageSize);
    }
}
