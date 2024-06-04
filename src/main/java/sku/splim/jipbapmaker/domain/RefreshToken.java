package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "RefreshToken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "refresh_token", columnDefinition = "TEXT", nullable = false)
    private String refreshToken;

    public RefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @Builder
    public RefreshToken(Long id, User user, String refreshToken) {
        this.id = id;
        this.user = user;
        this.refreshToken = refreshToken;
    }

}
