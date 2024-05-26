package sku.splim.jipbapmaker.controller;

import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.dto.ItemDTO;
import sku.splim.jipbapmaker.repository.ItemRepository;
import sku.splim.jipbapmaker.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/names")
    public List<String> getAllItemNames(){
        return itemService.getAllItemNames();
    }

    @GetMapping("/{code}")
    public List<String> getCategoryItem(@PathVariable("code") int code){
        return itemService.getCategoryItem(code);
    }

    @PostMapping("/increment/{itemName}")
    public ItemDTO incrementItemCount(@PathVariable("itemName") String itemName) {
        Item item = itemService.findByItemName(itemName);
        if (item == null) {
            throw new IllegalArgumentException("Invalid item name: " + itemName);
        }

        item.incrementCount();
        item = itemService.save(item);

        ItemDTO itemDto = new ItemDTO();
        itemDto.setItem_code(item.getItemCode());
        itemDto.setItem_name(item.getItemName());
        itemDto.setCount(item.getCount());

        return itemDto;
    }

}
