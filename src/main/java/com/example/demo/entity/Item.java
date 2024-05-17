package com.example.demo.entity;

import com.example.demo.dto.ItemDTO;
import com.example.demo.repository.CategoryRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "Item")
public class Item {
    @Id
    @Column(name = "item_code")
    private int itemCode;

    @Column(name = "item_name")
    private String itemName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_code", referencedColumnName = "category_code")
    private Category categoryCode;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "image_path", nullable = true)
    private String imagePath;

    public Item convertToEntity(ItemDTO itemDTO) {
        Item item = new Item();
        item.setItemCode(itemDTO.getItem_code());
        item.setItemName(itemDTO.getItem_name());
        Category category = new Category();
        item.setCategoryCode(category.convertToEntity(itemDTO.getCategory()));

        return item;
    }
}
