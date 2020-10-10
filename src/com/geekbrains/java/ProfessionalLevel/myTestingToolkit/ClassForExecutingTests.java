package com.geekbrains.java.ProfessionalLevel.myTestingToolkit;

import com.geekbrains.java.ProfessionalLevel.myTestingToolkit.annotations.AfterSuite;
import com.geekbrains.java.ProfessionalLevel.myTestingToolkit.annotations.BeforeSuite;
import com.geekbrains.java.ProfessionalLevel.myTestingToolkit.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassForExecutingTests {

    static List<Method> arrayPriority = new ArrayList<>();
    static boolean beforeFlag;
    static boolean afterFlag;

    public static void start(Class testClass) throws NoSuchMethodException {

        Constructor constructor = testClass.getConstructor();
        Method[] methods = testClass.getDeclaredMethods();

        checkReplaceAnnotation(methods);
        startBeforeMethod(beforeFlag, methods, constructor);

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)){
                createdArrayPriority(method);
            }
        }
        for (Method method : arrayPriority) {
            System.out.println("Приоритет тестирования метода - " + method.getAnnotation(Test.class).priority());
            invokeMethod(constructor, method);
        }

        startAfterMethod(afterFlag, methods, constructor);
    }

    private static void createdArrayPriority(Method method) {
        int currentPriority = method.getAnnotation(Test.class).priority();
        int arrSize = arrayPriority.size();
        if (arrSize == 0){
            arrayPriority.add(method);
        } else {
            for (int i = 0; i < arrSize; i++) {
                if (arrayPriority.get(i).getAnnotation(Test.class).priority() <= currentPriority) {
                    arrayPriority.add(i, method);
                    break;
                }
                if (i == arrSize - 1){
                    arrayPriority.add(method);
                }
            }
        }
    }

    private static void invokeMethod(Constructor constructor, Method method) {
        if (Modifier.isStatic(method.getModifiers())){
            try {
                method.invoke(null);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                method.invoke(constructor.newInstance());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkReplaceAnnotation(Method[] methods) {
        int beforeCount = 0;
        int afterCount = 0;
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)){
                beforeCount++;
                beforeFlag = true;
            }
            if (method.isAnnotationPresent(AfterSuite.class)){
                afterCount++;
                afterFlag = true;
            }
            if (beforeCount == 2 || afterCount == 2){
                throw new RuntimeException("В тестируемом классе более одного метода, помеченных анотацией @BeforeSuite либо @AfterSuite");
            }
        }
    }

    private static void startBeforeMethod(boolean beforeFlag, Method[] methods, Constructor constructor) {
        if (beforeFlag){
            for (Method method : methods) {
                if (method.isAnnotationPresent(BeforeSuite.class)){
                    invokeMethod(constructor, method);
                }
            }
        }
    }

    private static void startAfterMethod(boolean afterFlag, Method[] methods, Constructor constructor) {
        if (afterFlag){
            for (Method method : methods) {
                if (method.isAnnotationPresent(AfterSuite.class)){
                    invokeMethod(constructor, method);
                }
            }
        }
    }
}
