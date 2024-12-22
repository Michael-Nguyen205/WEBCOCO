package spring.boot.webcococo.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.boot.webcococo.entities.ProductPackageImages;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.models.response.ApiResponse;
import spring.boot.webcococo.repositories.ProductPackageImagesRepository;
import spring.boot.webcococo.services.impl.ProductPackageImagesServiceImpl;
import spring.boot.webcococo.utils.LocalizationUtils;
import spring.boot.webcococo.utils.MessageKeys;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImagesController {

    private final LocalizationUtils localizationUtils;
    private final ProductPackageImagesServiceImpl productPackageImagesService;
    private final ProductPackageImagesRepository productPackageImagesRepository;
    @PostMapping(value = "uploads/package/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //POST http://localhost:8088/v1/api/products
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Integer packageId,
            @ModelAttribute("files") List<MultipartFile> files
    ) {
        try {
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductPackageImages.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body(localizationUtils
                        .getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_MAX_5));
            }
            List<ProductPackageImages> packageImages = productPackageImagesService.createPackageImages(packageId, files );

            return ResponseEntity.ok().body(packageImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/images/" + imageName);

            log.error("imagePath:{}",imagePath);

            UrlResource resource = new UrlResource(imagePath.toUri());

            log.error("resource:{}",resource.getFilename());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/images/notfound.jpeg").toUri()));
                //return ResponseEntity.notFound().build();

///Users/nguyenduykhanh/Documents/PROJECT/webcococo/webcococo/uploads/images/notfound.jpeg
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }







    @DeleteMapping("/{imageName}")
    public ResponseEntity<?> deleteImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/images/" + imageName);
            log.error("imagePath:{}", imagePath);
            if (Files.exists(imagePath)) {
                // Xóa tệp
                Files.delete(imagePath);
                ProductPackageImages packageImages = productPackageImagesRepository.findByImage(imageName ).orElseThrow(()->new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
                productPackageImagesService.deleteById(packageImages.getProductId());
                return ResponseEntity.ok(new ApiResponse(200, "Success", "xoa thanh ccoong", null));
            } else {
                return ResponseEntity.notFound().build(); // Nếu tệp không tồn tại, trả về 404 Not Found
            }


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }





        //
//    @PutMapping(value = "uploadsthumbnail/{idProduct}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> setThumbnailProduct(@PathVariable Long idProduct,
//                                                 @RequestPart("file") MultipartFile file) {
//        try {
//            // Lấy sản phẩm hiện tại
//            Product existingProduct = productService.getProductById(idProduct);
////            MultipartFile file = null;
//            // Kiểm tra kích thước tệp tin
//            if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
//                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
//                        .body(localizationUtils
//                                .getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_LARGE));
//            }
//
//            // Kiểm tra loại nội dung của tệp tin
//            String contentType = file.getContentType();
//            if (contentType == null || !contentType.startsWith("image/")) {
//                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//                        .body(localizationUtils
//                                .getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
//            }
//
//            // Lưu tệp tin và cập nhật ảnh đại diện của sản phẩm
//            String filename = storeFile(file);
//
//            Long idproduct = existingProduct.getId();
//
//            productService.setThumbnailProductt(filename, idproduct);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//





}
