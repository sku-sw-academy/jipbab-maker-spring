package sku.splim.jipbapmaker.dto;

import sku.splim.jipbapmaker.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO {
    String item_name;
    int item_code;
    CategoryDTO category;
    private String imagePath;

    public ItemDTO() {}

    public ItemDTO(String item_name, int item_code, CategoryDTO category) {
        this.item_name = item_name;
        this.item_code = item_code;
        this.category = category;
    }

    public ItemDTO convertToDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItem_code(item.getItemCode());
        itemDTO.setItem_name(item.getItemName());

        CategoryDTO categoryDTO = new CategoryDTO();

        itemDTO.setCategory(categoryDTO.convertToDTO(item.getCategoryCode()));
        return itemDTO;
    }
}
