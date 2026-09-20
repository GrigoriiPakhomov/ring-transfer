package app.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Итоговая статистика переводов.
 */
@Getter
@Builder
public class TransferStatistics {
    private final int initialTotalBalance;
    private final int finalTotalBalance;
    private final int totalSuccessfulTransfers;
    private final int totalFailedTransfers;
    private final List<User> users;
}