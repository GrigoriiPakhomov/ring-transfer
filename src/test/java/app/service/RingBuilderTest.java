package app.service;

import app.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class RingBuilderTest {

    @Test
    @DisplayName("Создаёт круг пользователей, где последний ссылается на первого")
    void testBuildRing() {
        RingBuilder ringBuilder = new RingBuilder();

        List<User> users = ringBuilder.buildRing(3, 100);

        assertEquals(3, users.size());
        assertSame(users.get(1), users.get(0).getNextUser());
        assertSame(users.get(2), users.get(1).getNextUser());
        assertSame(users.get(0), users.get(2).getNextUser());
    }
}