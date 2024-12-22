package spring.boot.webcococo.repositories;

import spring.boot.webcococo.entities.PackagesFeature;

import java.util.List;
import java.util.Optional;

public interface PackagesFeatureRepository  extends BaseRepository<PackagesFeature, Integer>{

    Optional<List<PackagesFeature>> getAllByPackageId(Integer id);
}
