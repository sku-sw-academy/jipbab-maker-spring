package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sku.splim.jipbapmaker.domain.Log;
import sku.splim.jipbapmaker.domain.Notice;
import sku.splim.jipbapmaker.dto.AdminDTO;
import sku.splim.jipbapmaker.dto.LogDTO;
import sku.splim.jipbapmaker.dto.NoticeDTO;
import sku.splim.jipbapmaker.service.LogService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    LogService logService;

    @GetMapping("/all")
    public List<LogDTO> getNotices(){
        List<Log> logs = logService.getLogs();
        List<LogDTO> dtos = new ArrayList<>();

        for(Log log : logs){
            LogDTO dto = new LogDTO();
            dto.setId(log.getId());
            dto.setContent(log.getContent());
            dto.setModifyDate(log.getModifyDate());
            AdminDTO adminDTO = new AdminDTO();
            dto.setAdminDTO(adminDTO.convertToDTO(log.getAdmin()));
            dtos.add(dto);
        }

        return dtos;
    }
}
