package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sku.splim.jipbapmaker.dto.LogDTO;

import java.sql.Timestamp;

@Table(name = "Log")
@Getter
@Setter
@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    public Log(){

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

    public Log convertToEntity(LogDTO logDTO) {
        Log log = new Log();
        log.setId(logDTO.getId());
        log.setContent(logDTO.getContent());
        log.setModifyDate(logDTO.getModifyDate());
        Admin admin = new Admin();
        log.setAdmin(admin.convertToEntity(logDTO.getAdminDTO()));

        return log;
    }
}
