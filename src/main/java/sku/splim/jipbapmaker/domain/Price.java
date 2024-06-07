package sku.splim.jipbapmaker.domain;

import sku.splim.jipbapmaker.dto.PriceDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "Price")
public class Price {
    @Id
    private String Id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", referencedColumnName = "item_code")
    private Item itemCode;

    @Column
    private String kindName;

    @Column
    private int kindCode;

    @Column
    private String rankName;

    @Column
    private int rankCode;

    @Column
    private String unit;

    @Column(name = "regday_price")
    private String dpr1;

    @Column(name = "yesterday")
    private String dpr2;

    @Column(name = "week")
    private String dpr3;

    @Column(name = "month")
    private String dpr5;

    @Column(name = "year")
    private String dpr6;

    @Column(name = "avg_year")
    private String dpr7;

    @Column
    private String regday;

    @Column(name = "value")
    private Double values;

    @Column
    private Boolean status;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createDate = now;
        modifyDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        modifyDate = new Timestamp(System.currentTimeMillis());
    }

    public Price convertToEntity(PriceDTO priceDTO) {
        Price price = new Price();
        price.setId(priceDTO.getId());
        price.setName(priceDTO.getName());
        price.setKindName(priceDTO.getKindName());
        price.setKindCode(priceDTO.getKindCode());
        price.setRankName(priceDTO.getRankName());
        price.setRankCode(priceDTO.getRankCode());
        price.setUnit(priceDTO.getUnit());
        price.setDpr1(priceDTO.getDpr1());
        price.setDpr2(priceDTO.getDpr2());
        price.setDpr3(priceDTO.getDpr3());
        price.setDpr5(priceDTO.getDpr5());
        price.setDpr6(priceDTO.getDpr6());
        price.setDpr7(priceDTO.getDpr7());
        price.setRegday(priceDTO.getRegday());
        price.setStatus(priceDTO.isStatus());
        price.setValues(priceDTO.getValues());

        Item item = new Item();
        price.setItemCode(item.convertToEntity(priceDTO.getItemCode()));

        return price;
    }

}

