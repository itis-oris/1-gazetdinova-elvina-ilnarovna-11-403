import com.oris_sem01.travelplanner.util.PasswordUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilsTest {

    @Test
    void hashAndVerifyPassword() {
        String rawPassword = "secret123";

        // хешируем пароль
        String hash = PasswordUtils.hashPassword(rawPassword);

        assertNotNull(hash, "Хеш не должен быть null");
        assertFalse(hash.isBlank(), "Хеш не должен быть пустым");

        // правильный пароль должен подходить
        assertTrue(
                PasswordUtils.verifyPassword(rawPassword, hash),
                "Правильный пароль должен проходить проверку"
        );

        // а неправильный — нет
        assertFalse(
                PasswordUtils.verifyPassword("wrong-password", hash),
                "Неправильный пароль не должен проходить проверку"
        );
    }
}

