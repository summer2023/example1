package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.Product;
import com.thoughtworks.jingxiMallapi.repository.InventoryRepository;
import com.thoughtworks.jingxiMallapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    InventoryRepository inventoryRepository;

    //创建新商品
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getPrice() == null) {
            return new ResponseEntity<String>("Input product illegal!", HttpStatus.BAD_REQUEST);
        }
        Long id = productRepository.saveAndFlush(product).getId();
        URI location = URI.create("http://192.168.56.1:8083/products/" + id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        productRepository.save(product);
        inventoryRepository.saveByProductId(id);
        return new ResponseEntity<Product>(productRepository.findProductById(id), responseHeaders, HttpStatus.CREATED);
    }

    //修改商品信息
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) throws Exception {
        if (productRepository.findProductById(id) == null) {
            return new ResponseEntity<>("Cannot find such product with input id.", HttpStatus.NOT_FOUND);
        }
        productRepository.updateById(id, product.getName(), product.getDescription(), product.getPrice());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //根据商品id查找商品
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            return new ResponseEntity<String>("Cannot find such product with input id.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    //查找所有商品
    //根据name和描述模糊查询
    //根据name查询
    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getProducts(@RequestParam(value = "name", required = false, defaultValue = "") String name, @RequestParam(value = "description", required = false, defaultValue = "") String description) {
        if (!name.isEmpty() && !description.isEmpty()) {
            return productRepository.findByNameAndDescriptionContaining(name, description);
        } else if (!name.isEmpty()) {
            return productRepository.findByName(name);
        } else {
            return productRepository.findAll();
        }
    }

}
