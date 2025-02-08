package spring.boot.webcococo.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "models" )
@Entity
public class Models extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

//    @NotBlank(message = "{description.not.blank}")
//   @NotBlank(message = "description khong được thiếu")
    @Column(name = "description")
    private String description;



}
