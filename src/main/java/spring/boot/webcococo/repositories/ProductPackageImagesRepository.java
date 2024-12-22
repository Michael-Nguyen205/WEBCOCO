package spring.boot.webcococo.repositories;

import org.springframework.stereotype.Repository;
import spring.boot.webcococo.entities.ProductPackageImages;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductPackageImagesRepository extends BaseRepository<ProductPackageImages,Integer> {


    Optional<List<ProductPackageImages>> findAllById(Integer id);


    Optional<ProductPackageImages> findByImage (String imageName);

}
