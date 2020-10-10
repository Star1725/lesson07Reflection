package com.geekbrains.java.ProfessionalLevel.myTestingToolkit;

import com.geekbrains.java.ProfessionalLevel.myTestingToolkit.annotations.AfterSuite;
import com.geekbrains.java.ProfessionalLevel.myTestingToolkit.annotations.BeforeSuite;
import com.geekbrains.java.ProfessionalLevel.myTestingToolkit.annotations.Test;

public class ClassForTest {
    @BeforeSuite
    public static void init(){
        System.out.println("before test\n");
    }
    @Test
    public void test1(){
        System.out.println("test1\n");
    }
    @Test
    public void test4(){
        System.out.println("test4\n");
    }
    @Test(priority = 5)
    public static void test2(){
        System.out.println("test2\n");
    }
    @Test(priority = 10)
    public static void test3(){
        System.out.println("test3\n");
    }
    @AfterSuite
    public static void finalTest1(){
        System.out.println("finalTest\n");
    }
    //@AfterSuite
    public static void finalTest2(){
        System.out.println("finalTest\n");
    }
}
