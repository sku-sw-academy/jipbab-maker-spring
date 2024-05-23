package sku.splim.jipbapmaker.controller;

import sku.splim.jipbapmaker.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
