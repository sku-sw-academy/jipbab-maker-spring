package sku.splim.jipbapmaker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.Notice;
import sku.splim.jipbapmaker.dto.AdminDTO;
import sku.splim.jipbapmaker.dto.NoticeDTO;
import sku.splim.jipbapmaker.service.AdminService;
import sku.splim.jipbapmaker.service.NoticeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private AdminService adminService;
    private NoticeService noticeService;

    public NoticeController(NoticeService noticeService, AdminService adminService) {
        this.noticeService = noticeService;
        this.adminService = adminService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam("id") long id, @RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Admin admin = adminService.findById(id);
            if (admin == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }

            Notice notice = new Notice();
            notice.setTitle(title);
            notice.setContent(content);
            notice.setAdmin(admin);
            noticeService.save(notice);

            return ResponseEntity.ok("Ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestParam("id") long id, @RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Notice notice = noticeService.findById(id);
            if (notice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FAQ not found");
            }
            // 제목과 내용 업데이트
            notice.setTitle(title);
            notice.setContent(content);

            // FAQ 업데이트
            noticeService.update(notice);
            return ResponseEntity.ok("FAQ updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public List<NoticeDTO> getNotices(){
        List<Notice> notices = noticeService.getNotices();
        List<NoticeDTO> dtos = new ArrayList<>();

        for(Notice notice : notices){
            NoticeDTO dto = new NoticeDTO();
            dto.setId(notice.getId());
            dto.setTitle(notice.getTitle());
            dto.setContent(notice.getContent());
            dto.setModifyDate(notice.getModifyDate());
            AdminDTO adminDTO = new AdminDTO();
            dto.setAdminDTO(adminDTO.convertToDTO(notice.getAdmin()));
            dtos.add(dto);
        }

        return dtos;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFAQ(@RequestBody Map<String, Long> requestBody) {
        try {
            long adminId = requestBody.get("adminId");
            long noticeId = requestBody.get("noticeId");
            Admin admin = adminService.findById(adminId);

            noticeService.deleteNotice(noticeId, admin);
            return ResponseEntity.ok("FAQ deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
