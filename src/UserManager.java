// File: UserManager.java
import java.io.*;
import java.util.*;

public class UserManager {
    private static String fileName = "users.txt";
    private Map<String, User> users;

    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    // Load users from file
    private void loadUsers() {
        File file = new File(fileName);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String email = parts[0];
                    String password = parts[1];
                    users.put(email, new User(email, password));
                }
            }
        } catch (IOException e) {
            System.out.println("[Error] Failed to load users: " + e.getMessage());
        }
    }

    // Save users to file
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (User user : users.values()) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("[Error] Failed to save users: " + e.getMessage());
        }
    }

    // Register new user
//    public boolean register(String email, String password) {
//        if (users.containsKey(email)) {
//            System.out.println("[Error] Email already registered.");
//            return false;
//        }
//        User user = new User(email, password);
//        users.put(email, user);
//        saveUsers();
//        System.out.println("[Info] Registration successful!");
//        return true;
//    }
//
//    // Login user
//    public User login(String email, String password) {
//        if (!users.containsKey(email)) {
//            System.out.println("[Error] Email not registered.");
//            return null;
//        }
//        User user = users.get(email);
//        if (!user.getPassword().equals(password)) {
//            System.out.println("[Error] Incorrect password.");
//            return null;
//        }
//        System.out.println("[Info] Login successful!");
//        return user;
//    }
    public boolean register(String email, String password) throws UserAlreadyExistsException {
        if (users.containsKey(email)) {
            throw new UserAlreadyExistsException("Email already registered: " + email);
        }
        User user = new User(email, password);
        users.put(email, user);
        saveUsers();
        System.out.println("[Info] Registration successful!");
        return true;
    }

    public User login(String email, String password) throws UserNotFoundException {
        if (!users.containsKey(email)) {
            throw new UserNotFoundException("Email not registered: " + email);
        }
        User user = users.get(email);
        if (!user.getPassword().equals(password)) {
            throw new UserNotFoundException("Incorrect password for email: " + email);
        }
        System.out.println("[Info] Login successful!");
        return user;
    }

}
