package com.example.main.controller;

import com.example.main.entity.ProductDocument;
import com.example.main.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 成交记录
 */

@RestController
public class TransactionController {

    private final ProductService productService;

    public TransactionController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "/api/content", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDocument>> getProductInfo(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) throws UnsupportedEncodingException {

        // 解码URL参数
        String decodedName = URLDecoder.decode(name, StandardCharsets.UTF_8);

        ResponseEntity<List<ProductDocument>> response = productService.getProductInfo(decodedName, page, pageSize);

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.notFound().build();
        } else {
            return response;
        }
    }
}
