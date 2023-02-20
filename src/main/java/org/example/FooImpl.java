package org.example;

@Component
public class FooImpl implements Foo{
    @Resource
    private Boo boo;

    @Override
    public void say() {
        System.out.println("i am boo");
    }
}
