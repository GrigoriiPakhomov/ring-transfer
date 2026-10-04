package app.service;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Состояние жизненного цикла симуляции.
 */
@Getter
public class SimulationState {
    private final AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Возвращает признак активности симуляции.
     *
     * @return true, если симуляция продолжается
     */
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Останавливает симуляцию.
     */
    public void stop() {
        running.set(false);
    }
}