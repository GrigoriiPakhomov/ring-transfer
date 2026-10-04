package app;

import app.service.RingTransferService;
import lombok.extern.slf4j.Slf4j;

/**
 * Точка входа в приложение симуляции кольцевых переводов.
 */
@Slf4j
public class Main {

    /**
     * Запускает приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        ConsoleUi consoleUi = new ConsoleUi();
        Config config = consoleUi.readConfig();

        log.info("Параметры считаны: пользователей={}, время={} сек., сумма перевода={}, начальный баланс={}",
                config.getUserCount(),
                config.getWorkTimeSeconds(),
                config.getTransferAmount(),
                config.getInitialBalance());

        RingTransferService ringTransferService = new RingTransferService();
        ringTransferService.runSimulation(config);
    }
}