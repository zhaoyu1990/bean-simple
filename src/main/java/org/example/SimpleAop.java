package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SimpleAop implements InvocationHandler {
    private Object target;

    public SimpleAop(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
