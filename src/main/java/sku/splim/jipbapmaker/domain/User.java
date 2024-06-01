package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sku.splim.jipbapmaker.dto.PriceDTO;
import sku.splim.jipbapmaker.dto.UserDTO;

import java.util.Collection;
import java.util.List;

@Table(name = "User")
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(length = 255)
    private String profile;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean push;

    @Column(nullable = false)
    private boolean log;

    @Column(columnDefinition = "TEXT")
    private String fcmToken;

    @Builder
    public User(Long id, String email, String password, String nickname, String profile, boolean enabled, boolean push, boolean log, String fcmToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
        this.enabled = enabled;
        this.push = push;
        this.log = log;
        this.fcmToken = fcmToken;
    }

    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override // 사용자 id 반환
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .nickname(userDTO.getNickname())
                .profile(userDTO.getProfile())
                .enabled(userDTO.isEnabled())
                .push(userDTO.isPush())
                .log(userDTO.isLog())
                .fcmToken(userDTO.getFcmToken())
                .build();
    }
}
