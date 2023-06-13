package com.coke.yangboot.Demo.duotai;

public class Demo {
    public static void main(String[] args) {
        Animail animail = new Dog();
        System.out.println(animail.age);
        animail.eat();
        Dog dog = (Dog) animail;
        System.out.println(dog.age);
        dog.eat();
        dog.sleep();
    }
}
