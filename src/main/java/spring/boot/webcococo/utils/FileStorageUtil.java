package spring.boot.webcococo.utils;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileStorageUtil {

    private final Path rootLocation;

    public FileStorageUtil() {
        // Đường dẫn gốc để lưu file, có thể lấy từ cấu hình ứng dụng
        this.rootLocation = Paths.get("uploads");
        createDirectories(rootLocation);
    }

    /**
     * Tạo thư mục nếu chưa tồn tại
     */
    private void createDirectories(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory: " + path, e);
        }
    }

    /**
     * Kiểm tra file có phải là ảnh không
     */

    @Transactional
    protected boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @Transactional
    public String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        //Lấy tên file gốc và đảm bảo tính an toàn bằng StringUtils.cleanPath loại bỏ các kí tự đặc biệt
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file

//        /Users/nguyenduykhanh/Documents/BE/Bản sao AuthenAuthor 2/AuthenAuthor/uploads

        Path uploadDir = Paths.get("uploads/images");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        //  InputStream là luồng dữ liệu đầu vào của tệp, chứa nội dung mà bạn muốn sao chép

        try{
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        }catch(Exception e){
            e.printStackTrace();
            throw new AppException(ErrorCodeEnum.FILE_UPLOAD_FAILED);
        }
        return uniqueFilename;
    }

    /**
     * Xóa file
     */
    public void deleteFile(String filename, String subFolder) throws IOException {
        Path filePath = this.rootLocation.resolve(subFolder != null ? subFolder : "").resolve(filename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        } else {
            throw new IOException("File not found: " + filename);
        }
    }

    /**
     * Trả về đường dẫn đầy đủ của file
     */
    public Path getFilePath(String filename, String subFolder) {
        return this.rootLocation.resolve(subFolder != null ? subFolder : "").resolve(filename);
    }
}
