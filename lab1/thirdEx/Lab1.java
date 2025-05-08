package samples.synchronize;

import java.util.Scanner;

/**
 * Основний клас нашої програми
 * Щоб він був доданий до javadoc він має мати модифікатор доступу public
 */
public class Lab1 {

    /**
     * Точка входу будь-якої java-програми (десктоп/консоль, за винятком специфічних вирішень)
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {

        // вивід тексту на екран
        System.out.println("Привіт СІ-3 !!!");
        System.out.print("Як тебе звуть? "); // тут користувач має набрати в командному рядку стрічку зі своїм іменем
        // зчитування тексту з стандартного вводу
        Scanner scanner = new Scanner(System.in);
        // зчитування стрічки і запис у змінну name
        String name = scanner.nextLine();
        // вивід зчитаного на екран
        System.out.println("Привіт " + name);

    }
}
