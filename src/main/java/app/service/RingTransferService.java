package app.service;

import app.Config;
import app.model.TransferStatistics;
import app.model.User;
import app.validation.ConfigValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис запуска и сопровождения симуляции переводов.
 */
@Slf4j
public class RingTransferService {
    private final ConfigValidator configValidator = new ConfigValidator();
    private final RingBuilder ringBuilder = new RingBuilder();

    /**
     * Запускает симуляцию переводов по кругу.
     *
     * @param config конфигурация запуска
     */
    public void runSimulation(Config config) {
        configValidator.validate(config);

        List<User> users = ringBuilder.buildRing(config.getUserCount(), config.getInitialBalance());
        int initialTotalBalance = calculateTotalBalance(users);
        SimulationState simulationState = new SimulationState();
        List<Thread> threads = startTransferThreads(users, config.getTransferAmount(), simulationState);

        log.info("Симуляция запущена.");
        waitForCompletion(config.getWorkTimeSeconds());
        simulationState.stop();
        joinThreads(threads);

        TransferStatistics statistics = buildStatistics(users, initialTotalBalance);
        printStatistics(statistics);
    }

    private List<Thread> startTransferThreads(List<User> users, int transferAmount, SimulationState simulationState) {
        List<Thread> threads = new ArrayList<>();
        for (User user : users) {
            Thread thread = new Thread(new RingTransferTask(user, transferAmount, simulationState), user.getUserName() + "-thread");
            threads.add(thread);
            thread.start();
        }
        return threads;
    }

    private void waitForCompletion(int workTimeSeconds) {
        try {
            Thread.sleep(workTimeSeconds * 1000L);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Поток ожидания был прерван.", exception);
        }
    }

    private void joinThreads(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Не удалось дождаться завершения потока " + thread.getName(), exception);
            }
        }
    }

    public int calculateTotalBalance(List<User> users) {
        int totalBalance = 0;
        for (User user : users) {
            totalBalance += user.getBalance().get();
        }
        return totalBalance;
    }

    public TransferStatistics buildStatistics(List<User> users, int initialTotalBalance) {
        int finalTotalBalance = calculateTotalBalance(users);
        int totalSuccessfulTransfers = 0;
        int totalFailedTransfers = 0;

        for (User user : users) {
            totalSuccessfulTransfers += user.getSuccessfulTransfers().get();
            totalFailedTransfers += user.getFailedTransfers().get();
        }

        return TransferStatistics.builder()
                .initialTotalBalance(initialTotalBalance)
                .finalTotalBalance(finalTotalBalance)
                .totalSuccessfulTransfers(totalSuccessfulTransfers)
                .totalFailedTransfers(totalFailedTransfers)
                .users(users)
                .build();
    }

    public void printStatistics(TransferStatistics statistics) {
        log.info("Начальный общий баланс: {}", statistics.getInitialTotalBalance());
        log.info("Конечный общий баланс: {}", statistics.getFinalTotalBalance());
        log.info("Успешных переводов: {}", statistics.getTotalSuccessfulTransfers());
        log.info("Неуспешных переводов: {}", statistics.getTotalFailedTransfers());
        log.info("Изменение суммарного баланса: {}", statistics.getFinalTotalBalance() - statistics.getInitialTotalBalance());

        for (User user : statistics.getUsers()) {
            log.info("{} -> баланс: {}, успешных переводов: {}, неуспешных переводов: {}",
                    user.getUserName(),
                    user.getBalance().get(),
                    user.getSuccessfulTransfers().get(),
                    user.getFailedTransfers().get());
        }
    }
}