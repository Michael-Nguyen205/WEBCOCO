package spring.boot.webcococo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ActionEnum {
    POST( ""),
    UPDATE( ""),
    VIEW( ""),
    DELETE( "");

    private final String name;

    // Method to get enum by code

}
