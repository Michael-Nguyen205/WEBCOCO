package spring.boot.webcococo.entities;


import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity{



    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
