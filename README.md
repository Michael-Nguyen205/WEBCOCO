# Project Name

**Mô tả**: Đây là một project mẫu được xây 
dựng để [mô tả mục đích của project, ví dụ: phát 
triển một hệ thống quản lý, ứng dụng web, v.v.]. 
Project sử dụng công nghệ [list các công nghệ chính 
như Spring Boot, React, Node.js, v.v.] để xây dựng 
các tính năng chính và cung cấp một nền tảng dễ dàng 
mở rộng.

## Cài Đặt
Để chạy project trên máy cá nhân của bạn, hãy làm theo các bước sau:


## Thiết lập quyền tài khoản admin
Mặc định khi ứng dụng được tạo, ta sẽ có sẵn đủ các ACTION và 1 tài khoản user Có PERMISSION là ADMIN
tài khoản này nắm quyền cao nhất và có thể tạo và thiết lập các quyền nhỏ hơn
để thiết lập quyền (MODEL-ACTION-RAW) tkADMIN phải tạo model trước hoặc không tạo sao đó mới tạo quyền 
vd: tạo permission và pamr.




## Nghiệp vụ order sản phẩm
Khi khách hàng order gói dịch vụ và sản phẩm theo paymentTerm thì gửi request
/order đến server .sever dựa vào các id của gói dịch vụ để lấy giá 
và giá cọc nếu có từ đó ta tạo order và đợi cho bên admin duyệt
.khi admin duyệt order xong thì sẽ gửi email kèm link
thanh toán kèm thông tin cổng thanh toán tương ứng và
thông tin order trong url thanh toán
về mail khách hàng . khi khách hàng click vào mail sẽ hiện
trang thanh toán tương ứng để khách hàng thanh toán , khi 
thánh toán xong trang cổng thanh toán đó link người dùng 
đến 1 trang khác hoặc call 1 api dựa trên cấu hình urlCallBack
của hệ thống đến update thông tin transaction




### 1. Clone repository
Đầu tiên, clone repository này về máy của bạn:
```bash
git clone https://github.com/username/project-name.git
