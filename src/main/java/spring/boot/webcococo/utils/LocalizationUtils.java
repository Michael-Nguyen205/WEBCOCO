package spring.boot.webcococo.utils;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtils {
    //MessageSource: Là một interface của Spring dùng để lấy
    // thông điệp (message) từ các file properties đã được
    // dịch (ví dụ: messages.properties)
    private final MessageSource messageSource;
    //LocaleResolver: Là một interface của Spring dùng để xác định ngôn
    // ngữ và khu vực (locale) của người dùng. localeResolver giúp xác định
    // ngôn ngữ người dùng muốn sử dụng (ví dụ: en_US cho tiếng Anh,
    // vi_VN cho tiếng Việt).
    private final LocaleResolver localeResolver;
    public String getLocalizedMessage(String messageKey, Object... params) {//spread operator
        HttpServletRequest request = WebUtils.getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey, params, locale);
    }
}
