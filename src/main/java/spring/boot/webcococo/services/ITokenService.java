package spring.boot.webcococo.services;



import jakarta.validation.constraints.NotBlank;
import spring.boot.webcococo.entities.Tokens;
import spring.boot.webcococo.entities.Users;


public interface ITokenService {
    Tokens addToken(@NotBlank(message = "Phone number is required") String userDetails, String token, boolean isMobileDevice);
    Tokens refreshToken(String refreshToken, Users user) throws Exception;
}
