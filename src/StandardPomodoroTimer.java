//import javax.swing.*;
//
//public class StandardPomodoroTimer extends PomodoroTimer {
//    private JTextArea output;
//
//    public StandardPomodoroTimer(JTextArea outputArea) {
//        this.output = outputArea;
//    }
//
//    @Override
//    public void startSession() {
//        output.append("Starting 25-minute focus session...\n");
//    }
//
//    @Override
//    public void takeBreak() {
//        output.append("Taking 5-minute break...\n");
//    }
//}
// StandardPomodoroTimer.java
import java.util.function.Consumer;

public class StandardPomodoroTimer extends PomodoroTimer {
    private final Consumer<String> output;

    public StandardPomodoroTimer(Consumer<String> output) {
        this.output = output;
    }

    @Override
    public void startSession() {
        output.accept("Work session started. Focus for 25 minutes.");
        try {
            // Simulate work session (25 minutes)
            Thread.sleep(1500); // 1.5 seconds for testing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        output.accept("Work session ended. Take a short break.");
    }

    @Override
    public void takeBreak() {
        output.accept("Break started. 5 minutes.");
        try {
            // Simulate break (5 minutes)
            Thread.sleep(1000); // 1 second for testing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        output.accept("Break ended. Back to work!");
    }
}