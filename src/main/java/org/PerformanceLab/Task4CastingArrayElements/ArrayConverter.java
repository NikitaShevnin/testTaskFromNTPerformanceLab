package org.PerformanceLab.Task4CastingArrayElements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Класс ArrayConverter отвечает за преобразование элементов массива
 * целых чисел к одному общему числу с минимальными ходами.
 */
public class ArrayConverter {

    public static void main(String[] args) {
        // Указываем путь к файлу numbers.txt
        String filename = "src/main/java/org/PerformanceLab/Task4CastingArrayElements/numbers.txt";

        // Читаем числа из файла
        int[] nums = readNumbersFromFile(filename);

        // Проверяем, успешно ли прочитаны данные
        if (nums == null || nums.length == 0) {
            System.out.println("Ошибка при чтении данных из файла.");
            return;
        }

        // Вычисляем минимальное количество ходов
        int moves = calculateMinMoves(nums);
        System.out.println("Минимальное количество ходов: " + moves);
    }

    /**
     * Читает числа из указанного файла и возвращает их в виде массива.
     *
     * @param filename путь к файлу
     * @return массив целых чисел, считанных из файла
     */
    private static int[] readNumbersFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // Преобразуем строки в массив целых чисел
            return br.lines().mapToInt(Integer::parseInt).toArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Вычисляет минимальное количество ходов для приведения всех элементов
     * массива к медиане.
     *
     * @param nums массив целых чисел
     * @return количество ходов
     */
    private static int calculateMinMoves(int[] nums) {
        // Сортируем массив для нахождения медианы
        Arrays.sort(nums);
        int median = nums[nums.length / 2];
        System.out.println("Медиана: " + median);

        int moves = 0;

        // Отображаем изменения в массиве
        while (!allEqual(nums, median)) {
            System.out.println("Текущий массив: " + Arrays.toString(nums));
            for (int i = 0; i < nums.length; i++) {
                // Увеличиваем или уменьшаем каждый элемент массива
                if (nums[i] < median) {
                    nums[i]++;
                } else if (nums[i] > median) {
                    nums[i]--;
                }
                moves++;
            }
        }

        return moves;
    }

    /**
     * Проверяет, равны ли все элементы массива целевому значению.
     *
     * @param nums массив целых чисел
     * @param target значение, к которому нужно привести элементы
     * @return true, если все элементы равны целевому значению, иначе false
     */
    private static boolean allEqual(int[] nums, int target) {
        for (int num : nums) {
            if (num != target) {
                return false;
            }
        }
        return true;
    }
}