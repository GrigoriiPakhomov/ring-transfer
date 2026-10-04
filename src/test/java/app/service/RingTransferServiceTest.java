package app.service;

import app.model.TransferStatistics;
import app.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RingTransferServiceTest {

    @Test
    @DisplayName("Рассчитывает общий баланс, который должен возвращать корректную сумму")
    void testCalculateTotalBalance() {
        RingTransferService ringTransferService = new RingTransferService();
        RingBuilder ringBuilder = new RingBuilder();

        List<User> users = ringBuilder.buildRing(4, 100);

        int totalBalance = ringTransferService.calculateTotalBalance(users);

        assertEquals(400, totalBalance);
    }

    @Test
    @DisplayName("Собирает статистику, которая должна сохранять общий баланс неизменным")
    void testBuildStatistics() {
        RingTransferService ringTransferService = new RingTransferService();
        RingBuilder ringBuilder = new RingBuilder();

        List<User> users = ringBuilder.buildRing(3, 100);
        users.get(0).tryTransferToNext(10);
        users.get(1).tryTransferToNext(20);

        TransferStatistics statistics = ringTransferService.buildStatistics(users, 300);

        assertEquals(300, statistics.getInitialTotalBalance());
        assertEquals(300, statistics.getFinalTotalBalance());
        assertEquals(2, statistics.getTotalSuccessfulTransfers());
    }
}