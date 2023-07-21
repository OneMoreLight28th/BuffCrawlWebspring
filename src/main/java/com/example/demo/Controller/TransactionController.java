package com.example.demo.Controller;

import com.example.demo.Impclass.ProductDocument;
import com.example.demo.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:8080")
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
        String decodedName = URLDecoder.decode(name, "UTF-8");

        // 调用 ProductService 的方法，根据传入的 decodedName、page 和 pageSize 进行分页查询
        ResponseEntity<List<ProductDocument>> response = productService.getProductInfo(decodedName, page, pageSize);

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // 如果查询结果为空，返回 404 Not Found
            return ResponseEntity.notFound().build();
        } else {
            // 返回查询结果
            return response;
        }
    }
}
