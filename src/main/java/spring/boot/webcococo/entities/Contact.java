package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact")
@Entity
public class Contact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "full_name")
    private String fullName;


    @Column(name = "email")
    private String email;


    @Column(name = "phone_number")
    private String phoneNumber;


    @Column(name = "message")
    private String message;


    @Column(name = "locale")
    private String locale;


    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng chuỗi trong CSDL
    @Column(name = "status")
    private Contact.Status status; // Trạng thái chi tiết đơn hàng

    // Enum định nghĩa các trạng thái
    public enum Status {
        PENDING,        // Đang chờ xử lý
        COMPLETED,      // Đã hoàn thành: Đơn hàng đã xử lý xong và giao dịch hoàn tất.
    }

}
