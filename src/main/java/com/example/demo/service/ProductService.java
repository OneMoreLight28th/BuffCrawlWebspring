package com.example.demo.service;

import com.example.demo.entity.ProductDocument;
import com.example.demo.entity.Sticker;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


/**
 * 成交记录
 */


@Service
public class ProductService {

    private static final Logger logger = LogManager.getLogger(ProductService.class);

    private final DatabaseService databaseService;

    public ProductService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public ResponseEntity<List<ProductDocument>> getProductInfo(String name, int page, int pageSize) throws UnsupportedEncodingException {
        MongoDatabase database = databaseService.getDatabase("csgo_items");
        MongoCollection<Document> collection = database.getCollection("newcsgo_items");

        logger.info("收到的请求: {}", name);
        logger.info("page:{},pageSize:{}", page, pageSize);

        String decodedName = URLDecoder.decode(name, "UTF-8");

        // 根据商品名称进行查询
        Bson query = Filters.eq("name", decodedName);
        logger.info("查询信息:{}", query);

        int startIndex = (page - 1) * pageSize;
//        System.out.println("startIndex: " + startIndex);
//        System.out.println("pageSize: " + pageSize);

        // 按时间戳字段进行倒序排序
        Bson sort = Sorts.descending("timestamp");


        // 执行查询操作，获取商品文档列表，并按照时间戳字段倒序排序
        List<ProductDocument> productList = new ArrayList<>();
        FindIterable<Document> documents = collection.find(query).sort(sort);

        // 遍历所有文档，将需要的文档添加到 productList 中
        int totalItems = 0;
        for (Document doc : documents) {
            Object containerObj = doc.get("container");
            if (containerObj instanceof Document) {
                Document containerDoc = (Document) containerObj;
                handleContainerDocument(containerDoc, productList, name, page, pageSize);
            } else if (containerObj instanceof List) {
                List<Document> containerDocs = (List<Document>) containerObj;
                for (Document containerDoc : containerDocs) {
                    handleContainerDocument(containerDoc, productList, name, page, pageSize);
                }
            } else {
                logger.warn("商品 {} 的 container 为空。", decodedName);
            }

            totalItems += productList.size(); // 更新 totalItems

            if (totalItems >= page * pageSize) {
                // 如果当前总条目数已经超过当前页的末尾，则只取前面一部分加入到 productList 中
                int remainingItems = page * pageSize - (totalItems - productList.size());
                productList = productList.subList(0, remainingItems);
                break;
            }
        }

        logger.info("查询到的商品信息: {}", productList);

        if (productList.isEmpty()) {
            // 如果查询结果为空，返回 404 Not Found
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(productList);
        }
    }

    private void handleContainerDocument(Document containerDoc, List<ProductDocument> productList, String name, int page, int pageSize) {
        int totalItems = productList.size(); // 获取当前 productList 中的总条目数
        int startIndex = (page - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        int addedItems = 0; // 当前已经添加的商品信息数量

        for (String key : containerDoc.keySet()) {
            List<Document> transactions = containerDoc.getList(key, Document.class);
            if (transactions != null) {
                for (Document transaction : transactions) {
                    totalItems++; // 每读取一条商品信息，更新总数
                    addedItems++; // 每添加一个商品信息，更新当前已添加的数量

                    // 仅添加在分页范围内的商品信息
                    if (addedItems > startIndex && addedItems <= endIndex) {
                        ProductDocument productDoc = new ProductDocument();
                        productDoc.setName(name);
                        productDoc.setInspectionImg(transaction.get("inspection_img", Binary.class));
                        productDoc.setItemFloat(transaction.getString("itemfloat"));
                        productDoc.setPaintSeed(transaction.getInteger("paintseed"));
                        productDoc.setPrice(transaction.getString("price"));
                        productDoc.setTransactionTime(toLocalDateTime(transaction.getInteger("transaction_time")));

                        if (transaction.containsKey("stickers")) {
                            List<Document> stickerDocs = transaction.getList("stickers", Document.class);
                            if (stickerDocs != null) {
                                List<Sticker> stickers = new ArrayList<>();
                                for (Document stickerDoc : stickerDocs) {
                                    Sticker sticker = new Sticker();
                                    sticker.setImgData(stickerDoc.get("img_data", Binary.class));
                                    sticker.setName(stickerDoc.getString("name"));
                                    sticker.setSlot(stickerDoc.getInteger("slot"));

                                    Object wearValue = stickerDoc.get("wear");
                                    if (wearValue instanceof Integer) {
                                        int wear = (Integer) wearValue;
                                        sticker.setWear(wear);
                                    } else if (wearValue instanceof Double) {
                                        double wear = (Double) wearValue;
                                        sticker.setWear(wear);
                                    }

                                    stickers.add(sticker);
                                }
                                productDoc.setStickers(stickers);
                            }
                        }

                        productList.add(productDoc);
                    }

                    // 如果已经达到分页末尾，跳出循环，不再继续处理剩余数据
                    if (addedItems >= endIndex) {
                        break;
                    }
                }
            }

            // 如果已经达到分页末尾，不再继续处理剩余数据
            if (addedItems >= endIndex) {
                break;
            }
        }
    }

    private LocalDateTime toLocalDateTime(long timestamp) {
        // 将时间戳转换为本地日期时间格式
        Instant instant = Instant.ofEpochSecond(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
        return zonedDateTime.toLocalDateTime();
    }
}
