package app.service;

import app.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Создаёт круг пользователей.
 */
public class RingBuilder {

    /**
     * Создаёт пользователей и связывает их по кругу.
     *
     * @param userCount количество пользователей
     * @param initialBalance начальный баланс каждого пользователя
     * @return список пользователей
     */
    public List<User> buildRing(int userCount, int initialBalance) {
        List<User> users = new ArrayList<>();
        for (int index = 0; index < userCount; index++) {
            users.add(new User(index, initialBalance));
        }

        for (int index = 0; index < users.size(); index++) {
            User currentUser = users.get(index);
            User nextUser = users.get((index + 1) % users.size());
            currentUser.setNextUser(nextUser);
        }

        return users;
    }
}