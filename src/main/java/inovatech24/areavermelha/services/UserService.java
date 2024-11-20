package inovatech24.areavermelha.services;

import com.fasterxml.jackson.core.type.TypeReference;
import inovatech24.areavermelha.domain.User;
import inovatech24.areavermelha.jsonmanager.JsonFileUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final List<User> users;
    private final AtomicLong counter = new AtomicLong(1);
    private static final String FILE_PATH = "C:\\Users\\pcamp\\OneDrive\\Documentos\\JavaProjects\\Area-Vermelha-main\\data\\user.json";

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public UserService() throws IOException {
        this.users = new ArrayList<>(JsonFileUtil.loadDataFromFile(FILE_PATH, new TypeReference<>() {}));
        if(!users.isEmpty()) {
            counter.set(users.stream().mapToLong(User::getId).max().orElse(1) + 1);
        }
    }

    public User registerUser(User user) throws IOException {
        user.setId(counter.getAndIncrement());
        users.add(user);
        JsonFileUtil.saveDataToFile(users, FILE_PATH);
        return user;
    }

    public Optional<User> loginUser(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                .findFirst();
    }
}