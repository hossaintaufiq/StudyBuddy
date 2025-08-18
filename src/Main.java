import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();
        RegularUser user = null;

        System.out.println("Welcome to Smart Study Buddy!");

        // Login/Register loop
        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter Email: ");
                String email = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();
                userManager.register(email, password);

            } else if (choice == 2) {
                System.out.print("Enter Email: ");
                String email = scanner.nextLine();
                System.out.print("Enter Password: ");
                String password = scanner.nextLine();
                User u = userManager.login(email, password);
                if (u != null) {
                    user = new RegularUser(u.getEmail());
                    break;
                }

            } else if (choice == 3) {
                System.out.println("Goodbye!");
                return;

            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        // Task menu
        while (true) {
            System.out.println();
            System.out.println("1. Add a New Task");
            System.out.println("2. Show All Tasks");
            System.out.println("3. Start Pomodoro Sessions");
            System.out.println("4. View Study Summary");
            System.out.println("5. Mark Task Completed");
            System.out.println("6. Show Total Study Time");
            System.out.println("7. Delete a Task");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Task Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Subject: ");
                    String subject = scanner.nextLine();
                    System.out.print("Time (in minutes): ");
                    int time = scanner.nextInt();
                    scanner.nextLine();
                    user.addTask(new Task(name, subject, time));
                }
                case 2 -> user.showTasks();
                case 3 -> user.runPomodoroSessions();
                case 4 -> user.showSubjectSummary();
                case 5 -> {
                    user.showTasks();
                    System.out.print("Enter task number to mark as completed: ");
                    int taskNum = scanner.nextInt();
                    scanner.nextLine();
                    user.markTaskCompleted(taskNum - 1);
                }
                case 6 -> System.out.println("[Total Study Time] " + user.getTotalStudyTime() + " minutes");
                case 7 -> {
                    user.showTasks();
                    System.out.print("Enter task number to delete: ");
                    int delNum = scanner.nextInt();
                    scanner.nextLine();
                    user.deleteTask(delNum - 1);
                }
                case 8 -> {
                    System.out.println("Thank you for using Smart Study Buddy. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
