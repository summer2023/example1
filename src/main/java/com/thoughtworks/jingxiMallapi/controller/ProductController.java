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

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        URI location = URI.create("http://192.168.56.1:8083/products");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        productRepository.save(product);
        inventoryRepository.saveByProductId(product.getId());
        return new ResponseEntity<>(getProduct(product.getId()), responseHeaders, HttpStatus.CREATED);
    }


    //修改商品信息
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) throws Exception {
        HttpStatus status = getProduct(id) != null ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND;
        if (getProduct(id) == null) {
            throw new Exception("Product not found by id: " + id);
        }
        productRepository.updateById(id, product.getName(), product.getDescription(), product.getPrice());
        return new ResponseEntity<>(status);
    }

    //根据商品id查找商品
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findProductById(id);
    }

    //查找所有商品
    //根据name和描述模糊查询
    //根据name查询
    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getProducts(@RequestParam(value = "name",required = false, defaultValue = "") String name, @RequestParam(value = "description",required = false, defaultValue = "") String description) {
        if (!name.isEmpty() && !description.isEmpty()) {
            return productRepository.findByNameAndDescriptionContaining(name,description);
        } else if (!name.isEmpty()) {
            return productRepository.findByName(name);
        } else {
            return productRepository.findAll();
        }
    }

    //根据name和描述模糊查询
//    @RequestMapping(method = RequestMethod.GET)
//    public List<Product> getProducts(@PathVariable String name,@PathVariable String description) {
//        return productRepository.findByNameAndDescriptionContaining(name, description);
//    }
}
