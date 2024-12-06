package org.PerformanceLab.task1CircularArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Класс SolutionTask1 решает задачу нахождения пути в круговом массиве.
 *
 * Класс запрашивает у пользователя два значения:
 * n - размер массива (количество элементов),
 * m - длина интервала для обхода массива.
 */
public class SolutionTask1 {
    public static void main(String[] args) {

        // обьявим сканнер для консольного ввода параметров n и m
        Scanner scanner = new Scanner(System.in);

        // определяем вводные данные
        System.out.println("Введите значение размера массива n: ");
        int n = scanner.nextInt();
        System.out.println("Введите длинну интервала для обхода m: ");
        int m = scanner.nextInt();

        // Получаем путь
        List<Integer> path = findPath(n, m);

        // Вывод результата
        System.out.println("Путь: " + path);

    }

    public static List<Integer> findPath(int n, int m) {
        // Инициализируем массив arr длиной n, содержащий числа от 1 до n
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i + 1; // Заполняем массив значениями от 1 до n
        }

        int current_index = 0; // Индекс текущего элемента
        List<Integer> path = new ArrayList<>(); // Список для хранения результата

        // Цикл для формирования пути
        do {
            // Добавляем элемент массива arr[current_index] в массив path
            path.add(arr[current_index]);

            // Обновляем индекс current_index, добавляя m
            current_index = (current_index + m) % n;
            
        } while (current_index != 0); // Проверка завершения

        return path; // Возвращаем массив path
    }
}