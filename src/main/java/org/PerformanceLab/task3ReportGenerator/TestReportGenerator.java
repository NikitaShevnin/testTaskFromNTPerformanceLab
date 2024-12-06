package org.PerformanceLab.task3ReportGenerator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Этот класс генерирует отчет о тестах, сопоставляя результаты из двух JSON-файлов
 * и записывая итоговый отчет в третий JSON-файл.
 */
public class TestReportGenerator {

    public static void main(String[] args) {

        // Задание путей к файлам
        String valuesFilePath = "src/main/java/org/PerformanceLab/task3/values.json";
        String testsFilePath = "src/main/java/org/PerformanceLab/task3/tests.json";
        String reportFilePath = "src/main/java/org/PerformanceLab/task3/report.json";

        try {
            // 1. Чтение результатов тестов из values.json
            Map<Integer, String> values = readValuesFromJson(valuesFilePath);

            // 2. Чтение структуры из tests.json
            JsonObject testsJson = readJsonFromFile(testsFilePath);

            // 3. Сопоставление результатов с идентификаторами тестов и создание отчета
            JsonObject reportJson = generateReport(testsJson, values);

            // 4. Запись результата в report.json
            writeJsonToFile(reportJson, reportFilePath);

            // Подтверждение успешного создания отчета
            System.out.println("Report generated successfully: " + reportFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage()); // Обработка ошибок ввода-вывода
        }
    }

    /**
     * Читает результаты тестов из указанного JSON-файла.
     *
     * @param filePath путь к файлу values.json.
     * @return карта уникальных идентификаторов тестов и их значений.
     * @throws IOException если произойдет ошибка ввода-вывода.
     */
    private static Map<Integer, String> readValuesFromJson(String filePath) throws IOException {
        Gson gson = new Gson(); // Создание экземпляра Gson для парсинга JSON
        JsonObject valuesJson = gson.fromJson(new FileReader(filePath), JsonObject.class);
        Map<Integer, String> valuesMap = new HashMap<>(); // Карта для хранения значений тестов

        // Получение массива значений из JSON
        JsonArray valuesArray = valuesJson.getAsJsonArray("values");
        for (JsonElement element : valuesArray) {
            JsonObject valueObject = element.getAsJsonObject();
            // Заполнение карты результатами тестов
            valuesMap.put(valueObject.get("id").getAsInt(), valueObject.get("value").getAsString());
        }
        return valuesMap; // Возврат карты значений
    }

    /**
     * Читает и возвращает JSON-объект из указанного файла.
     *
     * @param filePath путь к JSON-файлу.
     * @return JsonObject, прочитанный из файла.
     * @throws IOException если произойдет ошибка ввода-вывода.
     */
    private static JsonObject readJsonFromFile(String filePath) throws IOException {
        JsonParser parser = new JsonParser(); // Создание парсера для чтения JSON
        return parser.parse(new FileReader(filePath)).getAsJsonObject(); // Чтение и парсинг JSON
    }

    /**
     * Генерирует отчет, сопоставляя идентификаторы тестов и их результаты.
     *
     * @param testsJson JSON-объект структуры тестов.
     * @param values карта с результатами тестов.
     * @return JsonObject, содержащий собранный отчет.
     */
    private static JsonObject generateReport(JsonObject testsJson, Map<Integer, String> values) {
        JsonObject reportJson = new JsonObject(); // Создание нового JSON-объекта для отчета
        JsonArray testsArray = testsJson.getAsJsonArray("tests");
        JsonArray reportArray = new JsonArray(); // Массив для хранения отчетных тестов

        // Заполнение отчета данными из тестов
        for (JsonElement testElement : testsArray) {
            JsonObject testObject = testElement.getAsJsonObject();
            // Получение значения для корневых тестов
            testObject.addProperty("value", getValueFromMap(testObject, values));
            // Сопоставление и добавление дочерних элементов
            addToReport(testObject, reportArray, values);
        }

        reportJson.add("tests", reportArray); // Добавление массива тестов в отчет
        return reportJson; // Возврат собранного отчета
    }

    /**
     * Рекурсивно добавляет тесты и их значения в отчет.
     *
     * @param testObject текущее тестовое значение.
     * @param reportArray массив, в который добавляются отчетные тесты.
     * @param values карта с результатами тестов.
     */
    private static void addToReport(JsonObject testObject, JsonArray reportArray, Map<Integer, String> values) {
        JsonObject reportTestObject = testObject.deepCopy(); // Копирование текущего тестового объекта в отчет
        int id = reportTestObject.get("id").getAsInt(); // Получение идентификатора теста

        // Если значение найдено, заполняем его
        if (values.containsKey(id)) {
            reportTestObject.addProperty("value", values.get(id));
        }

        // Если есть вложенные значения
        if (testObject.has("values")) {
            JsonArray childTests = testObject.getAsJsonArray("values");
            JsonArray reportChildTestsArray = new JsonArray(); // Массив для хранения дочерних отчетных тестов
            // Рекурсивно добавляем дочерние тесты
            for (JsonElement childElement : childTests) {
                addToReport(childElement.getAsJsonObject(), reportChildTestsArray, values);
            }
            reportTestObject.add("values", reportChildTestsArray); // Добавляем вложенные значения в отчете
        }

        reportArray.add(reportTestObject); // Добавляем текущий объект теста в массив отчета
    }

    /**
     * Получает значение для теста по его ID.
     *
     * @param testObject объект теста.
     * @param values карта с результатами тестов.
     * @return строковое значение результата теста.
     */
    private static String getValueFromMap(JsonObject testObject, Map<Integer, String> values) {
        int id = testObject.get("id").getAsInt(); // Получение идентификатора теста
        return values.getOrDefault(id, ""); // Возврат значения, если оно есть, иначе пустая строка
    }

    /**
     * Записывает указанный JSON-объект в файл.
     *
     * @param json JSON-объект, который нужно записать.
     * @param filePath путь к файлу, в который будет записан JSON.
     * @throws IOException если произойдет ошибка ввода-вывода.
     */
    private static void writeJsonToFile(JsonObject json, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Создание gson с красивым выводом
            gson.toJson(json, writer); // Запись JSON в файл
        }
    }
}