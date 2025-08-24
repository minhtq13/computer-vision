package com.elearning.elearning_support.enums.users;

import java.util.Objects;
import org.springframework.util.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GenderEnum {

    FEMALE(0, "Nữ", "Female"),
    MALE(1, "Nam", "Male"),
    OTHER(2, "Khác", "Other");

    private final Integer type;

    private final String name;

    private final String engName;

    /**
     * Get the gender by english name
     */
    public static Integer getGenderByEngName(String engName) {
        if (ObjectUtils.isEmpty(engName)) {
            return null;
        }
        if (Objects.equals(engName.toLowerCase(), FEMALE.engName.toLowerCase())) {
            return FEMALE.getType();
        } else if (Objects.equals(engName.toLowerCase(), MALE.engName.toLowerCase())) {
            return MALE.getType();
        } else if (Objects.equals(engName.toLowerCase(), OTHER.engName.toLowerCase())) {
            return OTHER.getType();
        } else {
            return null;
        }
    }

    /**
     * Get the gender by vietnamese name
     */
    public static Integer getGenderByVnName(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return null;
        }
        if (Objects.equals(name.toLowerCase(), FEMALE.getName().toLowerCase())) {
            return FEMALE.getType();
        } else if (Objects.equals(name.toLowerCase(), MALE.getName().toLowerCase())) {
            return MALE.getType();
        } else if (Objects.equals(name.toLowerCase(), OTHER.getName().toLowerCase())) {
            return OTHER.getType();
        } else {
            return null;
        }
    }
}
