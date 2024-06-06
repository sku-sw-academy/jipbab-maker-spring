package sku.splim.jipbapmaker.dto;

import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.FAQ;
import sku.splim.jipbapmaker.domain.Question;

import java.sql.Timestamp;

@Getter
@Setter
public class FAQDTO {
    private Long id;
    private AdminDTO adminDTO;
    private String title;
    private String content;
    private Timestamp createDate;
    private Timestamp modifyDate;

    public FAQDTO(){}

    public FAQDTO(Long id, AdminDTO adminDTO, String title, String content){
        this.id = id;
        this.adminDTO = adminDTO;
        this.title = title;
        this.content = content;
    }

    public FAQDTO convertToDTO(FAQ faq) {
        FAQDTO faqdto = new FAQDTO();
        faqdto.setId(faq.getId());
        faqdto.setTitle(faq.getTitle());
        faqdto.setContent(faq.getContent());
        faqdto.setModifyDate(faq.getModifyDate());
        AdminDTO adminDTO1 = new AdminDTO();
        faqdto.setAdminDTO(adminDTO1.convertToDTO(faq.getAdmin()));
        return faqdto;
    }

}
