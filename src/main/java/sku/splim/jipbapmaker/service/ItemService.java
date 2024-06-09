package sku.splim.jipbapmaker.service;

import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private LogService logService;

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

    public Item save(Item item){
        return itemRepository.save(item);
    }

    public List<Item> getfindTopItemsByCountNotZero(){
        return itemRepository.findTopItemsByCountNotZero();
    }

    public List<Item> getfindAll(){
        return itemRepository.findAll();
    }

    public List<Item> findTopItemsByCountDesc(){
        return itemRepository.findTopItemsByCountDesc();
    }

    public void uploadImage(int code, String name, Admin admin){
        Item item = itemRepository.findByItemCode(code);
        item.setImagePath(name);
        logService.upload(admin, item);
        itemRepository.save(item);
    }

}
