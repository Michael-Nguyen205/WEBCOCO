package spring.boot.webcococo.listener;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import spring.boot.webcococo.entities.Packages;
import spring.boot.webcococo.redis.RedisProducts.impl.PackageRedisImpl;


@Component
@RequiredArgsConstructor

public class PackageListener {

    private final PackageRedisImpl packageRedis;
    private static final Logger logger = LoggerFactory.getLogger(PackageListener.class);
    @PrePersist
    public void prePersist(Packages packages) {
        logger.info("prePersist");
    }

    @PostPersist //save = persis
    public void postPersist(Packages packages) {
        // Update Redis cache
        logger.info("postPersist");
        packageRedis.clearRedisCache();
    }



    @PreUpdate
    public void preUpdate(Packages packages) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preUpdate");
        packageRedis.clearRedisCache();

    }

    @PostUpdate
    public void postUpdate(Packages packages) {
        // Update Redis cache
        logger.info("postUpdate");
        packageRedis.clearRedisCache();
    }

    @PreRemove
    public void preRemove(Packages packages) {
        //ApplicationEventPublisher.instance().publishEvent(event);
        logger.info("preRemove");
        packageRedis.clearRedisCache();

    }

    @PostRemove
    public void postRemove(Packages packages) {
        // Update Redis cache
        logger.info("postRemove");
        packageRedis.clearRedisCache();
    }
}
