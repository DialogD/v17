package com.hgz.ppt;

/**
 * @Author: DJA
 * @Date: 2019/11/7
 */
public class Student {
    private String name;

    public String getName() {
        return name;
    }

    public Student() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public Student(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
