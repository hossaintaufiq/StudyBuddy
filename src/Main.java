import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RegularUser user = new RegularUser();

        System.out.println("Welcome to Smart Study Buddy!");

        while (true) {
            System.out.println();
            System.out.println("\n1. Add a New Task");
            System.out.println("2. Show All Tasks");
            System.out.println("3. Start Pomodoro Sessions");
            System.out.println("4. View Study Summary");
            System.out.println("5. Mark Task Completed");  // new option
            System.out.println("6. Show Total Study Time");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
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
                    user.showTasks();
                    System.out.print("Enter task number to mark as completed: ");
                    int taskNum = scanner.nextInt();
                    scanner.nextLine();
                    user.markTaskCompleted(taskNum - 1);  // list shown starting from 1
                    break;
                case 6:
                    int totalTime = user.getTotalStudyTime();
                    System.out.println("[Total Study Time] " + totalTime + " minutes");
                    break;
                case 7:
                    System.out.println("Thank you for using Smart Study Buddy. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}