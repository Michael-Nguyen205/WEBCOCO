package spring.boot.webcococo.redis.RedisProducts;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.PageRequest;
import spring.boot.webcococo.models.response.PackagesResponse;

import java.util.List;
import java.util.Set;

public interface IPackageRedis {
    //Clear cached data in Redis
    void clearRedisCache();//clear cache
    Set<PackagesResponse> getPackagesFromRedis(
            String keyword,
            Integer categoryId) throws JsonProcessingException;
    void savePackagesToRedis(Set<PackagesResponse> productResponses,
                                String keyword,
                             Integer categoryId
                                ) throws JsonProcessingException;

}
