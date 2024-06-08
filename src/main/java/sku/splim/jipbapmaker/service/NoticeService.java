package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.Notice;
import sku.splim.jipbapmaker.repository.NoticeRepository;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private LogService logService;

    public void save(Notice notice) {
        noticeRepository.save(notice);
        logService.notice(notice.getAdmin(), notice);
    }

    public void update(Notice notice) {
        noticeRepository.save(notice);
        logService.updateNotice(notice.getAdmin(), notice);
    }

    public List<Notice> getNotices() {
        return noticeRepository.findAllByOrderByModifyDateDesc();
    }

    public Notice findById(long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteNotice(long id, Admin admin) {
        Notice notice = noticeRepository.findById(id).orElse(null); // 해당 ID에 해당하는 FAQ를 조회
        if (notice != null) {
            noticeRepository.deleteById(id); // FAQ 삭제
            logService.deleteNotice(admin, notice); // 삭제된 FAQ에 대한 로그 기록
        } else {
            throw new IllegalArgumentException("FAQ not found with id: " + id);
        }
    }
}
