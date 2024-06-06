package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.Log;
import sku.splim.jipbapmaker.repository.LogRepository;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public void login(Admin admin){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("로그인");
        logRepository.save(log);
    }

    public void logOut(Admin admin){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("로그아웃");
        logRepository.save(log);
    }

    public void FAQ(Admin admin){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("FAQ 작성");
        logRepository.save(log);
    }

    public void notice(Admin admin){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("공지사항 작성");
        logRepository.save(log);
    }

    public void QnA(Admin admin){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("유저에게 답변 작성");
        logRepository.save(log);
    }
}
