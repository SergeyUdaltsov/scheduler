package com.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class TestClass {

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("First", "Second", "Third");

        List<String> elements = new ArrayList<>(Arrays.asList("First", "Fifth", "Fourth"));
        elements.add("sixth");
        for (String element : elements) {
            System.out.println(element);
        }
    }


}
