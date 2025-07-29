import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RegularUser user = new RegularUser();

        System.out.println("Welcome to Smart Study Buddy!");

        while (true) {
            System.out.println("\n1. Add Task\n2. Show Tasks\n3. Start Study Sessions\n4. Subject Summary\n5. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Task Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Subject: ");
                    String subject = scanner.nextLine();
                    System.out.print("Time (in minutes): ");
                    int time = scanner.nextInt();
                    scanner.nextLine();
                    user.addTask(new Task(name, subject, time));
                    break;
                case 2:
                    user.showTasks();
                    break;
                case 3:
                    user.runPomodoroSessions();
                    break;
                case 4:
                    user.showSubjectSummary();
                    break;
                case 5:
                    System.out.println("Goodbye and happy studying!");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}