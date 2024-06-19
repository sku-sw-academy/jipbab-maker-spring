package sku.splim.jipbapmaker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sku.splim.jipbapmaker.domain.Addition;
import sku.splim.jipbapmaker.repository.AddRepository;
import java.util.List;

@Service
public class AddService {
    private final AddRepository addRepository;

    public AddService(AddRepository addRepository) {
        this.addRepository = addRepository;
    }

    public void save(Addition add) {
        addRepository.save(add);
    }

    public List<Addition> findAllByUserIdOrderByModifyDate(Long userId) {
        return addRepository.findAllByUserIdOrderByModifyDate(userId);
    }

    public Addition findByUserIdAndRecipeId(Long userId, Long recipeId){
        return addRepository.findByUserIdAndRecipeId(userId, recipeId);
    }

    @Transactional
    public void deleteAdd(long id) {
        Addition addition = addRepository.findById(id);
        addRepository.delete(addition);
    }

    public List<Addition> findAll() {
        return addRepository.findAll();
    }
}
