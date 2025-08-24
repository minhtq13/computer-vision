package com.elearning.elearning_support.enums.users;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IdentityTypeEnum {

    ALL(-1, "Tất cả"),
    ID_CARD(0, "Chứng minh thư nhân dân"),
    CITIZEN_ID_CARD(1, "Căn cước công dân"),
    PASSPORT(2, "Hộ chiếu");


    private final Integer value;
    private final String type;

    /**
     * @param value : int type
     * @return : enum type
     */
    public static IdentityTypeEnum valueOf(Integer value) {
        if (Objects.isNull(value)) return null;
        switch (value) {
            case 0:
                return ID_CARD;
            case 1:
                return CITIZEN_ID_CARD;
            case 2:
                return PASSPORT;
            default:
                return null;
        }
    }

}
