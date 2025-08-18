import javax.swing.*;

public class StandardPomodoroTimer extends PomodoroTimer {
    private JTextArea output;

    public StandardPomodoroTimer(JTextArea outputArea) {
        this.output = outputArea;
    }

    @Override
    public void startSession() {
        output.append("Starting 25-minute focus session...\n");
    }

    @Override
    public void takeBreak() {
        output.append("Taking 5-minute break...\n");
    }
}
