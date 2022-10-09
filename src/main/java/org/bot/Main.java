package org.bot;

public class Main {
    public static void main(String[] args) {
        Logic logic = new Logic();
        String startMessage = logic.getHello();
        System.out.println(startMessage);
        logic.run();
    }
}