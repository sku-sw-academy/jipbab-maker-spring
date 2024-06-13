package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.domain.Preference;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.PreferenceDTO;
import sku.splim.jipbapmaker.repository.PreferenceRepository;
import sku.splim.jipbapmaker.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PreferenceService {
    @Autowired
    private PreferenceRepository perferenceRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;

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

    public List<Preference> getListPreferences(long id) {
        return perferenceRepository.findByUserId(id);
    }

    public Preference updatePreference(PreferenceDTO dto) {
        // DTO로부터 엔티티를 가져옵니다.
        Optional<Preference> optionalPreference = preferenceRepository.findById(dto.getId());
        // 엔티티가 존재하는지 확인합니다.
        if (optionalPreference.isPresent()) {
            Preference preference = optionalPreference.get();
            preference.setPrefer(dto.getPrefer());
            // 변경된 엔티티를 저장소에 저장하고 반환합니다.
            return preferenceRepository.save(preference);
        } else {
            // 엔티티가 존재하지 않는 경우에는 예외 처리를 합니다.
            throw new IllegalArgumentException("Preference with ID " + dto.getId() + " not found");
        }
    }

    public List<Preference> getPreferList(long id, int prefer){
        List<Preference> preferences = preferenceRepository.findByUserIdAndPrefer(id, prefer);
        return preferences;
    }

    public List<String> getPreferListToString(Long userId, int prefer) {
        List<Preference> preferences = preferenceRepository.findByUserIdAndPrefer(userId, prefer);
        return preferences.stream()
                .map(preference -> preference.getItem().getItemName()) // item의 이름을 가져온다고 가정
                .collect(Collectors.toList());
    }

    public Preference findByUserIdAndItem_ItemName(Long id, String itemName){
        return perferenceRepository.findByUserIdAndItem_ItemName(id, itemName);
    }

    public void save(Preference preference){
        preferenceRepository.save(preference);
    }
}
