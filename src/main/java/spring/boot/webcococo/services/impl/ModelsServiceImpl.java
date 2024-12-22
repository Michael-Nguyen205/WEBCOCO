package spring.boot.webcococo.services.impl;

import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Models;
import spring.boot.webcococo.repositories.ModelsRepository;
import spring.boot.webcococo.services.IModelService;

@Service

public class ModelsServiceImpl extends BaseServiceImpl<Models, Integer, ModelsRepository> implements IModelService {
//
//


public ModelsServiceImpl(ModelsRepository modelsRepository) {
    super(modelsRepository);
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
