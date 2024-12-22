package spring.boot.webcococo.repositories.native_queries_impl;//package spring.boot.authenauthor.repositories.native_queries_impl;
//
//import org.reactivestreams.Publisher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.r2dbc.core.DatabaseClient;
//import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import spring.boot.authenauthor.entities.PermissionActionModelRaw;
//import spring.boot.authenauthor.repositories.PermissionActionRawRepository;
//
//import java.util.List;
//
//@Repository
//public class PermissionActionRawRepositoryImpl implements PermissionActionRawRepository {
//
//    @Autowired
//    private DatabaseClient databaseClient;
//
//    @Override
//    public Mono<Long> countActionModelRawAuthors(String userName, Integer rawId, String actionName, String modelName) {
//        return databaseClient
//                .sql("SELECT COUNT(pamr.id) " +
//                        "FROM permission_action_model_raw pamr " +
//                        "JOIN model m ON pamr.model_id = m.id " +
//                        "JOIN action a ON pamr.action_id = a.id " +
//                        "JOIN users_permission up ON pamr.permission_id = up.permission_id " +
//                        "JOIN users u ON up.user_id = u.id " +
//                        "WHERE m.name = :model " +
//                        "AND a.name = :action " +
//                        "AND pamr.raw_id = :rawId " +
//                        "AND u.name = :userName")
//                .bind("userName", userName)
//                .bind("rawId", rawId)
//                .bind("action", actionName)
//                .bind("model", modelName)
//                .map((row, metadata) -> row.get(0, Long.class)) // Giả sử COUNT trả về Long
//                .one();
//    }
//
//
//    @Override
//    public <S extends PermissionActionModelRaw> Mono<S> save(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends PermissionActionModelRaw> Flux<S> saveAll(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public <S extends PermissionActionModelRaw> Flux<S> saveAll(Publisher<S> entityStream) {
//        return null;
//    }
//
//    @Override
//    public Mono<PermissionActionModelRaw> findById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Mono<PermissionActionModelRaw> findById(Publisher<Long> id) {
//        return null;
//    }
//
//    @Override
//    public Mono<Boolean> existsById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Mono<Boolean> existsById(Publisher<Long> id) {
//        return null;
//    }
//
//    @Override
//    public Flux<PermissionActionModelRaw> findAll() {
//        return null;
//    }
//
//    @Override
//    public Flux<PermissionActionModelRaw> findAllById(Iterable<Long> longs) {
//        return null;
//    }
//
//    @Override
//    public Flux<PermissionActionModelRaw> findAllById(Publisher<Long> idStream) {
//        return null;
//    }
//
//    @Override
//    public Mono<Long> count() {
//        return null;
//    }
//
//    @Override
//    public Mono<Void> deleteById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Mono<Void> deleteById(Publisher<Long> id) {
//        return null;
//    }
//
//    @Override
//    public Mono<Void> delete(PermissionActionModelRaw entity) {
//        return null;
//    }
//
//    @Override
//    public Mono<Void> deleteAllById(Iterable<? extends Long> longs) {
//        return null;
//    }
//
//    @Override
//    public Mono<Void> deleteAll(Iterable<? extends PermissionActionModelRaw> entities) {
//        return null;
//    }
//
//    @Override
//    public Mono<Void> deleteAll(Publisher<? extends PermissionActionModelRaw> entityStream) {
//        return null;
//    }
//
//    @Override
//    public Mono<Void> deleteAll() {
//        return null;
//    }
//
//    // Thêm các phương thức khác nếu cần
//}
