package org.example;
import java.util.Scanner;


public class Main {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Input:");
            String input = scanner.nextLine();

            String[] words = input.split("\\s+");

            for (String word : words) {
                if (!word.isEmpty()) {
                    System.out.println(word + "   " + word.length());
                }
            }
            scanner.close();
        }
    }
