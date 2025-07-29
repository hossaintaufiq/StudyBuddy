import java.util.*;
public class RegularUser implements TaskManager {
    private List<Task> tasks = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    private PomodoroTimer timer = new StandardPomodoroTimer();

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
        if (!subjectExists) {
            subjects.add(new Subject(task.getSubject()));
        }

        System.out.println("[Task Added] " + task);
    }


    @Override
    public void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("[Info] No tasks added yet.");
        } else {
            System.out.println("\n[Your Task List]");
            for (int i=0;i<tasks.size();i++) {
                System.out.println("["+(i+1)+"]"+"- " + tasks.get(i));
            }
        }
    }

    @Override
    public void runPomodoroSessions() {
        if (tasks.isEmpty()) {
            System.out.println("[Info] No tasks to run Pomodoro sessions.");
            return;
        }

        for (Task task : tasks) {
            System.out.println("\n[Starting Task] " + task.getName());
            int sessionCount = task.getTimeInMinutes() / 25;
            for (int i = 0; i < sessionCount; i++) {
                System.out.println("[Session] " + (i + 1) + "/" + sessionCount);
                timer.startSession();
                timer.takeBreak();
            }

            // Find the matching subject and add time
            for (Subject s : subjects) {
                if (s.getName().equalsIgnoreCase(task.getSubject())) {
                    s.addTime(task.getTimeInMinutes());
                    break;
                }
            }
        }
    }

//    task complete method
             public void markTaskCompleted(int taskIndex) {
                if (taskIndex >= 0 && taskIndex < tasks.size()) {
              Task t = tasks.get(taskIndex);
               t.setCompleted(true);
               System.out.println("[Update] Task marked as completed: " + t.getName());
             } else {
              System.out.println("[Error] Invalid task index!");
             }
        }
// totoal studytime
public int getTotalStudyTime() {
    int total = 0;
    for (Task t : tasks) {
        total += t.getTimeInMinutes();
    }
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