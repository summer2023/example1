package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.Inventory;
import com.thoughtworks.jingxiMallapi.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/inventories")
public class InventoryController {
    @Autowired
    InventoryRepository inventoryRepository;

    //修改库存信息
    @RequestMapping(method = RequestMethod.POST)
    public void saveInventory(@ModelAttribute Long productId){
        inventoryRepository.saveByProductId(productId);
    }

    //查找所有库存
    @RequestMapping(method = RequestMethod.GET)
    public List<Inventory> getInventories() {
        return inventoryRepository.findAll();
    }

    //修改库存信息，输入代表新增的库存数量
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Inventory inventory) throws Exception {
        if (inventoryRepository.findInventoryById(id) == null) {
            return new ResponseEntity<>("Cannot find such inventory with input id.", HttpStatus.NOT_FOUND);
        }
        inventoryRepository.updateCountById(id, inventory.getCount());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
