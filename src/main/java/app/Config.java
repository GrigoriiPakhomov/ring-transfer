package app;

import lombok.Getter;

/**
 * Конфигурация параметров запуска симуляции переводов.
 */
@Getter
public class Config {
    private final int userCount;
    private final int workTimeSeconds;
    private final int transferAmount;
    private final int initialBalance;

    /**
     * Создаёт объект конфигурации.
     *
     * @param userCount количество пользователей
     * @param workTimeSeconds время работы симуляции в секундах
     * @param transferAmount сумма одного перевода
     * @param initialBalance начальный баланс каждого пользователя
     */
    public Config(int userCount, int workTimeSeconds, int transferAmount, int initialBalance) {
        this.userCount = userCount;
        this.workTimeSeconds = workTimeSeconds;
        this.transferAmount = transferAmount;
        this.initialBalance = initialBalance;
    }

    /**
     * Проверяет корректность конфигурации.
     */
    public void validate() {
        if (userCount < 2) {
            throw new IllegalArgumentException("Количество пользователей должно быть не меньше 2.");
        }
        if (workTimeSeconds <= 0) {
            throw new IllegalArgumentException("Время работы должно быть больше 0.");
        }
        if (transferAmount <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше 0.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным.");
        }
    }
}