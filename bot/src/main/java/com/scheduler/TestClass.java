package com.scheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 4/7/2021
 */
public class TestClass {

    public static void main(String[] args) {

        List<Person> persons = new ArrayList<>();

        persons.add(new Person("djkb", 30));

        for (Person person : persons) {
            if (person.getAge() > 20) {
                persons.remove(person);
            }
        }
    }

}
