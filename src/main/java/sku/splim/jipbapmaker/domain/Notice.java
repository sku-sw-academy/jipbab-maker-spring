package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Not;
import sku.splim.jipbapmaker.dto.FAQDTO;
import sku.splim.jipbapmaker.dto.NoticeDTO;

import java.sql.Timestamp;

@Table(name = "notice")
@Getter
@Setter
@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    public Notice(){

    }

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createDate = now;
        modifyDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        modifyDate = new Timestamp(System.currentTimeMillis());
    }

    public Notice convertToEntity(NoticeDTO noticeDTO) {
        Notice notice = new Notice();
        notice.setId(noticeDTO.getId());
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setModifyDate(noticeDTO.getModifyDate());
        Admin admin1 = new Admin();
        notice.setAdmin(admin1.convertToEntity(noticeDTO.getAdminDTO()));
        return notice;
    }
}
