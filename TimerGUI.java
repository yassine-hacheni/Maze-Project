import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class TimerGUI extends JFrame {
    private JLabel numberLabel;
    private JLabel timerLabel;
    private JLabel afficheLabel;
    private JLabel highScoreLabel;
    private int secondsElapsed = 0; // Timer starts from 00:00:00
    private double score;
    private String affiche;
    private boolean stoptimer;
    private Timer timer; // Timer reference
    private double highScore;

    public TimerGUI(double score , double highScore) {
        this.stoptimer = false;
        this.affiche = "";
        this.score = score;
        this.highScore = highScore;
        setTitle("Score & Timer Display");
        setSize(300, 250); // Increased height to fit the affiche label
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1)); // 4 rows to fit the affiche label

        // Score Display
        numberLabel = new JLabel("Score: " + String.format("%.2f", this.score), SwingConstants.CENTER);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 20));

        highScoreLabel = new JLabel("High Score: " + String.format("%.2f", this.highScore) , SwingConstants.CENTER);
        numberLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        // Timer Display (Start at 00:00:00)
        timerLabel = new JLabel("Time: 00:00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Affiche Label (Bottom)
        afficheLabel = new JLabel(this.affiche, SwingConstants.CENTER);
        afficheLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Timer - Updates Every Second
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!stoptimer) {
                    secondsElapsed++; // Increment time
                    timerLabel.setText("Time: " + formatTime(secondsElapsed));
                }
            }
        });
        timer.start();

        // Add Components
        add(numberLabel);
        add(highScoreLabel);
        add(timerLabel);
        add(afficheLabel); // Added affiche label at the bottom

        setVisible(true);
    }

    // Method to Update Score
    public void setScore(double score) {
        this.score = score;
        numberLabel.setText("Score: " + String.format("%.2f", this.score)); // Update label
    }

    // Method to Update Affiche Label
    public void setAffiche(String newText) {
        this.affiche = newText;
        afficheLabel.setText(this.affiche); // Update label dynamically
    }

    // Stop the Timer
    public void stopTimer() {
        this.stoptimer = true;
    }

    // Convert Seconds to HH:MM:SS Format
    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, sec);
    }

    public double getHighScore() {
        return highScore;
    }

    public void setHighScore(double highScore) {
        this.highScore = highScore;
        numberLabel.setText("High Score: " + String.format("%.2f", this.highScore)); // Update label
    }

    
}

