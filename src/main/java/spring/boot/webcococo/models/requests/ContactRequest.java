package spring.boot.webcococo.models.requests;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.webcococo.entities.Contact;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {



    @NotBlank
    @Column(name = "full_name")
    private String fullName;


    @Column(name = "email")
    private String email;


    @Column(name = "phone_number")
    private String phoneNumber;


    @Column(name = "message")
    private String message;


}
