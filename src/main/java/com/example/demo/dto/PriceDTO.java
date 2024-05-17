package com.example.demo.dto;

import com.example.demo.entity.Item;
import com.example.demo.entity.Price;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class PriceDTO {
    private String id;
    private String name;
    private ItemDTO itemCode;
    private String kindName;
    private int kindCode;
    private String rankName;
    private int rankCode;
    private String unit;
    private String dpr1;
    private String dpr2;
    private String dpr3;
    private String dpr5;
    private String dpr6;
    private String dpr7;
    private String regday;
    private double value;
    private boolean status;

    // 생성자, Getter 및 Setter 메서드
    public PriceDTO() {}

    public PriceDTO(String id, String name, ItemDTO itemCode, String kindName, int kindCode, String rankName, int rankCode, String unit,  String dpr1,  String dpr2,  String dpr3,   String dpr5,  String dpr6,  String dpr7, String regday, Double value, boolean status) {
        this.id = id;
        this.name = name;
        this.itemCode = itemCode;
        this.kindName = kindName;
        this.kindCode = kindCode;
        this.rankName = rankName;
        this.rankCode = rankCode;
        this.unit = unit;;
        this.dpr1 = dpr1;
        this.dpr2 = dpr2;
        this.dpr3 = dpr3;;
        this.dpr5 = dpr5;
        this.dpr6 = dpr6;
        this.dpr7 = dpr7;
        this.regday = regday;
        this.value = value;
        this.status = status;
    }

    public PriceDTO convertToDTO(Price price) {
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setId(price.getId());
        priceDTO.setName(price.getName());
        priceDTO.setKindName(price.getKindName());
        priceDTO.setKindCode(price.getKindCode());
        priceDTO.setRankName(price.getRankName());
        priceDTO.setRankCode(price.getRankCode());
        priceDTO.setUnit(price.getUnit());
        priceDTO.setDpr1(price.getDpr1());
        priceDTO.setDpr2(price.getDpr2());
        priceDTO.setDpr3(price.getDpr3());
        priceDTO.setDpr5(price.getDpr5());
        priceDTO.setDpr6(price.getDpr6());
        priceDTO.setDpr7(price.getDpr7());
        priceDTO.setRegday(price.getRegday());
        priceDTO.setStatus(price.getStatus());
        priceDTO.setValue(price.getValue());

        ItemDTO itemDTO = new ItemDTO();
        priceDTO.setItemCode(itemDTO.convertToDTO(price.getItemCode()));

        return priceDTO;
    }

}
