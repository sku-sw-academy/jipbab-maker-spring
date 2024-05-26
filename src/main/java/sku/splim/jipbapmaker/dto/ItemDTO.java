package sku.splim.jipbapmaker.dto;

import sku.splim.jipbapmaker.domain.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO {
    private String item_name;
    private int item_code;
    private CategoryDTO category;
    private String imagePath;
    private int count;

    public ItemDTO() {}

    public ItemDTO(String item_name, int item_code, CategoryDTO category, int count) {
        this.item_name = item_name;
        this.item_code = item_code;
        this.category = category;
        this.count = count;
    }

    public ItemDTO convertToDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItem_code(item.getItemCode());
        itemDTO.setItem_name(item.getItemName());
        itemDTO.setCount(item.getCount());
        CategoryDTO categoryDTO = new CategoryDTO();

        itemDTO.setCategory(categoryDTO.convertToDTO(item.getCategoryCode()));
        return itemDTO;
    }
}
