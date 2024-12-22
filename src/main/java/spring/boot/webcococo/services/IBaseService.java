package spring.boot.webcococo.services;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IBaseService<T, ID extends Serializable> {

    // Trả về tất cả các entity dưới dạng Flux<T>
    List<T> findAll();

    // Trả về một entity theo ID dưới dạng Mono<T>
    Optional<T> findById(ID id);

    // Lưu một entity mới dưới dạng Mono<T>
    T save(T entity);

    List<T> saveAll(List<T> entities);

    // Cập nhật entity theo ID dưới dạng Mono<T>
    T update( T entity);

    // Xóa một entity theo ID và trả về Mono<Void>
    void deleteById(ID id);
}
