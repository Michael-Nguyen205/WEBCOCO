package spring.boot.webcococo.services.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.services.IBaseService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
//@Service
//@RequiredArgsConstructor
//@NoArgsConstructor
//@AllArgsConstructor
public class BaseServiceImpl<T, ID extends Serializable, R extends JpaRepository<T,ID>> implements IBaseService<T, ID> {

    protected R repository;

    BaseServiceImpl(R repo) {
        this.repository = repo;
    }
    @Override
    public Optional<T> findById(ID id) {
        log.error("Entity find by ID: {}", id);
        Optional<T> entity = repository.findById(id);
        if (entity.isEmpty()) {
            log.error("Entity not found for ID: {}", id);
            throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Entity not found for ID: " + id);
        }
        return entity;
    }

    @Override
    public List<T> findAll() {
        try {

            return repository.findAll();
        } catch (Exception e) {
            log.error("Error fetching all entities: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "No entities found");
        }
    }



    @Override
    public T save(T entity) {
        try {
            return repository.save(entity);
        } catch (DuplicateKeyException e) {
            log.error("Duplicate key error: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.DUPLICATE_DATA, "Entity already exists");
        } catch (DataIntegrityViolationException e) {
            log.error("Duplicate key error: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.DUPLICATE_DATA, "Entity already exists");
        } catch (Exception e) {
            log.error("Error saving entity: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.INVALID_KEY, "Internal server error");
        }
    }



    @Override
    public List<T> saveAll(List<T> entities) {
        try {
            // Lưu các entity vào repository
            return new ArrayList<>(repository.saveAll(entities));
        } catch (DuplicateKeyException e) {
            log.error("Duplicate key error: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.DUPLICATE_DATA, "Entity already exists");
        } catch (Exception e) {
            log.error("Error saving entity: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.INVALID_KEY, "Internal server error");
        }
    }



    @Override
    public T update(T updatedEntity) {
        try {
            return repository.save(updatedEntity);
        } catch (DuplicateKeyException e) {
            log.error("Duplicate key error: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.DUPLICATE_DATA, "Entity already exists");
        } catch (Exception e) {
            log.error("Error updating entity: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.INVALID_KEY, "Internal server error");
        }
    }

    @Override
    public void deleteById(ID id) {
        Optional<T> entity = repository.findById(id);
        if (entity.isEmpty()) {
            log.error("Entity not found for ID: {}", id);
            throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Entity not found for ID: " + id);
        }
        try {
            repository.deleteById(id);
            log.info("Entity with ID {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Error deleting entity with ID {}: {}", id, e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.DATABASE_SAVE_ERROR, "Error deleting entity");
        }
    }
}
