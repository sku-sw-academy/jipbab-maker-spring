package sku.splim.jipbapmaker.dto;

import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.domain.Preference;
import sku.splim.jipbapmaker.domain.User;


@Getter
@Setter
public class PreferenceDTO {
    private Long id;
    private ItemDTO item;
    private UserDTO user;
    private int prefer;

    public PreferenceDTO() {}

    public PreferenceDTO(long id, ItemDTO item, UserDTO user, int prefer) {
        this.id = id;
        this.item = item;
        this.user = user;
        this.prefer = prefer;
    }

    public static PreferenceDTO convertToDTO(Preference preference) {
        PreferenceDTO preferenceDTO = new PreferenceDTO();
        preferenceDTO.setId(preference.getId());

        ItemDTO itemDTO = new ItemDTO();
        UserDTO userDTO = new UserDTO();

        preferenceDTO.setItem(itemDTO.convertToDTO(preference.getItem()));
        preferenceDTO.setUser(userDTO.convertToDTO(preference.getUser()));
        preferenceDTO.setPrefer(preference.getPrefer());
        return preferenceDTO;
    }

}
