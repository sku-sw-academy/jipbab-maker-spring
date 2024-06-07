package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.*;
import sku.splim.jipbapmaker.dto.AdminDTO;
import sku.splim.jipbapmaker.dto.UserDTO;

import java.sql.Timestamp;

@Table(name = "admin")
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String name;

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

    @Builder
    public Admin(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static Admin convertToEntity(AdminDTO adminDTO) {
        return Admin.builder()
                .id(adminDTO.getId())
                .email(adminDTO.getEmail())
                .password(adminDTO.getPassword())
                .name(adminDTO.getName())
                .build();
    }
}
