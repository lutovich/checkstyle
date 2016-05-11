import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class InputTypeParameterAnnotations {

    public void methodWithAnnotatedVariables() {
        List<@TypeAnnotation1 String> l1 = new ArrayList<>();
        List<@TypeAnnotation1 @TypeAnnotation2 String> l2 = new ArrayList<>();
        Map<@TypeAnnotation1 String,@TypeAnnotation2 Map<String,@TypeAnnotation1 String>> m1 = new HashMap<>();
        Map.@TypeAnnotation1 Entry<String,String> e1 = null;
        Map.@TypeAnnotation1 Entry<@TypeAnnotation2 String,@TypeAnnotation2 String> e2 = null;

        Object o1 = new @TypeAnnotation1 Object();
        List<String> l3 = new @TypeAnnotation1 @TypeAnnotation2 ArrayList<>();

        Outer outer = new Outer();
        Outer.Inner inner = outer.new @TypeAnnotation1 Inner();

        Object o2 = "";
        String s1 = (@TypeAnnotation1 String) o2;
        String s2 = (@TypeAnnotation1 @TypeAnnotation2 String) o2;

        List<? super @TypeAnnotation1 String> l4 = new ArrayList<>();
        List<@TypeAnnotation1 ? super @TypeAnnotation2 String> l5 = new ArrayList<>();
        List<@TypeAnnotation1 ? extends Comparable<String>> l6 = new ArrayList<>();
        List<@TypeAnnotation1 ? extends @TypeAnnotation2 Comparable<@TypeAnnotation1 String>> l7 = new ArrayList<>();

        List<String> l8 = Collections.<@TypeAnnotation1 String>emptyList();

        Object o3 = new <@TypeAnnotation1 String>Object();

        boolean b1 = o2 instanceof @TypeAnnotation1 String;
        boolean b2 = o2 instanceof @TypeAnnotation1 @TypeAnnotation2 String;
    }

    public <T extends @TypeAnnotation1 Callable<T> & @TypeAnnotation2 Runnable> void method1() {
    }

    public void method2() throws @TypeAnnotation1 IOException {
    }

    public void method3() throws @TypeAnnotation1 @TypeAnnotation2 IOException {
    }
}

class Outer {
    class Inner {
    }
}

class MyRunnable1 implements @TypeAnnotation1 Runnable {
    @Override
    public void run() {
    }
}

class MyRunnable2 implements @TypeAnnotation1 @TypeAnnotation2 Runnable {
    @Override
    public void run() {
    }
}

class Folder<F extends @TypeAnnotation1 File> {
}

@Target( {ElementType.TYPE_USE, ElementType.TYPE_PARAMETER} )
@interface TypeAnnotation1 {
}

@Target( {ElementType.TYPE_USE, ElementType.TYPE_PARAMETER} )
@interface TypeAnnotation2 {
}
