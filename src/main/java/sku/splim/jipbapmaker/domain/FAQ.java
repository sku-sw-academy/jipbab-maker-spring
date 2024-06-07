package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.dto.FAQDTO;
import sku.splim.jipbapmaker.dto.QuestionDTO;

import java.sql.Timestamp;

@Table(name = "faq")
@Getter
@Setter
@Entity
public class FAQ {
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

    public FAQ(){

    }

    public FAQ convertToEntity(FAQDTO faqdto) {
        FAQ faq = new FAQ();
        faq.setId(faqdto.getId());
        faq.setTitle(faqdto.getTitle());
        faq.setContent(faqdto.getContent());
        faq.setModifyDate(faqdto.getModifyDate());
        Admin admin1 = new Admin();
        faq.setAdmin(admin1.convertToEntity(faqdto.getAdminDTO()));
        return faq;
    }
}
