package sku.splim.jipbapmaker.dto;

import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.FAQ;
import sku.splim.jipbapmaker.domain.Notice;

import java.sql.Timestamp;

@Getter
@Setter
public class NoticeDTO {
    private Long id;
    private AdminDTO adminDTO;
    private String title;
    private String content;
    private Timestamp createDate;
    private Timestamp modifyDate;

    public NoticeDTO(){}

    public NoticeDTO(Long id, AdminDTO adminDTO, String title, String content){
        this.id = id;
        this.adminDTO = adminDTO;
        this.title = title;
        this.content = content;
    }

    public NoticeDTO convertToDTO(Notice notice) {
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setId(notice.getId());
        noticeDTO.setTitle(notice.getTitle());
        noticeDTO.setContent(notice.getContent());
        noticeDTO.setModifyDate(notice.getModifyDate());
        AdminDTO adminDTO1 = new AdminDTO();
        noticeDTO.setAdminDTO(adminDTO1.convertToDTO(notice.getAdmin()));
        return noticeDTO;
    }
}
