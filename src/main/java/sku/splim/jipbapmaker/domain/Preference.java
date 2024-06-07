package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.dto.PreferenceDTO;
import sku.splim.jipbapmaker.dto.UserDTO;

import java.sql.Timestamp;

@Table(name = "Preference")
@Getter
@Setter
@Entity
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", referencedColumnName = "item_code")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "prefer")
    private int prefer;

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

    public static Preference convertToEntity(PreferenceDTO dto){
        Preference preference = new Preference();
        Item item = new Item();
        User user = new User();
        preference.setId(dto.getId());
        preference.setItem(item.convertToEntity(dto.getItem()));
        preference.setUser(user.convertToEntity(dto.getUser()));
        preference.setPrefer(dto.getPrefer());
        return preference;
    }

}
