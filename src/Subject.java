public class Subject {
    private String name;
    private int totalTimeSpent;

    public Subject(String name) {
        this.name = name;
        this.totalTimeSpent = 0;
    }

    public void addTime(int minutes) {
        totalTimeSpent += minutes;
    }

    public String getName() {
        return name;
    }

    public int getTotalTimeSpent() {
        return totalTimeSpent;
    }
}
