package com.coke.yangboot.Demo.duotai;

public class Dog extends Animail{
    public int age = 1;
    public static int dogage = 1;
    @Override
    public void eat() {
        System.out.println("吃狗粮");
    }
    public void  sleep(){
        System.out.println("闭眼");

    }

}
