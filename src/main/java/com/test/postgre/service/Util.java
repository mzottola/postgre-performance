package com.test.postgre.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {
    public static List<String> createListOfUsers(boolean even) {
        int numberOfUsers = even ? 100 : 120;
        return IntStream.range(1, numberOfUsers)
                .mapToObj(it -> "username_" + it)
                .collect(Collectors.toList());
    }
}
