import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SmartStudyBuddyGUI {
    private JFrame frame;
    private JPanel loginPanel, dashboardPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private RegularUser user;
    private UserManager userManager = new UserManager();

    // Pomodoro messages display
    private JTextArea pomodoroOutput;
    private JScrollPane pomodoroScroll;

    public SmartStudyBuddyGUI() {
        frame = new JFrame("Smart Study Buddy");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new CardLayout());

        pomodoroOutput = new JTextArea(10, 40);
        pomodoroOutput.setEditable(false);
        pomodoroScroll = new JScrollPane(pomodoroOutput);

        createLoginPanel();
        frame.add(loginPanel, "Login");
        frame.setVisible(true);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(5, 2, 10, 10));

        emailField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginBtn);
        loginPanel.add(registerBtn);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());
    }

    private void createDashboardPanel() {
        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new GridLayout(9, 1, 5, 5));

        JButton addTaskBtn = new JButton("Add Task");
        JButton showTaskBtn = new JButton("Show Tasks");
        JButton markCompletedBtn = new JButton("Mark Completed");
        JButton deleteTaskBtn = new JButton("Delete Task");
        JButton pomodoroBtn = new JButton("Start Pomodoro");
        JButton summaryBtn = new JButton("View Study Summary");
        JButton totalTimeBtn = new JButton("Total Study Time");
        JButton logoutBtn = new JButton("Logout");

        dashboardPanel.add(addTaskBtn);
        dashboardPanel.add(showTaskBtn);
        dashboardPanel.add(markCompletedBtn);
        dashboardPanel.add(deleteTaskBtn);
        dashboardPanel.add(pomodoroBtn);
        dashboardPanel.add(summaryBtn);
        dashboardPanel.add(totalTimeBtn);
        dashboardPanel.add(logoutBtn);
        dashboardPanel.add(pomodoroScroll);

        // Button Actions
        addTaskBtn.addActionListener(e -> addTask());
        showTaskBtn.addActionListener(e -> showTasks());
        markCompletedBtn.addActionListener(e -> markTaskCompleted());
        deleteTaskBtn.addActionListener(e -> deleteTask());
        pomodoroBtn.addActionListener(e -> runPomodoro());
        summaryBtn.addActionListener(e -> showSummary());
        totalTimeBtn.addActionListener(e -> showTotalTime());
        logoutBtn.addActionListener(e -> logout());

        frame.add(dashboardPanel, "Dashboard");
        switchPanel("Dashboard");
    }

    private void switchPanel(String name) {
        CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
        cl.show(frame.getContentPane(), name);
    }

    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        try {
            User u = userManager.login(email, password);
            user = new RegularUser(u.getEmail(), pomodoroOutput); // GUI constructor
            createDashboardPanel();
        } catch (UserNotFoundException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void register() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        try {
            userManager.register(email, password);
            JOptionPane.showMessageDialog(frame, "Registration Successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (UserAlreadyExistsException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------- Task Methods ----------------

    private void addTask() {
        JTextField nameField = new JTextField();
        JTextField subjectField = new JTextField();
        JTextField timeField = new JTextField();
        Object[] fields = {
                "Task Name:", nameField,
                "Subject:", subjectField,
                "Time (minutes):", timeField
        };
        int option = JOptionPane.showConfirmDialog(frame, fields, "Add New Task", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String subject = subjectField.getText();
                int time = Integer.parseInt(timeField.getText());
                user.addTask(new Task(name, subject, time));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid time input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showTasks() {
        List<Task> tasks = FileManager.loadTasks(user.getEmail());
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No tasks added.", "Tasks", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            sb.append("[").append(i + 1).append("] ").append(tasks.get(i)).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "Tasks", JOptionPane.INFORMATION_MESSAGE);
    }

    private void markTaskCompleted() {
        String input = JOptionPane.showInputDialog(frame, "Enter task number to mark completed:");
        if (input != null) {
            try {
                int index = Integer.parseInt(input) - 1;
                user.markTaskCompleted(index);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidTaskException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteTask() {
        String input = JOptionPane.showInputDialog(frame, "Enter task number to delete:");
        if (input != null) {
            try {
                int index = Integer.parseInt(input) - 1;
                user.deleteTask(index);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidTaskException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void runPomodoro() {
        pomodoroOutput.setText(""); // clear previous messages
        user.runPomodoroSessions();
        JOptionPane.showMessageDialog(frame, "Pomodoro sessions completed!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSummary() {
        List<Subject> subjects = user.getSubjects();
        if (subjects.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No summary available.", "Study Summary", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Subject s : subjects) {
            sb.append("- Subject: ").append(s.getName())
                    .append(" | Total Time: ").append(s.getTotalTimeSpent()).append(" mins\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "Study Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showTotalTime() {
        JOptionPane.showMessageDialog(frame, "Total Study Time: " + user.getTotalStudyTime() + " minutes", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        user = null;
        switchPanel("Login");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartStudyBuddyGUI::new);
    }
}
