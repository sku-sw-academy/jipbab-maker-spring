package com.example.demo.controller;

import com.example.demo.dto.PriceDTO;
import com.example.demo.entity.Item;
import com.example.demo.entity.Price;
import com.example.demo.service.ItemService;
import com.example.demo.service.PriceService;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/saving/top3/{regday}") //메인페이지 알뜰
    public List<PriceDTO> getTop3Names(@PathVariable("regday") String regday){
        List<Price> prices = priceService.findFirst3ByRegdayOrderByValue(regday);
        List<PriceDTO> priceDTOs = new ArrayList<>();

        for(Price price : prices){
            PriceDTO priceDTO = new PriceDTO();
            priceDTOs.add(priceDTO.convertToDTO(price));
        }

        return priceDTOs;
    }

    @GetMapping("/save/names/{regday}") //알뜰 소비 페이지
    public List<String> getNames(@PathVariable("regday") String regday){
        List<Price> prices = priceService.findByRegdayOrderByValue(regday);
        List<String> names = new ArrayList<>();

        for(Price price : prices){
            Item item = itemService.findByItemCode(price.getItemCode().getItemCode());
            names.add(item.getItemName());
        }
        return names;
    }

    @GetMapping("/saving/detail/{regday}") // 알뜰 소비 페이지
    public ResponseEntity<List<PriceDTO>> getDetails(@PathVariable("regday") String regday) {
        List<Price> prices = priceService.findByRegdayOrderByValue(regday);

        if (prices.isEmpty()) {
            // 데이터가 없으면 404 상태 코드를 반환
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 데이터가 있으면 DTO로 변환하여 반환
        List<PriceDTO> priceDTOs = new ArrayList<>();
        for (Price price : prices) {
            PriceDTO priceDTO = new PriceDTO();
            priceDTOs.add(priceDTO.convertToDTO(price));
        }

        return new ResponseEntity<>(priceDTOs, HttpStatus.OK);
    }

    @GetMapping("/save/kinds/{regday}") //알뜰 소비 페이지
    public List<String> getKinds(@PathVariable("regday") String regday){
        List<Price> prices = priceService.findByRegdayOrderByValue(regday);
        List<String> kinds = new ArrayList<>();

        for(Price price : prices){
            kinds.add(price.getKindName());
        }
        return kinds;
    }

    @GetMapping("/save/ranks/{regday}") //알뜰 소비 페이지
    public List<String> getRanks(@PathVariable("regday") String regday){
        List<Price> prices = priceService.findByRegdayOrderByValue(regday);
        List<String> ranks = new ArrayList<>();

        for(Price price : prices){
            ranks.add(price.getRankName());
        }
        return ranks;
    }

    @GetMapping("/save/values/{regday}") //알뜰 소비 페이지
    public List<Double> getValues(@PathVariable("regday") String regday){
        List<Price> prices = priceService.findByRegdayOrderByValue(regday);
        List<Double> values = new ArrayList<>();

        for(Price price : prices){
            values.add(price.getValues());
        }
        return values;
    }

    @GetMapping("/kinds/{itemname}")  //검색 결과 페이지
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

    @GetMapping("/ranks/{kind}") //검색 결과 페이지
    public List<String> findDistinctRankNamesByKindName(@PathVariable("kind") String kind){
        return priceService.findDistinctRankNamesByKindName(kind);
    }

}
