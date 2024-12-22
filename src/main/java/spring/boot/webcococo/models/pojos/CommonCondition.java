package spring.boot.webcococo.models.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonCondition {

    Integer page = 1;

    Integer size = 5;

    String keyword;
}


