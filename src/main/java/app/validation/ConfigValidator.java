package app.validation;

import app.Config;

/**
 * Валидатор пользовательской конфигурации.
 */
public class ConfigValidator {

    /**
     * Проверяет корректность конфигурации.
     *
     * @param config конфигурация запуска
     */
    public void validate(Config config) {
        if (config == null) {
            throw new IllegalArgumentException("Конфигурация не должна быть null.");
        }
        if (config.getUserCount() < 2) {
            throw new IllegalArgumentException("Количество пользователей должно быть не меньше 2.");
        }
        if (config.getWorkTimeSeconds() <= 0) {
            throw new IllegalArgumentException("Время работы должно быть больше 0.");
        }
        if (config.getTransferAmount() <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше 0.");
        }
        if (config.getInitialBalance() < 0) {
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным.");
        }
    }
}