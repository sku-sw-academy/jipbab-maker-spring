package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.domain.Preference;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.repository.PreferenceRepository;
import sku.splim.jipbapmaker.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PreferenceService {
    @Autowired
    private PreferenceRepository perferenceRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    public void savePreference(String email) {
        List<Preference> prefers = new ArrayList<>();
        List<Item> items = itemService.getfindAll(); // itemService.getfindAll()에서 findAll()로 변경
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            for (Item item : items) {
                Preference pref = new Preference();
                pref.setItem(item);
                pref.setUser(user); // 사용자 설정
                pref.setPrefer(1);
                prefers.add(pref);
            }
            perferenceRepository.saveAll(prefers);
        } else {
            // 사용자 이메일이 없을 경우 처리
            throw new IllegalArgumentException("No user found with email: " + email);
        }
    }

    public List<Preference> listPreferences() {
        return perferenceRepository.findAll();
    }
}
