package com.elearning.elearning_support.utils;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class CollectionUtils {

    // generate a sequence numbers with a step
    public static List<Double> generateDoubleSequenceWithStep(Double start, Double end, Double step) {
        return DoubleStream.iterate(start, curr -> curr <= end, curr -> curr + step).boxed()
            .collect(toList());
    }

    public static List<Integer> generateIntSequenceWithStep(Integer start, Integer end, Integer step) {
        return IntStream.iterate(start, curr -> curr <= end, curr -> curr + step).boxed().collect(toList());
    }

    public static List<Long> generateLongSequenceWithStep(Long start, Long end, Long step) {
        return LongStream.iterate(start, curr -> curr <= end, curr -> curr + step).boxed().collect(toList());
    }
}
