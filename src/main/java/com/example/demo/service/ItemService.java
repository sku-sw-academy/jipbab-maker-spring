package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.entity.Price;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public List<String> getAllItemNames() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map(Item::getItemName).collect(Collectors.toList());
    }

    public Item findByItemName(String name){
        return itemRepository.findByItemName(name);
    }

    public List<String> getCategoryItem(int code){
        return itemRepository.findItemNamesByCategoryCode(code);
    }

}
