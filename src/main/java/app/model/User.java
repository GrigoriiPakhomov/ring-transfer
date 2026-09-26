package app.model;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Пользователь системы переводов.
 */
@Getter
public class User {
    private final int id;
    private final String userName;
    private final AtomicInteger balance;
    private User nextUser;
    private final AtomicInteger successfulTransfers;
    private final AtomicInteger failedTransfers;

    /**
     * Создаёт пользователя с начальным балансом.
     *
     * @param id идентификатор пользователя
     * @param initialBalance начальный баланс
     */
    public User(int id, int initialBalance) {
        this.id = id;
        this.userName = "Пользователь-" + id;
        this.balance = new AtomicInteger(initialBalance);
        this.successfulTransfers = new AtomicInteger(0);
        this.failedTransfers = new AtomicInteger(0);
    }

    /**
     * Назначает следующего пользователя.
     *
     * @param nextUser следующий получатель перевода
     */
    public void setNextUser(User nextUser) {
        this.nextUser = nextUser;
    }

    /**
     * Пытается выполнить перевод следующему пользователю без блокировок.
     *
     * @param amount сумма перевода
     * @return true, если перевод выполнен успешно, иначе false
     */
    public boolean tryTransferToNext(int amount) {
        while (true) {
            int currentBalance = balance.get();

            if (currentBalance < amount) {
                failedTransfers.incrementAndGet();
                return false;
            }

            int newBalance = currentBalance - amount;
            if (balance.compareAndSet(currentBalance, newBalance)) {
                nextUser.balance.addAndGet(amount);
                successfulTransfers.incrementAndGet();
                return true;
            }
        }
    }
}