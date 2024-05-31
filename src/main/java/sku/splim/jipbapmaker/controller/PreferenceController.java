package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Preference;
import sku.splim.jipbapmaker.service.PreferenceService;
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

    @GetMapping("/list")
    public ResponseEntity<List<Preference>> listPreferences() {
        List<Preference> preferences = preferenceService.listPreferences();
        return ResponseEntity.ok(preferences);
    }
}
