package com.example.demo.controller;

import com.example.demo.entity.Item;
import com.example.demo.entity.Price;
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

    @GetMapping("/save/names")
    public List<String> getTop10Name(){
        List<Price> prices = priceService.findFirst10ByValueIsNotNullOrderByRegDayDescValueAsc();
        List<String> names = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            Item item = itemService.findByItemCode(prices.get(i).getItemCode().getItemCode());
            names.add(item.getItemName());
        }
        return names;
    }

    @GetMapping("/save/kinds")
    public List<String> getTop10Kind(){
        List<Price> prices = priceService.findFirst10ByValueIsNotNullOrderByRegDayDescValueAsc();
        List<String> kinds = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            kinds.add(prices.get(i).getKindName());
        }
        return kinds;
    }

    @GetMapping("/save/ranks")
    public List<String> getTop10Rank(){
        List<Price> prices = priceService.findFirst10ByValueIsNotNullOrderByRegDayDescValueAsc();
        List<String> ranks = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            ranks.add(prices.get(i).getRankName());
        }
        return ranks;
    }

    @GetMapping("/save/values")
    public List<Double> getTop10Value(){
        List<Price> prices = priceService.findFirst10ByValueIsNotNullOrderByRegDayDescValueAsc();
        List<Double> values = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            values.add(prices.get(i).getValue());
        }
        return values;
    }

}
