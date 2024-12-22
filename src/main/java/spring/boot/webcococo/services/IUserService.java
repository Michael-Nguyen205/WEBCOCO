package spring.boot.webcococo.services;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import spring.boot.webcococo.entities.Users;
import spring.boot.webcococo.models.requests.UserRegisterRequest;
import spring.boot.webcococo.models.response.UserLoginResponse;
import spring.boot.webcococo.models.response.UserRegisterResponse;



@Component
public interface IUserService {
//    Users createUser(UserRegisterRequest userRegisterRequest) throws Exception;

   UserRegisterResponse createUser(UserRegisterRequest userRegisterRequest);

    UserLoginResponse login(String email, String password , HttpServletRequest request ) ;


     Users getUser(String userId);
    void deleteUser(String userId);



    void approveUser(Boolean statusActive,String userId );

//    String changePass( String phoneNumber , String password) throws Exception;
//    Users getUserDetailsFro
}