package org.PerformanceLab.task2CircleAndPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Класс CirclePointPosition для определения положения точек относительно окружности.
 */
public class SolutionCircleAndPoinTask {

    /**
     * Главный метод программы, выполняет чтение данных из файлов
     * и определяет положение точек относительно окружности.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        // Задаем пути к файлам с данными о круге и точках
        String circleFileName = "src/main/java/org/PerformanceLab/task2circle/circle.txt";
        String pointsFileName = "src/main/java/org/PerformanceLab/task2circle/points.txt";

        try {
            // Считываем данные о круге
            double[] circleData = readCircleData(circleFileName);
            double circleX = circleData[0]; // x координата центра окружности
            double circleY = circleData[1]; // y координата центра окружности
            double radius = circleData[2];   // радиус окружности

            // Считываем данные о точках и проверяем их положение относительно круга
            readPointsData(pointsFileName, circleX, circleY, radius);

        } catch (IOException e) {
            // Обработка исключений при чтении файлов
            e.printStackTrace();
        }
    }

    /**
     * Считывает данные о круге из указанного файла.
     *
     * @param filePath путь к файлу с данными о круге
     * @return массив, содержащий координаты центра и радиус круга
     * @throws IOException если происходит ошибка ввода-вывода
     */
    private static double[] readCircleData(String filePath) throws IOException {

        // Создаем BufferedReader для считывания данных из указанного файла
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        // Переменная для хранения текущей строки, считываемой из файла
        String line;
        // Создаем массив для хранения данных о круге
        double[] data = new double[3]; // Массив на 3 элемента: 0 - x, 1 - y, 2 - радиус

        // Чтение координат центра окружности
        line = reader.readLine();

        // Разделяем строку на части по пробелам для получения координат центра
        // Вместо использования массива, напрямую берем координаты из строки
        String[] center = line.split(" ");
        data[0] = Double.parseDouble(center[0]); // x координата центра
        data[1] = Double.parseDouble(center[1]); // y координата центра

        // Чтение радиуса окружности
        line = reader.readLine();
        data[2] = Double.parseDouble(line.trim()); // радиус

        reader.close(); // Закрываем reader так как мы не используем try with resourses
        return data; // Возвращаем данные о круге
    }

    /**
     * Считывает данные о точках из указанного файла и определяет
     * их положение относительно окружности.
     *
     * @param filePath путь к файлу с координатами точек
     * @param circleX x координата центра окружности
     * @param circleY y координата центра окружности
     * @param radius радиус окружности
     * @throws IOException если происходит ошибка ввода-вывода
     */
    private static void readPointsData(String filePath, double circleX, double circleY, double radius) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        // Чтение координат точек
        while ((line = reader.readLine()) != null) {
            String[] point = line.split(" ");
            double pointX = Double.parseDouble(point[0]);
            double pointY = Double.parseDouble(point[1]);

            // Вычисляем расстояние от точки до центра круга
            double distance = Math.sqrt(Math.pow((pointX - circleX), 2) + Math.pow((pointY - circleY), 2));

            // Определяем положение точки относительно круга и выводим результат
            if (distance < radius) {
                System.out.println("Точка (" + pointX + ", " + pointY + ") находится внутри окружности."); // Точка внутри
            } else if (distance == radius) {
                System.out.println("Точка (" + pointX + ", " + pointY + ") расположена на окружности."); // Точка на окружности
            } else {
                System.out.println("Точка (" + pointX + ", " + pointY + ") находится снаружи окружности."); // Точка снаружи
            }
        }

        reader.close(); // Закрываем reader
    }
}