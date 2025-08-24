package com.elearning.elearning_support.utils.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SetStringConverter implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> strings) {
        if(Objects.isNull(strings) || strings.isEmpty()) return "[]";
        return strings.toString();
    }

    @Override
    public Set<String> convertToEntityAttribute(String s) {
        Set<String> setString = Collections.emptySet();
        if(Objects.isNull(s)) return setString;
        setString = Arrays.stream(s.replace("[", "").replace("]", "").split(","))
            .map(String::new).collect(Collectors.toSet());
        return setString;
    }
}
