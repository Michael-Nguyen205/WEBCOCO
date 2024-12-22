package spring.boot.webcococo.services;


import spring.boot.webcococo.models.requests.BankGatewayDetailRequest;
import spring.boot.webcococo.models.response.BankGatewayDetailResponse;

public interface IBankGateWayDetailService {

    BankGatewayDetailResponse getBankgateWayDetail(Integer id);


    BankGatewayDetailResponse createBankgateWayDetail(Integer paymentMethodId , Integer bankType, BankGatewayDetailRequest bankGatewayReques);


//    List<Posts> getAllPosts();
//    Optional<Posts> getPostById(Integer id , String action);
//    Posts createPost(Posts posts);
//    Posts updatePost(Integer id, Posts updatedPosts);
//    void deletePost(Integer id);
}
