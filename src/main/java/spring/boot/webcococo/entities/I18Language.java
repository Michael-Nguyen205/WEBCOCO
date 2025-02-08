package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity // Đánh dấu class này là một Entity trong JPA
@Table(name = "i18n_language") // Tên bảng trong cơ sở dữ liệu
public class I18Language {

    @Id // Đánh dấu khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng giá trị khóa chính
    private Integer id;
    @NotBlank
    @Column(nullable = false, unique = true, length = 10) // Mã ngôn ngữ (ví dụ: "en", "vi")
    private String code;

    @Column( unique = true, length = 20) // Locale đầy đủ (ví dụ: "en-US")
    private String locale;

    @Column(length = 20) // Tên ngôn ngữ (ví dụ: "English")
    private String name;

    @Column(name = "native_name", length = 100) // Tên ngôn ngữ gốc (ví dụ: "Tiếng Việt")
    private String nativeName;


    @Column(name = "is_default")
    private Boolean isDefault = false; // Mặc định là false

    @Column(name = "is_active")
    private Boolean isActive = true; // Mặc định là true
}