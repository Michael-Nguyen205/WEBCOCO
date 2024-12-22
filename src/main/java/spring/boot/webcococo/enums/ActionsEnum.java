package spring.boot.webcococo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ActionsEnum {
    VIEW("VIEW", "View an item"),
    CREATE("CREATE", "Create a new item");

    private String type;
    private String description;

    public static ActionsEnum fromType(String type) {
        return Arrays.stream(ActionsEnum.values())
                .filter(actionEnum -> actionEnum.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid action type: " + type));
    }
}
