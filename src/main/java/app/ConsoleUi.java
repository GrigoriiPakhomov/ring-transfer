package app;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * Консольный интерфейс для чтения параметров симуляции.
 */
@Slf4j
public class ConsoleUi {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Считывает параметры запуска из консоли.
     *
     * @return корректная конфигурация симуляции
     */
    public Config readConfig() {
        int userCount = readPositiveInt("Введите количество пользователей (не меньше 2): ", 2);
        int workTimeSeconds = readPositiveInt("Введите время работы в секундах: ", 1);
        int transferAmount = readPositiveInt("Введите сумму перевода: ", 1);
        int initialBalance = readPositiveInt("Введите начальный баланс каждого пользователя: ", 0);

        return new Config(userCount, workTimeSeconds, transferAmount, initialBalance);
    }

    /**
     * Считывает целое число и проверяет нижнюю границу.
     *
     * @param prompt текст приглашения
     * @param minValue минимально допустимое значение
     * @return корректное значение
     */
    private int readPositiveInt(String prompt, int minValue) {
        while (true) {
            log.info(prompt);
            String rawValue = scanner.nextLine();

            try {
                int value = Integer.parseInt(rawValue.trim());
                if (value < minValue) {
                    log.error("Значение должно быть не меньше {}. Повторите ввод.", minValue);
                    continue;
                }
                return value;
            } catch (NumberFormatException exception) {
                log.error("Введено нецелое число: '{}'. Повторите ввод.", rawValue);
            }
        }
    }
}