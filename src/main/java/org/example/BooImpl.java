package org.example;

@Component
public class BooImpl implements Boo{
    @Resource
    private Foo foo;

    @Override
    public void say() {
        System.out.println("i am boo");
    }
}
