package spring.boot.webcococo.controllers;//package spring.boot.authenauthor.controllers;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import spring.boot.authenauthor.entities.Posts;
//import spring.boot.authenauthor.enums.ActionsEnum;
//import spring.boot.authenauthor.services.IPostsService;
//import spring.boot.authenauthor.services.impl.PostsServiceImpl;
//
//import java.util.List;
//import java.util.Optional;
//@Log4j2
//@RestController
//@RequestMapping("${api.prefix}/posts")
//@RequiredArgsConstructor
//public class PostsController {
//
////
////    @Autowired
////    private IPostsService postService;
//
//
//    @Autowired
//    private PostsServiceImpl postService;
//
//
//    // Get a single post by ID
//
//
//
//    // Get all posts
//    @GetMapping
//    public ResponseEntity<List<Posts>> getAllPosts() {
//        return ResponseEntity.ok(postService.findAll()
//        );
//    }
//
//
//
//
//
////    @PostMapping
////    @PreAuthorize("@authorizationService.hasPermission(authentication, 'create')")
////    public ResponseEntity<Posts> createPost(@RequestBody Posts post) {
////        return ResponseEntity.ok(postService.createPost(post));
////    }
//
//
//
//
//    // Update an existing post
//    @PutMapping("/{id}")
//    @PreAuthorize("@authorizationService.hasPermission(authentication, 'update')")
//    public ResponseEntity<Posts> updatePost(@PathVariable Integer id, @RequestBody Posts updatedPost) {
//        return ResponseEntity.ok(postService.update(id,updatedPost));
//    }
//
//
//
////  @DefaultValue("-1")
//    @GetMapping("/{id}")
//    @PreAuthorize("@authorizationService.hasAuthor(authentication,'view' , 'posts', #id  )")
////    @PreAuthorize("@authorUtils.hasPermissionAction(authentication, #action)")
//    public ResponseEntity<Posts> getPostById(@PathVariable Integer id ) {
//        Optional<Posts> post = postService.findById(id);
//        return post.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Delete a post
//    @DeleteMapping("/{id}")
//    @PreAuthorize("@authorizationService.hasPermission(authentication, 'delete')")
//    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
//        postService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}
//
//
//
//
//
////    @PostMapping("")
////    public ResponseEntity<?> createOrder(
////            @Valid @RequestBody OrderDTO orderDTO,
////
////            BindingResult result
////    ) {
//////        try {
////            if(result.hasErrors()) {
////                List<String> errorMessages = result.getFieldErrors()
////                        .stream()
////                        .map(FieldError::getDefaultMessage)
////                        .toList();
////                return ResponseEntity.badRequest().body(errorMessages);
////            }
////            TblCustomer orderResponse = orderService.createOrder(orderDTO);
////            return ResponseEntity.ok(orderResponse);
//////        } catch (Exception e) {
//////            return ResponseEntity.badRequest().body(e.getMessage());
//////        }
////    }
////
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
//
//
//
//
//
////    PageRequest pageRequest = PageRequest.of(
////            page, limit,
////            //Sort.by("createdAt").descending()
////            Sort.by("id").ascending()
////    );
//
//
//
//
////    @Override
////    @Transactional
////    public void deleteProduct(long id) {
////        Optional<Product> optionalProduct = productRepository.findById(id);
////        optionalProduct.ifPresent(productRepository::delete);
////    }
//
//
////
////    @DeleteMapping("/{id}")
////    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) {
////        //xóa mềm => cập nhật trường active = false
////        orderService.deleteOrder(id);
////        String result = localizationUtils.getLocalizedMessage(
////                MessageKeys.DELETE_ORDER_SUCCESSFULLY, id);
////        return ResponseEntity.ok().body(result);
////    }
////
//
//
//
//
