package org.example;

import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input size: ");
        try {
            int size = scanner.nextInt();

            float[] array = new float[size];
            Random random = new Random();

            // Заповнення масиву випадковими float-значеннями
            for (int i = 0; i < size; i++) {
                array[i] = random.nextFloat();
            }

            System.out.println("Start:");
            printArray(array);
            System.out.println();

            // Піднесення до 5-го степеня з явним приведенням до float
            for (int i = 0; i < size; i += 2) {
                array[i] = (float) Math.pow(array[i], 5); // Явне приведення типів
            }

            System.out.println("End:");
            printArray(array);
            System.out.println();

        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void printArray(float[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.printf("[%d]: %.6f", i, array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
}