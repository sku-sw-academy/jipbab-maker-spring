package sku.splim.jipbapmaker.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.domain.Log;

import java.sql.Timestamp;

@Setter
@Getter
public class LogDTO {
    private Long id;
    private AdminDTO adminDTO;
    private String content;
    private Timestamp createDate;
    private Timestamp modifyDate;

    public LogDTO(){

    }

    public LogDTO(Long id, AdminDTO adminDTO, String content) {
        this.id = id;
        this.adminDTO = adminDTO;
        this.content = content;
    }

    public LogDTO convertToDTO(Log log) {
        LogDTO logDTO = new LogDTO();
        logDTO.setId(log.getId());
        logDTO.setContent(log.getContent());
        logDTO.setModifyDate(log.getModifyDate());
        AdminDTO adminDTO1 = new AdminDTO();
        logDTO.setAdminDTO(adminDTO1.convertToDTO(log.getAdmin()));
        return logDTO;
    }
}
