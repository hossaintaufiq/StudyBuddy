import java.util.*;

public class RegularUser implements TaskManager {
    private String userEmail;
    private List<Task> tasks;
    private List<Subject> subjects;
    private PomodoroTimer timer = new StandardPomodoroTimer();

    public RegularUser(String email) {
        this.userEmail = email;
        this.tasks = FileManager.loadTasks(email);
        this.subjects = new ArrayList<>();

        // initialize subjects from tasks
        for (Task t : tasks) {
            boolean exists = false;
            for (Subject s : subjects) {
                if (s.getName().equalsIgnoreCase(t.getSubject())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) subjects.add(new Subject(t.getSubject()));
        }
    }

    @Override
    public void addTask(Task task) {
        tasks.add(task);

        boolean subjectExists = false;
        for (Subject s : subjects) {
            if (s.getName().equalsIgnoreCase(task.getSubject())) {
                subjectExists = true;
                break;
            }
        }
        if (!subjectExists) subjects.add(new Subject(task.getSubject()));

        FileManager.saveTasks(userEmail, tasks);
        System.out.println("[Task Added] " + task);
    }

    @Override
    public void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("[Info] No tasks added yet.");
            return;
        }
        System.out.println("\n[Your Task List]");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + tasks.get(i));
        }
    }

    @Override
    public void runPomodoroSessions() {
        if (tasks.isEmpty()) {
            System.out.println("[Info] No tasks to run Pomodoro sessions.");
            return;
        }

        for (Task task : tasks) {
            if (task.isCompleted()) continue;

            System.out.println("\n[Starting Task] " + task.getName());
            int sessionCount = task.getTimeInMinutes() / 25;
            for (int i = 0; i < sessionCount; i++) {
                System.out.println("[Session] " + (i + 1) + "/" + sessionCount);
                timer.startSession();
                timer.takeBreak();
            }

            // add time to corresponding subject
            for (Subject s : subjects) {
                if (s.getName().equalsIgnoreCase(task.getSubject())) {
                    s.addTime(task.getTimeInMinutes());
                    break;
                }
            }
        }
    }

//    public void markTaskCompleted(int index) {
//        if (index < 0 || index >= tasks.size()) {
//            System.out.println("[Error] Invalid task index!");
//            return;
//        }
//        tasks.get(index).setCompleted(true);
//        FileManager.saveTasks(userEmail, tasks);
//        System.out.println("[Update] Task marked as completed: " + tasks.get(index).getName());
//    }
    public void markTaskCompleted(int index) throws InvalidTaskException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskException("Invalid task index: " + (index + 1));
        }
        tasks.get(index).setCompleted(true);
        FileManager.saveTasks(userEmail, tasks);
        System.out.println("[Update] Task marked as completed: " + tasks.get(index).getName());
    }

//    public void deleteTask(int index) {
//        if (index < 0 || index >= tasks.size()) {
//            System.out.println("[Error] Invalid task index!");
//            return;
//        }
//        Task removed = tasks.remove(index);
//        FileManager.saveTasks(userEmail, tasks);
//        System.out.println("[Update] Task deleted: " + removed.getName());
//    }
    public void deleteTask(int index) throws InvalidTaskException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidTaskException("Invalid task index: " + (index + 1));
        }
        Task removed = tasks.remove(index);
        FileManager.saveTasks(userEmail, tasks);
        System.out.println("[Update] Task deleted: " + removed.getName());
    }


    public int getTotalStudyTime() {
        int total = 0;
        for (Task t : tasks) total += t.getTimeInMinutes();
        return total;
    }

    public void showSubjectSummary() {
        if (subjects.isEmpty()) {
            System.out.println("[Info] No study summary available.");
            return;
        }
        System.out.println("\n[Study Summary by Subject]");
        for (Subject s : subjects) {
            System.out.println("- Subject: " + s.getName() + " | Total Time: " + s.getTotalTimeSpent() + " minutes");
        }
    }
}
