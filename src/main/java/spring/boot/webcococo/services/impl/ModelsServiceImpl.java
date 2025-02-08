package spring.boot.webcococo.services.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.webcococo.entities.Models;
import spring.boot.webcococo.repositories.ModelsRepository;
import spring.boot.webcococo.services.IModelService;

import java.util.Optional;

@Service
@Log4j2
public class ModelsServiceImpl extends BaseServiceImpl<Models, Integer, ModelsRepository> implements IModelService {
    //
//
    private final ModelsRepository modelsRepository;

    public ModelsServiceImpl(ModelsRepository modelsRepository, ModelsRepository modelsRepository1) {
        super(modelsRepository);
        this.modelsRepository = modelsRepository1;
    }

    @Transactional
    public Models findOrCreateModel(Object entity) {
        Class<?> clazzCategories = entity.getClass();
        String entityName = clazzCategories.getSimpleName();

        try {
            // Thử tìm kiếm model theo tên entity
            Optional<Models> optionalModel = modelsRepository.findByName(entityName);

            // Trả về model nếu tìm thấy
            if (optionalModel.isPresent()) {
                return optionalModel.get();
            } else {
                // Nếu không tìm thấy, tạo mới model và lưu vào cơ sở dữ liệu
                Models newModel = new Models();
                newModel.setName(entityName);
                modelsRepository.save(newModel);

                // Trả về đối tượng mới vừa được tạo
                return newModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Ghi log lỗi nếu có ngoại lệ và ném lại ngoại lệ
            log.error("Error while finding or creating model for entity: " + entityName, e);
            throw e; // Hoặc có thể ném một ngoại lệ khác nếu cần
        }
    }
//    @Override
//    @PreAuthorize("@authorUtils.hasPermissionRaw(#id, #action)")
//    public Optional<Posts> getPostById(Integer id , String action) {
//        Optional<Posts> postsOptional = postsRepository.findById(id);
//        return postsOptional;
//    }

//    @Override
//    public List<Posts> getAllPosts() {
//        return postsRepository.findAll();
//    }
//
//    @Override
//    @PreAuthorize("@authorUtils.hasPermissionRaw(#id, #action)")
//    public Optional<Posts> getPostById(Integer id , String action) {
//        Optional<Posts> postsOptional = postsRepository.findById(id);
//        return postsOptional;
//    }
//
//    @Override
//    public Posts createPost(Posts posts) {
//        return postsRepository.save(posts);
//    }
//
//
//    @Override
//    public Posts updatePost(Integer id, Posts updatedPosts) {
//        return postsRepository.findById(id)
//            .map(post -> {
//                post.setTitle(updatedPosts.getTitle());
//                post.setBody(updatedPosts.getBody());
//                return postsRepository.save(post);
//            }).orElseThrow(() -> new RuntimeException("Post not found with id " + id));
//    }
//
//    @Override
//    public void deletePost(Integer id) {
//        postsRepository.deleteById(id);
//    }
//


}
