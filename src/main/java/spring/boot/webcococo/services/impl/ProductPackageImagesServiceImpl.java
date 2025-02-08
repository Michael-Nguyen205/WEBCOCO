package spring.boot.webcococo.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.boot.webcococo.entities.Categories;
import spring.boot.webcococo.entities.Packages;
import spring.boot.webcococo.entities.ProductPackageImages;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.repositories.CategoryRepository;
import spring.boot.webcococo.repositories.PackagesRepository;
import spring.boot.webcococo.repositories.ProductPackageImagesRepository;
import spring.boot.webcococo.services.IProductPackageImagesService;
import spring.boot.webcococo.utils.FileStorageUtil;
import spring.boot.webcococo.utils.LocalizationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service

public class ProductPackageImagesServiceImpl extends BaseServiceImpl<ProductPackageImages, Integer, ProductPackageImagesRepository> implements IProductPackageImagesService {

    private final LocalizationUtils localizationUtils;
    private final PackagesRepository packagesRepository;

    private final CategoryRepository categoryRepository;

    private  final FileStorageUtil fileStorageUtil;

    private final ProductPackageImagesRepository productPackageImagesRepository;


    public ProductPackageImagesServiceImpl(ProductPackageImagesRepository repository, LocalizationUtils localizationUtils, PackagesRepository packagesRepository, CategoryRepository categoryRepository, FileStorageUtil fileStorageUtil, ProductPackageImagesRepository productPackageImagesRepository) {
        super(repository);
        this.localizationUtils = localizationUtils;
        this.packagesRepository = packagesRepository;
        this.categoryRepository = categoryRepository;
        this.fileStorageUtil = fileStorageUtil;
        this.productPackageImagesRepository = productPackageImagesRepository;
    }

    @Transactional
    protected boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }





    @Transactional
    @Override
    public List<ProductPackageImages> createPackageImages(Integer packageId, List<MultipartFile> files) throws IOException {
        try {


            Packages existingPackage = packagesRepository.findById(packageId).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));
            List<ProductPackageImages> packageImages = productPackageImagesRepository.findAllById(existingPackage.getId()).orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND));


            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                    throw new AppException(ErrorCodeEnum.UPLOAD_IMAGES_FILE_LARGE);
                }


                if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                    throw new AppException(ErrorCodeEnum.UPLOAD_IMAGES_FILE_LARGE);
                }

                // Lưu file và cập nhật thumbnail trong DTO
                String filename = fileStorageUtil.storeFile(file); // Thay thế hàm này với code của bạn để lưu file
                //lưu vào đối tượng product trong DB
                if (filename == null || filename.isEmpty()) {
                    throw new AppException(ErrorCodeEnum.FILE_UPLOAD_FAILED);
                }

                ProductPackageImages productPackageImage = new ProductPackageImages();
                productPackageImage.setPackageId(existingPackage.getId());
                productPackageImage.setImage(filename);

                packageImages.add(productPackageImage);
            }

            if (packageImages.size() > ProductPackageImages.MAXIMUM_IMAGES_PER_PRODUCT) {
                throw new AppException(ErrorCodeEnum.UPLOAD_IMAGES_FILE_LARGE);
            }

            saveAll(packageImages);

            return packageImages;
        } catch (Exception e) {
            throw e;
        }
    }






}
