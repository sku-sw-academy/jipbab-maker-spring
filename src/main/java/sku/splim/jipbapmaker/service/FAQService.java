package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.FAQ;
import sku.splim.jipbapmaker.repository.FAQRepository;
import java.util.List;

@Service
public class FAQService {
    @Autowired
    private FAQRepository faqRepository;

    @Autowired
    private LogService logService;

    public void save(FAQ faq) {
        faqRepository.save(faq);
        logService.FAQ(faq.getAdmin(), faq);
    }

    public void update(FAQ faq) {
        faqRepository.save(faq);
        logService.updateFAQ(faq.getAdmin(), faq);
    }

    public List<FAQ> getFAQs() {
        return faqRepository.findAllByOrderByModifyDateDesc();
    }

    public FAQ findById(long id) {
        return faqRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteFAQ(long id, Admin admin) {
        FAQ faq = faqRepository.findById(id).orElse(null); // 해당 ID에 해당하는 FAQ를 조회
        if (faq != null) {
            faqRepository.deleteById(id); // FAQ 삭제
            logService.deleteFAQ(admin, faq); // 삭제된 FAQ에 대한 로그 기록
        } else {
            throw new IllegalArgumentException("FAQ not found with id: " + id);
        }
    }
}
