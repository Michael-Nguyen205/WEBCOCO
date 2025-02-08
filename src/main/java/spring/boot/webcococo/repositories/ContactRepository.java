package spring.boot.webcococo.repositories;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.boot.webcococo.entities.BankGatewayDetail;
import spring.boot.webcococo.entities.Contact;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends BaseRepository<Contact, Integer> {
//    Optional<List<BankGatewayDetail>> findAllByPaymentMethodId(Integer integer);




//
//    @NotBlank
//    @Column(name = "full_name")
//    private String fullName;
//
//
//    @Column(name = "email")
//    private String email;
//
//
//    @Column(name = "phone_number")
//    private String phoneNumber;
//
//
//    @Column(name = "message")
//    private String message;
//
//
//    @Column(name = "locale")
//    private String locale;
//
//
//    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng chuỗi trong CSDL
//    @Column(name = "status")
//    private Contact.Status status; // Trạng thái chi tiết đơn hàng
//
//    // Enum định nghĩa các trạng thái
//    public enum Status {
//        PENDING,        // Đang chờ xử lý
//        COMPLETED,      // Đã hoàn thành: Đơn hàng đã xử lý xong và giao dịch hoàn tất.
//    }
//




    @Query(value = "SELECT * " +
            "FROM contact c " +
            "WHERE " +
           " (:keyword = 'ALL' OR " +
            "   p.full_name LIKE CONCAT('%', :keyword, '%')  " +
            "  OR p.email LIKE CONCAT('%', :keyword, '%')" +
            "  OR p.phone_number LIKE CONCAT('%', :keyword, '%')" +
            "  OR p.message LIKE CONCAT('%', :keyword, '%')" +
            "AND (:status = 'ALL' OR p.status = :status) " +
            "AND (:locale = 'ALL' OR p.locale = :locale) " +
            ")", nativeQuery = true)
    Page<Contact> searchContacts(@Param("locale") String locale,
                                 @Param("status") String status,
                                 @Param("keyword") String keyword,
                                  Pageable pageable);


}
