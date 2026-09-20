package app.validation;

import app.Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigValidatorTest {

    @Test
    @DisplayName("Проверяет конфигурацию и выбрасывает исключение, если пользователей меньше двух")
    void testValidateWhenUserCountIsTooSmall() {
        ConfigValidator configValidator = new ConfigValidator();

        assertThrows(IllegalArgumentException.class,
                () -> configValidator.validate(new Config(1, 5, 10, 100)));
    }

    @Test
    @DisplayName("Проверяет конфигурацию и выбрасывает исключение, если время работы некорректное")
    void testValidateWhenWorkTimeIsInvalid() {
        ConfigValidator configValidator = new ConfigValidator();

        assertThrows(IllegalArgumentException.class,
                () -> configValidator.validate(new Config(2, 0, 10, 100)));
    }

    @Test
    @DisplayName("Проверяет конфигурацию и не выбрасывает исключение при корректных данных")
    void testValidateWhenConfigIsCorrect() {
        ConfigValidator configValidator = new ConfigValidator();

        assertDoesNotThrow(() -> configValidator.validate(new Config(2, 5, 10, 100)));
    }
}