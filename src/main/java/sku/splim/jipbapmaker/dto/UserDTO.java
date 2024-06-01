package sku.splim.jipbapmaker.dto;

import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.User;
import lombok.Builder;


@Getter
@Setter
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profile;
    private boolean enabled;
    private boolean push;
    private boolean log;
    private String fcmToken;

    public UserDTO() {
    }

    @Builder
    public UserDTO(Long id, String email, String password, String nickname, String profile, boolean enabled, boolean push, boolean log, String fcmToken) {
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

    public static UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .enabled(user.isEnabled())
                .push(user.isPush())
                .log(user.isLog())
                .fcmToken(user.getFcmToken())
                .build();
    }
}




