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

    public static void star(Class testClass) throws NoSuchMethodException {

        Constructor constructor = testClass.getConstructor();

        boolean beforeFlag;
        boolean afterFlag;
        Method[] methods = testClass.getDeclaredMethods();
        List<Method> methodList = new ArrayList<>(Arrays.asList(methods));
        beforeFlag = methodList.removeIf(method -> method.isAnnotationPresent(BeforeSuite.class));
        afterFlag = methodList.removeIf(method -> method.isAnnotationPresent(AfterSuite.class));

        checkReplaceAnnotation(beforeFlag, afterFlag, methodList);
        startBeforeMethod(beforeFlag, methods, constructor);

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)){
                invokeMethod(constructor, method);
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

    private static void checkReplaceAnnotation(boolean beforeFlag, boolean afterFlag, List<Method> methodList) {
        if (beforeFlag || afterFlag){
            if (methodList.removeIf(method -> method.isAnnotationPresent(BeforeSuite.class)) || methodList.removeIf(method -> method.isAnnotationPresent(AfterSuite.class))){
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
}
