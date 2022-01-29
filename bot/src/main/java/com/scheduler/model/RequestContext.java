package com.scheduler.model;

/**
 * @author Serhii_Udaltsov on 10/23/2021
 */
public class RequestContext {

    private String name;
    private int age;
    private boolean flag;

    public RequestContext(String name, int age, boolean flag) {
        this.name = name;
        this.age = age;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
