package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_gateway")
@Entity
public class BankGateWay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private Boolean idActive;



    // Getters and Setters
}
