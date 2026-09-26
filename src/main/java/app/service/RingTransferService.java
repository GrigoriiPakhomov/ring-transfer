package app.service;

import app.Config;
import app.model.TransferStatistics;
import app.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Сервис запуска и сопровождения симуляции кольцевых переводов.
 */
@Slf4j
public class RingTransferService {
    private final RingBuilder ringBuilder = new RingBuilder();

    /**
     * Запускает симуляцию переводов по кольцу.
     *
     * @param config конфигурация запуска
     */
    public void runSimulation(Config config) {
        config.validate();

        List<User> users = ringBuilder.buildRing(config.getUserCount(), config.getInitialBalance());
        int initialTotalBalance = calculateTotalBalance(users);
        SimulationState simulationState = new SimulationState();
        ExecutorService executorService = Executors.newFixedThreadPool(config.getUserCount());
        List<CompletableFuture<Void>> tasks = startTransferTasks(
                users,
                config.getTransferAmount(),
                simulationState,
                executorService
        );

        log.info("Симуляция запущена.");
        waitForCompletion(config.getWorkTimeSeconds());
        simulationState.stop();
        waitForTasks(tasks);
        shutdownExecutor(executorService);

        TransferStatistics statistics = buildStatistics(users, initialTotalBalance);
        printStatistics(statistics);
    }

    /**
     * Запускает задачу перевода для каждого пользователя.
     *
     * @param users список пользователей
     * @param transferAmount сумма перевода
     * @param simulationState общее состояние симуляции
     * @param executorService пул потоков для выполнения задач
     * @return список асинхронных задач
     */
    private List<CompletableFuture<Void>> startTransferTasks(
            List<User> users,
            int transferAmount,
            SimulationState simulationState,
            ExecutorService executorService
    ) {
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        for (User user : users) {
            CompletableFuture<Void> task = CompletableFuture.runAsync(
                    new RingTransferTask(user, transferAmount, simulationState),
                    executorService
            );
            tasks.add(task);
        }
        return tasks;
    }

    /**
     * Ожидает завершения времени работы симуляции.
     *
     * @param workTimeSeconds время ожидания в секундах
     */
    private void waitForCompletion(int workTimeSeconds) {
        try {
            Thread.sleep(workTimeSeconds * 1000L);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Поток ожидания был прерван.", exception);
        }
    }

    /**
     * Дожидается завершения всех асинхронных задач.
     *
     * @param tasks список асинхронных задач
     */
    private void waitForTasks(List<CompletableFuture<Void>> tasks) {
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                tasks.toArray(new CompletableFuture[0])
        );
        allTasks.join();
    }

    /**
     * Корректно завершает пул потоков.
     *
     * @param executorService пул потоков
     */
    private void shutdownExecutor(ExecutorService executorService) {
        executorService.shutdown();
    }

    /**
     * Вычисляет суммарный баланс пользователей.
     *
     * @param users список пользователей
     * @return общий баланс
     */
    public int calculateTotalBalance(List<User> users) {
        int totalBalance = 0;
        for (User user : users) {
            totalBalance += user.getBalance().get();
        }
        return totalBalance;
    }

    /**
     * Собирает статистику завершённой симуляции.
     *
     * @param users список пользователей
     * @param initialTotalBalance начальный общий баланс
     * @return объект со статистикой
     */
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

    /**
     * Выводит итоговую статистику симуляции.
     *
     * @param statistics итоговая статистика
     */
    public void printStatistics(TransferStatistics statistics) {
        log.info("Начальный общий баланс: {}", statistics.getInitialTotalBalance());
        log.info("Конечный общий баланс: {}", statistics.getFinalTotalBalance());
        log.info("Успешных переводов: {}", statistics.getTotalSuccessfulTransfers());
        log.info("Неуспешных переводов: {}", statistics.getTotalFailedTransfers());
        log.info("Изменение суммарного баланса: {}",
                statistics.getFinalTotalBalance() - statistics.getInitialTotalBalance());

        for (User user : statistics.getUsers()) {
            log.info("{} -> баланс: {}, успешных переводов: {}, неуспешных переводов: {}",
                    user.getUserName(),
                    user.getBalance().get(),
                    user.getSuccessfulTransfers().get(),
                    user.getFailedTransfers().get());
        }
    }
}