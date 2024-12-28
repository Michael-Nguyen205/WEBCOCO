package spring.boot.webcococo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity // Đánh dấu class này là một Entity trong JPA
@Table(name = "i18n_language") // Tên bảng trong cơ sở dữ liệu
public class I18Language {

    @Id // Đánh dấu khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng giá trị khóa chính
    private Integer id;

    @Column(nullable = false, unique = true, length = 10) // Mã ngôn ngữ (ví dụ: "en", "vi")
    private String code;

    @Column( unique = true, length = 20) // Locale đầy đủ (ví dụ: "en-US")
    private String locale;

    @Column(length = 20) // Tên ngôn ngữ (ví dụ: "English")
    private String name;

    @Column(name = "native_name", length = 100) // Tên ngôn ngữ gốc (ví dụ: "Tiếng Việt")
    private String nativeName;

    @Column(name = "is_active") // Ngôn ngữ có hoạt động không
    private Boolean isActive ;
}