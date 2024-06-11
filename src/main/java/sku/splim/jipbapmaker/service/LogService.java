package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.*;
import sku.splim.jipbapmaker.repository.LogRepository;
import java.util.List;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public void upload(Admin admin, Item item){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("'"+ item.getItemName() + "' 이미지 업로드");
        logRepository.save(log);
    }

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

    public void QnA(Answer answer){
        Log log = new Log();
        log.setAdmin(answer.getAdmin());
        log.setContent("'" + answer.getQuestion().getTitle() + "' 답변 작성");
        logRepository.save(log);
    }

    public void updateQnA(Answer answer){
        Log log = new Log();
        log.setAdmin(answer.getAdmin());
        log.setContent("'" + answer.getQuestion().getTitle() + "' 답변 수정");
        logRepository.save(log);
    }

    public void deleteQnA(Answer answer){
        Log log = new Log();
        log.setAdmin(answer.getAdmin());
        log.setContent("'" + answer.getQuestion().getTitle() + "' 답변 삭제");
        logRepository.save(log);
    }

    public void recipe(Admin admin, Recipe recipe){
        Log log = new Log();
        log.setAdmin(admin);
        log.setContent("'" + recipe.getTitle() + "' 공유 중단");
        logRepository.save(log);
    }

}
