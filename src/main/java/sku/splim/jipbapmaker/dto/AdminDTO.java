package sku.splim.jipbapmaker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.Admin;

@Getter
@Setter
@Builder
public class AdminDTO {
    private Long id;
    private String email;
    private String password;
    private String name;


    public AdminDTO() {
    }

    @Builder
    public AdminDTO(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static AdminDTO convertToDTO(Admin admin) {
        return AdminDTO.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .password(admin.getPassword())
                .name(admin.getName())
                .build();
    }
}
