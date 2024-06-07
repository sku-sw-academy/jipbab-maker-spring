package sku.splim.jipbapmaker.domain;

import sku.splim.jipbapmaker.dto.ItemDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "count")
    private int count;

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

    public void incrementCount() {
        this.count += 1;
    }

    public Item convertToEntity(ItemDTO itemDTO) {
        Item item = new Item();
        item.setItemCode(itemDTO.getItem_code());
        item.setItemName(itemDTO.getItem_name());
        item.setCount(itemDTO.getCount());
        Category category = new Category();
        item.setCategoryCode(category.convertToEntity(itemDTO.getCategory()));

        return item;
    }
}
