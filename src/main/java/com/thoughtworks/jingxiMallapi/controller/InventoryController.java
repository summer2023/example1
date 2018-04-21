package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.Inventory;
import com.thoughtworks.jingxiMallapi.entity.Product;
import com.thoughtworks.jingxiMallapi.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
//        Inventory inventory = new Inventory(productId,0);
        inventoryRepository.saveByProductId(productId);
    }

    //查找所有库存
    @RequestMapping(method = RequestMethod.GET)
    public List<Inventory> getInventories() {
        return inventoryRepository.findAll();
    }
}
