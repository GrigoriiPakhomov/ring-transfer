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
}