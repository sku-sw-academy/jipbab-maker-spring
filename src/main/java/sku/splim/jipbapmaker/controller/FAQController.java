package sku.splim.jipbapmaker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.FAQ;
import sku.splim.jipbapmaker.dto.AdminDTO;
import sku.splim.jipbapmaker.dto.FAQDTO;
import sku.splim.jipbapmaker.service.AdminService;
import sku.splim.jipbapmaker.service.FAQService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/faq")
public class FAQController {
    private final AdminService adminService;
    private FAQService faqService;


    public FAQController(FAQService faqService, AdminService adminService) {
        this.faqService = faqService;
        this.adminService = adminService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam("id") long id, @RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Admin admin = adminService.findById(id);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }

            FAQ faq = new FAQ();
            faq.setTitle(title);
            faq.setContent(content);
            faq.setAdmin(admin);
            faqService.save(faq);

            return ResponseEntity.ok("Ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestParam("id") long id, @RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            FAQ faq = faqService.findById(id);
            if (faq == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FAQ not found");
            }
            // 제목과 내용 업데이트
            faq.setTitle(title);
            faq.setContent(content);

            // FAQ 업데이트
            faqService.update(faq);
            return ResponseEntity.ok("FAQ updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<FAQDTO> getFAQs(){
        List<FAQ> faqs = faqService.getFAQs();
        List<FAQDTO> dtos = new ArrayList<>();

        for(FAQ faq : faqs){
            FAQDTO dto = new FAQDTO();
            dto.setId(faq.getId());
            dto.setTitle(faq.getTitle());
            dto.setContent(faq.getContent());
            dto.setModifyDate(faq.getModifyDate());
            AdminDTO adminDTO = new AdminDTO();
            dto.setAdminDTO(adminDTO.convertToDTO(faq.getAdmin()));
            dtos.add(dto);
        }

        return dtos;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFAQ(@RequestBody Map<String, Long> requestBody) {
        try {
            long adminId = requestBody.get("adminId");
            long faqId = requestBody.get("faqId");
            Admin admin = adminService.findById(adminId);

            faqService.deleteFAQ(faqId, admin);
            return ResponseEntity.ok("FAQ deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
