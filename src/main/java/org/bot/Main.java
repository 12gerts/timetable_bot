package org.bot;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Logic logic = new Logic();
        String startMessage = logic.getHello();
        System.out.println(startMessage);
        Scanner input = new Scanner(System.in);

        while (true) {
            String command = input.nextLine();
            String message = logic.getReport(command);
            System.out.println(message);
        }
    }
}