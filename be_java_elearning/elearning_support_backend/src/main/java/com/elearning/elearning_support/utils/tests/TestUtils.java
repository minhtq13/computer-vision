package com.elearning.elearning_support.utils.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.util.ObjectUtils;

public class TestUtils {

    public static Integer DEFAULT_DURATION = 60;

    public static Map<String, Integer> mapFromCharToBinAnswer = new HashMap<>();

    public static Map<Integer, String> mapFromBinToCharAnswer = new HashMap<>();

    static {
        mapFromCharToBinAnswer.put("A", 1);
        mapFromCharToBinAnswer.put("B", 2);
        mapFromCharToBinAnswer.put("C", 3);
        mapFromCharToBinAnswer.put("D", 4);
        mapFromCharToBinAnswer.put("E", 5);
        mapFromCharToBinAnswer.put("F", 6);

        mapFromBinToCharAnswer.put(1, "A");
        mapFromBinToCharAnswer.put(2, "B");
        mapFromBinToCharAnswer.put(3, "C");
        mapFromBinToCharAnswer.put(4, "D");
        mapFromBinToCharAnswer.put(5, "E");
        mapFromBinToCharAnswer.put(6, "F");
    }


    /**
     * Convert selected answer from text to integer
     */
    public static Set<Integer> getSelectedAnswerNo(String selectedAnswers) {
        if (Objects.equals(selectedAnswers, "")) {
            return new HashSet<>();
        }
        return Arrays.stream(selectedAnswers.trim().split("")).map(item -> mapFromCharToBinAnswer.get(item)).collect(Collectors.toSet());
    }


    /**
     * Convert selected answer from integer to text
     */
    public static String getSelectedAnswerChar(Set<Integer> selectedAnswers) {
        if (ObjectUtils.isEmpty(selectedAnswers)) {
            return "";
        }
        return selectedAnswers.stream().map(item -> mapFromBinToCharAnswer.get(item)).collect(Collectors.joining());
    }

}
