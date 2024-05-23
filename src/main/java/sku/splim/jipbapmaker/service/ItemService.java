package sku.splim.jipbapmaker.service;

import sku.splim.jipbapmaker.entity.Item;
import sku.splim.jipbapmaker.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Item findByItemCode(int code){
        return itemRepository.findByItemCode(code);
    }

    public List<String> getCategoryItem(int code){
        return itemRepository.findItemNamesByCategoryCode(code);
    }

}