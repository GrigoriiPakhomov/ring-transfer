package app.service;

import app.model.User;

/**
 * Задача потока, который выполняет переводы от имени одного пользователя.
 */
public class RingTransferTask implements Runnable {
    private final User user;
    private final int transferAmount;
    private final SimulationState simulationState;

    /**
     * Создаёт задачу перевода для пользователя.
     *
     * @param user пользователь-отправитель
     * @param transferAmount сумма одного перевода
     * @param simulationState общее состояние симуляции
     */
    public RingTransferTask(User user, int transferAmount, SimulationState simulationState) {
        this.user = user;
        this.transferAmount = transferAmount;
        this.simulationState = simulationState;
    }

    /**
     * Выполняет переводы, пока симуляция активна.
     */
    @Override
    public void run() {
        while (simulationState.isRunning()) {
            user.tryTransferToNext(transferAmount);
        }
    }
}