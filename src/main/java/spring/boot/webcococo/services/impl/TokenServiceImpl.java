package spring.boot.webcococo.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Tokens;
import spring.boot.webcococo.entities.Users;
import spring.boot.webcococo.enums.ErrorCodeEnum;
import spring.boot.webcococo.exceptions.AppException;
import spring.boot.webcococo.repositories.TokenRepository;
import spring.boot.webcococo.repositories.UserRepository;
import spring.boot.webcococo.services.ITokenService;
import spring.boot.webcococo.utils.JwtTokenUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

    @Value("${jwt.max-token}")
    private int MAX_TOKENS;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Tokens addToken(String email, String token, boolean isMobileDevice) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "User not found"));

        List<Tokens> userTokens = tokenRepository.findByUserId(user.getId());

        if (userTokens.size() >= MAX_TOKENS) {
            deleteOldToken(userTokens);
        }

        return saveNewToken(user.getId(), token, isMobileDevice);
    }

    private void deleteOldToken(List<Tokens> userTokens) {
        log.error("List<Tokens> : {}", userTokens);

        Tokens tokenToDelete = userTokens.stream()
                .filter(userToken -> !userToken.isMobile())
                .findFirst()
                .orElse(userTokens.get(0)); // Nếu không có token nào khác thì lấy token đầu tiên

        tokenRepository.delete(tokenToDelete); // Xóa token
    }

    private Tokens saveNewToken(String userId, String token, boolean isMobileDevice) {
        log.error("đang lưu token");
        if (token.length() > 500) {
            throw new AppException(ErrorCodeEnum.INVALID_TOKEN, "Token phải có độ dài nhỏ hơn hoặc bằng 500 ký tự");
        }

        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
        Tokens newToken = Tokens.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .isMobile(isMobileDevice)
                .refreshToken(UUID.randomUUID().toString())
                .refreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken))
                .build();

        try {
            return tokenRepository.save(newToken);
        } catch (Exception e) {
            log.error("Lỗi khi lưu token: {}", e.getMessage(), e);
            throw new AppException(ErrorCodeEnum.DATABASE_SAVE_ERROR, "Lưu token lỗi");
        }
    }

    @Override
    public Tokens refreshToken(String refreshToken, Users user) {
        Tokens existingToken = tokenRepository.findByRefreshToken(refreshToken);

        if (existingToken == null) {
            throw new AppException(ErrorCodeEnum.DATA_NOT_FOUND, "Refresh token not found");
        }

        return existingToken;
    }
}
