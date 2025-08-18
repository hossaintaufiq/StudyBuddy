public class Task {

        private String name;
        private String subject;
        private int timeInMinutes;
        private boolean isCompleted;  // new field

        public Task(String name, String subject, int timeInMinutes) {
            this.name = name;
            this.subject = subject;
            this.timeInMinutes = timeInMinutes;
            this.isCompleted = false;
        }
    public int getTimeMinutes() {
        return timeInMinutes;
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
        public boolean isCompleted() {
        return isCompleted;
       }

       public void setCompleted(boolean completed) {
          isCompleted = completed;
       }

    @Override
    public String toString() {
        String status = isCompleted ? "[Completed]" : "[Pending]";
        return status + " Task: " + name + " | Subject: " + subject + " | Duration: " + timeInMinutes + " mins";
    }


}
