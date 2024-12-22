package spring.boot.webcococo.services;

import org.springframework.web.multipart.MultipartFile;
import spring.boot.webcococo.entities.ProductPackageImages;

import java.io.IOException;
import java.util.List;

public interface IProductPackageImagesService {

    List<ProductPackageImages> createPackageImages(     Integer packageId,
                                                       List<MultipartFile> files  ) throws IOException;
}
