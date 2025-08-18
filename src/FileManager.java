import java.io.*;
import java.util.*;

public class FileManager {

    public static String getTaskFileName(String email) {
        // replace special chars to make a valid filename
        return "tasks_" + email.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
    }

    public static void saveTasks(String email, List<Task> tasks) {
        String fileName = getTaskFileName(email);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Task task : tasks) {
                writer.write(task.getName() + "," + task.getSubject() + "," + task.getTimeInMinutes() + "," + task.isCompleted());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("[Error] Failed to save tasks: " + e.getMessage());
        }
    }

    public static List<Task> loadTasks(String email) {
        String fileName = getTaskFileName(email);
        List<Task> tasks = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return tasks;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Task t = new Task(parts[0], parts[1], Integer.parseInt(parts[2]));
                    t.setCompleted(Boolean.parseBoolean(parts[3]));
                    tasks.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("[Error] Failed to load tasks: " + e.getMessage());
        }
        return tasks;
    }
}
