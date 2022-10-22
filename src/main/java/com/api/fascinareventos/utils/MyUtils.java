package com.api.fascinareventos.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class MyUtils {

    public static Double RoundDecimal(double value, int decimals) {
        double dc = Math.pow(10, decimals);
        return Math.round(value * dc) / dc;
    }

    public static Integer RoundMultiple(double value, int multiple) {
        double sum = value / multiple;
        return Math.toIntExact(Math.round(sum) * multiple);
    }

    public static Page<?> toPage(List<?> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if(start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
}
