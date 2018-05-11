package com.thoughtworks.jingxiMallapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.jingxiMallapi.JingxiMallapiApplication;
import com.thoughtworks.jingxiMallapi.entity.Product;
import com.thoughtworks.jingxiMallapi.repository.InventoryRepository;
import com.thoughtworks.jingxiMallapi.repository.ProductRepository;
import jdk.nashorn.internal.runtime.arrays.AnyElements;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = JingxiMallapiApplication.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private InventoryRepository inventoryRepository;

    @Test
    public void should_return_product_when_given_exist_id() throws Exception {
        Product product = new Product("矿泉水", "农夫山泉", 3);
        given(productRepository.findProductById(1L)).willReturn(product);
        mvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value("矿泉水"))
                .andExpect(jsonPath("$.description").value("农夫山泉"))
                .andExpect(jsonPath("$.price").value(3));
    }

    @Test
    public void should_return_wrong_message_when_given_nonexistent_id() throws Exception {
        given(productRepository.findProductById(2L)).willReturn(null);
        mvc.perform(get("/products/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Ignore
    public void should_save_product_when_input_valid() throws Exception {
        Product product = new Product("矿泉水", "农夫山泉", 3);
        ObjectMapper mapper = new ObjectMapper();
        product.setId(1L);
        given(productRepository.save(product)).willReturn(product);
        given(inventoryRepository.saveByProductId(1L)).willReturn(1);
        mvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(product)))
                .andExpect(status().isCreated());
    }

}