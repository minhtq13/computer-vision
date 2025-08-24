package com.elearning.elearning_support.utils.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ListStringConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        if(Objects.isNull(strings) || strings.isEmpty()) return "[]";
        return strings.toString();
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        List<String> lstString = new ArrayList<>();
        if(Objects.isNull(s)) return lstString;
        lstString = Arrays.stream(s.replace("[", "").replace("]", "").split(","))
            .map(String::new).collect(Collectors.toList());
        return lstString;
    }
}
