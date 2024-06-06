package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Builder
    public Admin(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }


}
