package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public User createUser(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    public boolean isUserWithEmailExist(String email) {
        return getAllUsers().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean isUserExists(Long userId) {
        return getUserById(userId).isPresent();
    }

}
