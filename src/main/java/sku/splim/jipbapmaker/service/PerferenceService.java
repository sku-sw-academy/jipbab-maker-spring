package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.repository.PreferenceRepository;

@Service
public class PerferenceService {
    @Autowired
    private PreferenceRepository perferenceRepository;
}
