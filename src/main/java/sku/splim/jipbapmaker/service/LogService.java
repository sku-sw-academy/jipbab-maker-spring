package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.FAQ;
import sku.splim.jipbapmaker.domain.Log;
import sku.splim.jipbapmaker.domain.Notice;
import sku.splim.jipbapmaker.repository.LogRepository;
import java.util.List;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public List<Log> getLogs() {
        return logRepository.findAllByOrderByModifyDateDesc();
    }

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

    public void FAQ(Admin admin, FAQ faq){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("자주하는 질문 '" + faq.getTitle() + "' 작성");
        logRepository.save(log);
    }

    public void updateFAQ(Admin admin, FAQ faq){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("자주하는 질문 '" + faq.getTitle() + "' 수정");
        logRepository.save(log);
    }

    public void deleteFAQ(Admin admin, FAQ faq){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("자주하는 질문 '" + faq.getTitle() + "' 삭제");
        logRepository.save(log);
    }

    public void notice(Admin admin, Notice notice){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("공지사항 '" +  notice.getTitle()+ "' 작성");
        logRepository.save(log);
    }

    public void updateNotice(Admin admin, Notice notice){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("공지사항 '"+ notice.getTitle() +"' 수정");
        logRepository.save(log);
    }

    public void deleteNotice(Admin admin, Notice notice){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("공지사항 '"+ notice.getTitle() +"' 삭제");
        logRepository.save(log);
    }

    public void QnA(Admin admin){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("유저에게 답변 작성");
        logRepository.save(log);
    }
}
