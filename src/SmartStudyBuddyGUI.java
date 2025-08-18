import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SmartStudyBuddyGUI {

    private JFrame frame;
    private JPanel mainPanel, loginPanel, dashboardPanel, contentPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private RegularUser user;
    private final UserManager userManager = new UserManager();

    private JTable taskTable;
    private DefaultTableModel taskTableModel;
    private JTextArea pomodoroOutput;

    // Colors
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);   // blue
    private final Color SECONDARY_COLOR = new Color(236, 240, 241); // light gray
    private final Color HEADER_COLOR = new Color(41, 128, 185);    // darker blue

    public SmartStudyBuddyGUI() {
        // Main frame
        frame = new JFrame("Smart Study Buddy");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new CardLayout());
        frame.add(mainPanel);

        createLoginPanel();
        createDashboardPanel();

        frame.setVisible(true);
        switchPanel("Login");
    }

    /** ---------------- LOGIN PANEL ---------------- */
    private void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(SECONDARY_COLOR);

        JLabel title = new JLabel("📘 Smart Study Buddy", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(HEADER_COLOR);
        header.add(title);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLbl = new JLabel("Email:");
        JLabel passLbl = new JLabel("Password:");
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.setBackground(PRIMARY_COLOR);
        loginBtn.setForeground(Color.WHITE);
        registerBtn.setBackground(PRIMARY_COLOR);
        registerBtn.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; form.add(emailLbl, gbc);
        gbc.gridx = 1; form.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(passLbl, gbc);
        gbc.gridx = 1; form.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(loginBtn, gbc);
        gbc.gridx = 1; form.add(registerBtn, gbc);

        loginPanel.add(header, BorderLayout.NORTH);
        loginPanel.add(form, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());

        mainPanel.add(loginPanel, "Login");
    }

    /** ---------------- DASHBOARD PANEL ---------------- */
    private void createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());

        // Header
        JLabel title = new JLabel("📘 Smart Study Buddy Dashboard", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        JPanel header = new JPanel();
        header.setBackground(HEADER_COLOR);
        header.add(title);

        // Left menu
        JPanel menuPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        menuPanel.setBackground(SECONDARY_COLOR);

        JButton addTaskBtn = createMenuButton("➕ Add Task");
        JButton showTaskBtn = createMenuButton("📋 Refresh Tasks");
        JButton markCompletedBtn = createMenuButton("✅ Mark Completed");
        JButton deleteTaskBtn = createMenuButton("🗑️ Delete Task");
        JButton pomodoroBtn = createMenuButton("⏳ Start Pomodoro");
        JButton summaryBtn = createMenuButton("📊 Study Summary");
        JButton totalTimeBtn = createMenuButton("⏱️ Total Study Time");
        JButton logoutBtn = createMenuButton("🚪 Logout");

        menuPanel.add(addTaskBtn);
        menuPanel.add(showTaskBtn);
        menuPanel.add(markCompletedBtn);
        menuPanel.add(deleteTaskBtn);
        menuPanel.add(pomodoroBtn);
        menuPanel.add(summaryBtn);
        menuPanel.add(totalTimeBtn);
        menuPanel.add(logoutBtn);

        // Right content area
        contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Task Table
        taskTableModel = new DefaultTableModel(new String[]{"#", "Task", "Subject", "Time (mins)", "Status"}, 0);
        taskTable = new JTable(taskTableModel);
        JScrollPane taskScroll = new JScrollPane(taskTable);

        // Pomodoro output
        pomodoroOutput = new JTextArea(6, 40);
        pomodoroOutput.setEditable(false);
        pomodoroOutput.setBorder(BorderFactory.createTitledBorder("Pomodoro Progress"));
        JScrollPane pomodoroScroll = new JScrollPane(pomodoroOutput);

        contentPanel.add(taskScroll, BorderLayout.CENTER);
        contentPanel.add(pomodoroScroll, BorderLayout.SOUTH);

        // Add to dashboard
        dashboardPanel.add(header, BorderLayout.NORTH);
        dashboardPanel.add(menuPanel, BorderLayout.WEST);
        dashboardPanel.add(contentPanel, BorderLayout.CENTER);

        // Button actions
        addTaskBtn.addActionListener(e -> addTask());
        showTaskBtn.addActionListener(e -> refreshTasks());
        markCompletedBtn.addActionListener(e -> markTaskCompleted());
        deleteTaskBtn.addActionListener(e -> deleteTask());
        pomodoroBtn.addActionListener(e -> runPomodoro());
        summaryBtn.addActionListener(e -> showSummary());
        totalTimeBtn.addActionListener(e -> showTotalTime());
        logoutBtn.addActionListener(e -> logout());

        mainPanel.add(dashboardPanel, "Dashboard");
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        return btn;
    }

    /** ---------------- HELPER METHODS ---------------- */
    private void switchPanel(String name) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, name);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        try {
            User u = userManager.login(email, password);
            user = new RegularUser(u.getEmail(), pomodoroOutput);
            refreshTasks();
            switchPanel("Dashboard");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void register() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        try {
            userManager.register(email, password);
            JOptionPane.showMessageDialog(frame, "Registration successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTasks() {
        taskTableModel.setRowCount(0);
        List<Task> tasks = FileManager.loadTasks(user.getEmail());
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            taskTableModel.addRow(new Object[]{i + 1, t.getName(), t.getSubject(), t.getTimeMinutes(), t.isCompleted() ? "Done" : "Pending"});
        }
    }

    private void addTask() {
        JTextField nameField = new JTextField();
        JTextField subjectField = new JTextField();
        JTextField timeField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Task Name:")); panel.add(nameField);
        panel.add(new JLabel("Subject:")); panel.add(subjectField);
        panel.add(new JLabel("Time (mins):")); panel.add(timeField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String subject = subjectField.getText().trim();
                int time = Integer.parseInt(timeField.getText().trim());
                user.addTask(new Task(name, subject, time));
                refreshTasks();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void markTaskCompleted() {
        int row = taskTable.getSelectedRow();
        if (row >= 0) {
            try {
                user.markTaskCompleted(row);
                refreshTasks();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteTask() {
        int row = taskTable.getSelectedRow();
        if (row >= 0) {
            try {
                user.deleteTask(row);
                refreshTasks();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void runPomodoro() {
        pomodoroOutput.setText("");
        startPomodoroSession(1, 25, 5, 4); // session=1, work=25, break=5, totalSessions=4
    }

    private void startPomodoroSession(int session, int workTime, int breakTime, int totalSessions) {
        appendPomodoroLog("Session " + session + " started! Work for " + workTime + " minutes...");

        countdown(workTime, "Work", () -> {
            appendPomodoroLog("Break time! Take " + breakTime + " minutes.");
            countdown(breakTime, "Break", () -> {
                if (session < totalSessions) {
                    startPomodoroSession(session + 1, workTime, breakTime, totalSessions);
                } else {
                    appendPomodoroLog("✅ All Pomodoro sessions completed!");
                }
            });
        });
    }

    private void countdown(int minutes, String label, Runnable onFinish) {
        final int[] secondsLeft = {minutes * 60};
        Timer timer = new Timer(1000, e -> {
            int min = secondsLeft[0] / 60;
            int sec = secondsLeft[0] % 60;
            pomodoroOutput.setText(label + " time: " + String.format("%02d:%02d", min, sec));
            secondsLeft[0]--;

            if (secondsLeft[0] < 0) {
                ((Timer) e.getSource()).stop();
                onFinish.run();
            }
        });
        timer.start();
    }

    private void appendPomodoroLog(String text) {
        pomodoroOutput.append(text + "\n");
    }



    private void showSummary() {
        StringBuilder sb = new StringBuilder("Study Summary:\n");
        for (Subject s : user.getSubjects()) {
            sb.append("- ").append(s.getName())
                    .append(": ").append(s.getTotalTimeSpent()).append(" mins\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showTotalTime() {
        JOptionPane.showMessageDialog(frame, "Total Study Time: " + user.getTotalStudyTime() + " mins");
    }

    private void logout() {
        user = null;
        switchPanel("Login");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartStudyBuddyGUI::new);
    }
}
