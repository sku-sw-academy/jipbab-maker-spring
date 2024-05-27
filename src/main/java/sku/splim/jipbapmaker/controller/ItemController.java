package sku.splim.jipbapmaker.controller;

import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.dto.CategoryDTO;
import sku.splim.jipbapmaker.dto.ItemDTO;
import sku.splim.jipbapmaker.repository.ItemRepository;
import sku.splim.jipbapmaker.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
        CategoryDTO categoryDTO = new CategoryDTO();
        itemDto.setItem_code(item.getItemCode());
        itemDto.setItem_name(item.getItemName());
        itemDto.setCount(item.getCount());
        itemDto.setCategory(categoryDTO.convertToDTO(item.getCategoryCode()));

        return itemDto;
    }

    @GetMapping("/all")
    public List<ItemDTO> getAllItems(){
        List<ItemDTO> itemDTOS = new ArrayList<>();
        List<Item> items = itemService.getfindAll();

        for(Item item : items){
            ItemDTO itemDto = new ItemDTO();
            CategoryDTO categoryDTO = new CategoryDTO();

            itemDto.setItem_name(item.getItemName());
            itemDto.setItem_code(item.getItemCode());
            itemDto.setCount(item.getCount());
            itemDto.setCategory(categoryDTO.convertToDTO(item.getCategoryCode()));
            itemDTOS.add(itemDto);
        }

        return itemDTOS;
    }

}
