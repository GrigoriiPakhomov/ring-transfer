package app.service;

import lombok.Getter;

/**
 * Состояние жизненного цикла симуляции.
 */
@Getter
public class SimulationState {
    private volatile boolean running = true;

    /**
     * Возвращает признак активности симуляции.
     *
     * @return true, если симуляция продолжается
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Останавливает симуляцию.
     */
    public void stop() {
        this.running = false;
    }
}