package app.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Переводит деньги следующему пользователю, если баланса достаточно")
    void testTryTransferToNextWhenBalanceIsEnough() {
        User firstUser = new User(0, 100);
        User secondUser = new User(1, 100);
        firstUser.setNextUser(secondUser);

        boolean result = firstUser.tryTransferToNext(10);

        assertTrue(result);
        assertEquals(90, firstUser.getBalance().get());
        assertEquals(110, secondUser.getBalance().get());
        assertEquals(1, firstUser.getSuccessfulTransfers().get());
        assertEquals(0, firstUser.getFailedTransfers().get());
    }

    @Test
    @DisplayName("Не переводит деньги следующему пользователю, если баланса недостаточно")
    void testTryTransferToNextWhenBalanceIsNotEnough() {
        User firstUser = new User(0, 5);
        User secondUser = new User(1, 100);
        firstUser.setNextUser(secondUser);

        boolean result = firstUser.tryTransferToNext(10);

        assertFalse(result);
        assertEquals(5, firstUser.getBalance().get());
        assertEquals(100, secondUser.getBalance().get());
        assertEquals(0, firstUser.getSuccessfulTransfers().get());
        assertEquals(1, firstUser.getFailedTransfers().get());
    }
}