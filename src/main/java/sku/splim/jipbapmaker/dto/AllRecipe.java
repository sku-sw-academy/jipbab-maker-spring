package sku.splim.jipbapmaker.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class AllRecipe {
    private Long id;
    private UserDTO userDTO;
    private String title;
    private String content;
    private String comment;
    private String image;
    private boolean status;
    private boolean deletedAt;
    private Timestamp createDate;
    private Timestamp modifyDate;
    private boolean owner;
}
