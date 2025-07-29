public class Task {

        private String name;
        private String subject;
        private int timeInMinutes;

        public Task(String name, String subject, int timeInMinutes) {
            this.name = name;
            this.subject = subject;
            this.timeInMinutes = timeInMinutes;
        }

        public String getName() {
            return name;
        }

        public String getSubject() {
            return subject;
        }

        public int getTimeInMinutes() {
            return timeInMinutes;
        }

    @Override
    public String toString() {
        return "Task: " + name + " | Subject: " + subject + " | Duration: " + timeInMinutes + " mins";
    }



}
