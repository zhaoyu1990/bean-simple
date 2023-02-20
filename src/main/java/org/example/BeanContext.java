package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class BeanContext {
    private static final Map<String, Object> realObjectMap = new HashMap<>();
    private static final Map<String, Object> objectFactoryMap = new HashMap<>();
    private static final Map<String, Object> proxyMap = new HashMap<>();

    public Set<Class<?>> getComponentAnnotationClass(String packageName) {
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName);
        if (Objects.isNull(resourceAsStream)) {
            return new HashSet<>();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        Set<Class<?>> collect = bufferedReader.lines().filter(line -> line.endsWith(".class")).map(line -> {
            try {
                return Class.forName(packageName.replaceAll("/", ".") + "." + line.replace(".class", ""));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet());
        return collect.stream().filter(f -> Objects.nonNull(f.getAnnotation(Component.class))).collect(Collectors.toSet());
    }

    public void initBean(Object o) throws IllegalAccessException {
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Resource annotation = field.getAnnotation(Resource.class);
            if (annotation != null) {
                Object o1 =  realObjectMap.get(field.getType().getSimpleName());
                field.set(o, o1);
            }
        }
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        BeanContext beanContext = new BeanContext();
        Set<Class<?>> componentAnnotationClass = beanContext.getComponentAnnotationClass("org/example");
        for (Class<?> annotationClass : componentAnnotationClass) {
            Object o = annotationClass.newInstance();
            realObjectMap.put(Arrays.stream(annotationClass.getInterfaces()).findFirst().get().getSimpleName(), o);
        }
        for (Map.Entry<String, Object> stringObjectEntry : realObjectMap.entrySet()) {
            beanContext.initBean(stringObjectEntry.getValue());
        }
        System.out.println(realObjectMap.get("Foo"));
    }
}
