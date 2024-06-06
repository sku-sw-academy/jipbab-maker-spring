package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "refreshToken_admin")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class RefreshToken_admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(name = "refresh_token", columnDefinition = "TEXT", nullable = false)
    private String refreshToken;

    public RefreshToken_admin(Admin admin, String refreshToken) {
        this.admin = admin;
        this.refreshToken = refreshToken;
    }

    public RefreshToken_admin update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }

    public Long getAdminId() {
        return admin != null ? admin.getId() : null;
    }

    @Builder
    public RefreshToken_admin(Long id, Admin admin, String refreshToken) {
        this.id = id;
        this.admin = admin;
        this.refreshToken = refreshToken;
    }

}
