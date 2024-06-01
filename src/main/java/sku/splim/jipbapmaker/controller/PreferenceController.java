package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Preference;
import sku.splim.jipbapmaker.dto.ItemDTO;
import sku.splim.jipbapmaker.dto.PreferenceDTO;
import sku.splim.jipbapmaker.dto.UserDTO;
import sku.splim.jipbapmaker.service.PreferenceService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/prefer")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @PostMapping("/save")
    public ResponseEntity<String> savePreference(@RequestParam("to") String email) {
        try {
            preferenceService.savePreference(email);
            return ResponseEntity.ok("Preferences saved successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<PreferenceDTO>> getListPreferences(@PathVariable("id") Long id) {
        List<PreferenceDTO> preferences = new ArrayList<>();
        List<Preference> preferenceList = preferenceService.getListPreferences(id);

        for(Preference preference : preferenceList) {
            PreferenceDTO preferenceDTO = new PreferenceDTO();
            UserDTO userDTO = new UserDTO();
            ItemDTO itemDTO = new ItemDTO();

            preferenceDTO.setId(preference.getId());
            preferenceDTO.setPrefer(preference.getPrefer());
            preferenceDTO.setItem(itemDTO.convertToDTO(preference.getItem()));
            preferenceDTO.setUser(userDTO.convertToDTO(preference.getUser()));
            preferences.add(preferenceDTO);
        }

        return ResponseEntity.ok(preferences);
    }

    @PostMapping("/update")
    public ResponseEntity<PreferenceDTO> getUpdate(@RequestBody PreferenceDTO dto) {
        PreferenceDTO preferenceDTO = new PreferenceDTO();
        Preference preference = preferenceService.updatePreference(dto);
        ItemDTO itemDTO = new ItemDTO();
        UserDTO userDTO = new UserDTO();
        preferenceDTO.setId(preference.getId());
        preferenceDTO.setItem(itemDTO.convertToDTO(preference.getItem()));
        preferenceDTO.setUser(userDTO.convertToDTO(preference.getUser()));
        preferenceDTO.setPrefer(preference.getPrefer());

        return ResponseEntity.ok(preferenceDTO);
    }
}
