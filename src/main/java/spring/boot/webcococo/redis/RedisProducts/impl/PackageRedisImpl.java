package spring.boot.webcococo.redis.RedisProducts.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import spring.boot.webcococo.models.response.PackagesResponse;
import spring.boot.webcococo.redis.RedisProducts.IPackageRedis;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class PackageRedisImpl implements IPackageRedis {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;
    @Value("${spring.data.redis.use-redis-cache}")
    private boolean useRedisCache;

    private String getKeyFrom(String keyword,
                              Integer categoryId
                              ) {


        String key = String.format("all_products:%s:%d",
                keyword, categoryId);
        return key;
    }



    @Override
    public Set<PackagesResponse> getPackagesFromRedis(String keyword, Integer categoryId) throws JsonProcessingException {
        if (!useRedisCache) {
            return Collections.emptySet();  // Trả về một Set rỗng thay vì null nếu không dùng Redis
        }

        String key = this.getKeyFrom(keyword, categoryId);
        String json = (String) redisTemplate.opsForValue().get(key);

        System.out.println("Redis key: " + key);
        System.out.println("JSON from Redis: " + json);

        // Kiểm tra nếu không có dữ liệu từ Redis hoặc dữ liệu không hợp lệ
        if (json == null || json.trim().isEmpty() || json.equals("[]")) {
            return Collections.emptySet();  // Trả về Set rỗng thay vì null
        }

        // Dùng Set để tránh trùng lặp khi deserialize
        Set<PackagesResponse> packagesResponses = new HashSet<>();
        try {
            packagesResponses = new HashSet<>(redisObjectMapper.readValue(json, new TypeReference<List<PackagesResponse>>() {}));
        } catch (Exception e) {
            e.printStackTrace();
            // Ghi log hoặc xử lý lỗi thích hợp
            // Có thể thêm một thông báo log hoặc throw một Exception có ý nghĩa
        }
        return packagesResponses;
    }



    //    @Override
//    public void clear(){
//        redisTemplate.getConnectionFactory().getConnection().flushAll();
//    }
    @Override
    @Scheduled(fixedRate = 10800000)
    public void clearRedisCache() {
        // Đếm số lượng khóa trước khi xóa
        Set<String> keysBefore = redisTemplate.keys("*");
        int keysCountBefore = (keysBefore != null) ? keysBefore.size() : 0;
        log.error("Number of keys before clear: {}", keysCountBefore);

        // Xóa tất cả dữ liệu trong Redis
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        redisTemplate.getConnectionFactory().getConnection().flushDb();

        // Đếm số lượng khóa sau khi xóa
        Set<String> keysAfter = redisTemplate.keys("*");
        int keysCountAfter = (keysAfter != null) ? keysAfter.size() : 0;
        log.info("Number of keys after clear: {}", keysCountAfter);

        // Kiểm tra nếu tất cả các khóa đã được xóa
        if (keysCountAfter == 0) {
            log.error("All keys have been successfully cleared from Redis.");
        } else {
            log.error("Some keys are still present in Redis after clear.");
        }
    }



    @Override
    //save to Redis
    public void savePackagesToRedis(Set<PackagesResponse> productResponses,
                                String keyword,
                                Integer categoryId
                                ) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId);
        String json = redisObjectMapper.writeValueAsString(productResponses);
        redisTemplate.opsForValue().set(key, json);
    }
}
