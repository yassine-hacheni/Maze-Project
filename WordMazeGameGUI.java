import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.*;

public class WordMazeGameGUI extends JFrame {

    private JPanel mainPanel, difficultyPanel, gameModePanel;
    private CardLayout cardLayout;
    
    private JButton easyButton, mediumButton, hardButton;
    private JButton playButton, searchButton, dijkstraButton;
    private GameScore gameScore;
    private GamePlay gamePlay;
    private WordTracker wt;
    private Djickastra djickastra;
    private String selectedDifficulty;
    private Labyrinth labyrinth;

    public WordMazeGameGUI() {
        

        setTitle("Word Maze Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create Difficulty Selection Panel
        difficultyPanel = new JPanel(new GridLayout(3, 1));
        easyButton = new JButton("Easy");
        mediumButton = new JButton("Medium");
        hardButton = new JButton("Hard");

        easyButton.addActionListener(e -> setDifficulty("Easy"));
        mediumButton.addActionListener(e -> setDifficulty("Medium"));
        hardButton.addActionListener(e -> setDifficulty("Hard"));

        difficultyPanel.add(new JLabel("Choose Difficulty:"));
        difficultyPanel.add(easyButton);
        difficultyPanel.add(mediumButton);
        difficultyPanel.add(hardButton);
        
        // Create Game Mode Selection Panel
        gameModePanel = new JPanel(new GridLayout(4, 1));
        playButton = new JButton("Play Game");
        searchButton = new JButton("Run Searches");
        dijkstraButton = new JButton("Find Shortest Path");

        playButton.addActionListener(e -> startGame()); // Hide panel after choosing mode});
        searchButton.addActionListener(e -> runSearches());
        dijkstraButton.addActionListener(e -> findShortestPath());

        gameModePanel.add(new JLabel("Game Mode (Difficulty: Not Selected)")); // This will be updated later
        gameModePanel.add(playButton);
        gameModePanel.add(searchButton);
        gameModePanel.add(dijkstraButton);


        // Add Panels to Main Panel
        mainPanel.add(difficultyPanel, "DifficultySelection");
        mainPanel.add(gameModePanel, "GameModeSelection");

        add(mainPanel);
        setVisible(true);
    }

    private void startGame() {
        gamePlay.play();
        dispose();
    }

    private void runSearches() {
       // Create a new JFrame for algorithm selection
        dispose();
        JFrame searchFrame = new JFrame("Choose Search Algorithm");
        searchFrame.setSize(300, 150);
        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setLayout(new GridLayout(2, 1));

        // Create buttons for DFS and BFS selection
        JButton dfsButton = new JButton("DFS (Depth-First Search)");
        JButton bfsButton = new JButton("BFS (Breadth-First Search)");

        // Add action listeners
        dfsButton.addActionListener(e -> {
            searchFrame.dispose();
            searchFrame.dispose();
            GUIForDFS gui = new GUIForDFS(labyrinth);
            JFrame frame = new JFrame("Word Labyrinth DFS Show");
            frame.add(gui);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            DFS dfs = new DFS(this.labyrinth , this.wt ,  gui);
           
            new Thread(() -> {
                wt.runDFS(dfs);  // Call DFS method
            }).start();
        });

        bfsButton.addActionListener(e -> {
            searchFrame.dispose();
            GUIForDFS gui = new GUIForDFS(labyrinth);
            JFrame frame = new JFrame("Word Labyrinth BFS Show");
            frame.add(gui);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            BFS bfs = new BFS(this.labyrinth , this.wt , gui);
            new Thread(() -> {
                wt.runBFS(bfs);  // Call BFS method
            }).start();
           
            
        });

        // Add buttons to the frame
        searchFrame.add(dfsButton);
        searchFrame.add(bfsButton);

        searchFrame.setVisible(true);

        }

        private void findShortestPath() {
            djickastra.displayShortestPathToExit();
            dispose();
        }

        private void setDifficulty(String difficulty) {
            this.labyrinth = new Labyrinth(difficulty);
            this.djickastra = new Djickastra(labyrinth);
            
            this.gameScore = new GameScore(labyrinth);
            this.wt = new WordTracker(labyrinth, gameScore);
            this.gamePlay = new GamePlay(labyrinth, gameScore, wt);
            this.selectedDifficulty = difficulty;
            ((JLabel) gameModePanel.getComponent(0)).setText("Game Mode (Difficulty: " + difficulty + ")");
            cardLayout.show(mainPanel, "GameModeSelection"); // Switch to game mode selection
        }
    }
