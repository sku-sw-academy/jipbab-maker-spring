package com.example.demo.controller;

import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;
import com.example.demo.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/prices")
public class PriceController {
    @Autowired
    private PriceService priceService;

    @Autowired
    private Item item;

    @Autowired
    private ItemService itemService;

    @GetMapping("/kinds/{itemname}")
    public List<String> findDistinctKindNamesByItemCode(@PathVariable("itemname") String itemname){
        item = itemService.findByItemName(itemname);
        if (item != null) {
            // 아이템을 찾았다면 해당 아이템 코드를 사용하여 메서드를 호출합니다.
            return priceService.findDistinctKindNamesByItemCode(item.getItemCode());
        } else {
            // 아이템을 찾지 못했을 때의 처리를 추가할 수 있습니다.
            return Collections.emptyList(); // 예: 빈 리스트를 반환
        }
    }

    @GetMapping("/ranks/{kind}")
    public List<String> findDistinctRankNamesByKindName(@PathVariable("kind") String kind){
        return priceService.findDistinctRankNamesByKindName(kind);
    }

    @GetMapping("/save/name")
    public List<String> getTop10Name(){
        return null;
    }

}
