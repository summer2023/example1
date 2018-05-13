package com.thoughtworks.jingxiMallapi.controller;

import com.thoughtworks.jingxiMallapi.entity.Inventory;
import com.thoughtworks.jingxiMallapi.exception.ItemNotFoundException;
import com.thoughtworks.jingxiMallapi.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/inventories")
public class InventoryController {
    @Autowired
    InventoryRepository inventoryRepository;

    //修改库存信息
    @PostMapping
    public void saveInventory(@ModelAttribute Long productId) {
        inventoryRepository.saveByProductId(productId);
    }

    //查找所有库存
    @GetMapping
    public List<Inventory> getInventories() {
        return inventoryRepository.findAll();
    }

    //修改库存信息，输入代表新增的库存数量
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable Long id, @RequestBody Inventory inventory) throws Exception {
        inventoryRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Inventory", id));
        inventoryRepository.updateCountById(id, inventory.getCount());
    }

}
