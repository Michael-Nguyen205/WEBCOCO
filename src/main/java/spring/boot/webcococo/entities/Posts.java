package spring.boot.webcococo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Posts {

    @Id
    private Integer id;

    @Column(name = "users_id")
    private Integer usersId;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "user_id")
    private Integer userId;

    // Getters and Setters
}
