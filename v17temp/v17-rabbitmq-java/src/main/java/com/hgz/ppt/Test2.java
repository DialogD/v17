package com.hgz.ppt;

/**
 * @Author: DJA
 * @Date: 2019/11/7
 */
public class Test2 {
    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        swap(a,b);
        System.out.println(a);
        System.out.println(b);

        Student stu = new Student("张三");
        change(stu);
        System.out.println(stu);
    }
    private static void change(Student stu2) {
        stu2 = new Student();
        stu2.setName("zs");
    }
    private static void swap(int a, int b) {
        int temp = a;
        a = b;
        b = temp;
    }

}
