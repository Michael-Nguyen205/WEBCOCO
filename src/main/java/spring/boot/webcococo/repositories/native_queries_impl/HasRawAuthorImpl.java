package spring.boot.webcococo.repositories.native_queries_impl;//package spring.boot.authenauthor.repositories.native_queries_impl;
//
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.data.repository.query.Param;
//import spring.boot.authenauthor.entities.PermissionActionRaw;
//import spring.boot.authenauthor.repositories.PermissionActionRawRepository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//
//public class HasRawAuthorImpl implements PermissionActionRawRepository {
//
//
//    @Override
//    @Query(value = "SELECT COUNT(*) > 0 " +
//            "FROM permission_action_raw par " +
//            "JOIN actions a ON par.action_id = a.id " +
//            "WHERE par.permission_id = :permissionId " +
//            "AND par.posts_id = :postId " +
//            "AND a.name = :actionName", nativeQuery = true)
//    boolean hasPermission(@Param("permissionId") Integer permissionId,
//                          @Param("postId") Integer postId,
//                          @Param("actionName") String actionName);
//
//
//
//    @Override
//    public List<Integer> findActionIdsByPermissionId(Integer permissionId) {
//        return null;
//    }
//
//    @Override
//    public List<Integer> findPostIdsByPermissionAndAction(Integer permissionId, Integer actionId) {
//        return null;
//    }
//
//
//
//
//
//
//
//
//
//
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> S saveAndFlush(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> List<S> saveAllAndFlush(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<PermissionActionRaw> entities) {
//
//    }
//
//    @Override
//    public void deleteAllByIdInBatch(Iterable<Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//
//    }
//
//    @Override
//    public PermissionActionRaw getOne(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public PermissionActionRaw getById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public PermissionActionRaw getReferenceById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> List<S> findAll(Example<S> example) {
//        return null;
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> List<S> findAll(Example<S> example, Sort sort) {
//        return null;
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public <S extends PermissionActionRaw, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//        return null;
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> S save(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends PermissionActionRaw> List<S> saveAll(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public Optional<PermissionActionRaw> findById(Long aLong) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return false;
//    }
//
//    @Override
//    public List<PermissionActionRaw> findAll() {
//        return null;
//    }
//
//    @Override
//    public List<PermissionActionRaw> findAllById(Iterable<Long> longs) {
//        return null;
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//
//    }
//
//    @Override
//    public void delete(PermissionActionRaw entity) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends PermissionActionRaw> entities) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public List<PermissionActionRaw> findAll(Sort sort) {
//        return null;
//    }
//
//    @Override
//    public Page<PermissionActionRaw> findAll(Pageable pageable) {
//        return null;
//    }
//}
