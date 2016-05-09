package com.puppycrawl.tools.checkstyle.checks.annotation;

public class InputIncorrectAnnotationAmongModifiers {

    public @NotAmongModifiers Object object1 = new Object();
    public @NotAmongModifiers final Object object2 = new Object();
    public static @NotAmongModifiers final transient Object object3 = new Object();
    protected static volatile transient @NotAmongModifiers Object object4 = new Object();

    private @NotAmongModifiers void method1() {
    }

    public @NotAmongModifiers static void method2() {
    }

    public static @NotAmongModifiers strictfp void method3() {
    }

    protected final synchronized @NotAmongModifiers strictfp void method4() {
    }

    public @NotAmongModifiers class AnnotationAmongModifiersClass1 {
    }

    protected @NotAmongModifiers static class AnnotationAmongModifiersClass2 {
    }

    private static @NotAmongModifiers final class AnnotationAmongModifiersClass3 {
    }

    public static abstract @NotAmongModifiers class AnnotationAmongModifiersClass4 {
    }

    protected static abstract strictfp @NotAmongModifiers class AnnotationAmongModifiersClass5 {
    }
}

@interface NotAmongModifiers {
}
