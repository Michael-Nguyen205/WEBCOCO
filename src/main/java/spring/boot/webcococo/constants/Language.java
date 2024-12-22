package spring.boot.webcococo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum Language {
    ENGLISH("en", "English"),
    FRENCH("fr", "French"),
    GERMAN("de", "German"),
    SPANISH("es", "Spanish"),
    VIETNAMESE("vi", "Vietnamese"),
    CHINESE("zh", "Chinese"),
    JAPANESE("ja", "Japanese"),
    KOREAN("ko", "Korean"),
    RUSSIAN("ru", "Russian"),
    ITALIAN("it", "Italian"),
    PORTUGUESE("pt", "Portuguese");

    private final String code;
    private final String name;

    // Method to get enum by code
    public static Language fromCode(String code) {
        for (Language language : Language.values()) {
            if (language.getCode().equalsIgnoreCase(code)) {
                return language;
            }
        }
        throw new IllegalArgumentException("No language found with code: " + code);
    }
}
