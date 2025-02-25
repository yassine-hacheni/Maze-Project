import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordLabyrinthGUI extends JPanel {
    private Labyrinth grid;
    private int SIZEX ;
    private int SIZEY; // Example size
    private LabyrinthNode agentPosition;
    private GameScore gameScore ;
    private StringBuilder currentPath;
    private List<LabyrinthNode> currentNodePath;
    private int lenOfTrain;
    private int zero;
    private TimerGUI timGUI;
    private double highScore;

    public WordLabyrinthGUI(Labyrinth grid, LabyrinthNode currentNode , GameScore gameScore, WordTracker wt , StringBuilder currentPath, List<LabyrinthNode> currentNodePath) {
        this.gameScore = gameScore;
        this.currentNodePath = currentNodePath;
        this.currentPath = currentPath;
        this.lenOfTrain = 0;
        // this.zero = this.lenOfTrain;
        this.setPreferredSize(new Dimension(500, 500));
        this.setFocusable(true);
        System.out.println("heloooo heyyy i have "+ grid.getMaxY()+ " and also "+ grid.getMinY());
        this.SIZEY = grid.getMaxY() + Math.abs(grid.getMinY()) + 1;
        this.SIZEX = grid.getMaxX() + Math.abs(grid.getMinX()) + 1;

        this.highScore = FloatFileManager.readScore();

        timGUI = new TimerGUI(gameScore.getScore() , this.highScore);
    
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                moveAgent(e.getKeyCode() , currentNode , wt );
            }
        });
        initializeLabyrinth(grid);
   
    }

    private void initializeLabyrinth(Labyrinth grid) {
        this.grid = grid;
        agentPosition = grid.getEntry();
        repaint();
        
    }
    
            
        
    private void moveAgent(int keyCode ,LabyrinthNode currentNode, WordTracker wt) {
        int x = agentPosition.x;
        int y = agentPosition.y;
        int newX = x, newY = y;
        SwingUtilities.invokeLater(() -> this.timGUI.setScore(gameScore.getScore())); // update the score in the other interface
        System.out.println(gameScore.getScore()+"this the score");
        gameScore.incrementMoves();
        gameScore.incrementReintMoves();
        switch (keyCode) {
            case KeyEvent.VK_NUMPAD7: newX--; newY--; break; // Up-left
            case KeyEvent.VK_NUMPAD8: newX--; break; // Up
            case KeyEvent.VK_NUMPAD9: newX--; newY++; break; // Up-right
            case KeyEvent.VK_NUMPAD4: newY--; break; // Left
            case KeyEvent.VK_NUMPAD6: newY++; break; // Right
            case KeyEvent.VK_NUMPAD1: newX++; newY--; break; // Down-left
            case KeyEvent.VK_NUMPAD2: newX++; break; // Down
            case KeyEvent.VK_NUMPAD3: newX++; newY++; break; // Down-right
        }
        LabyrinthNode node = grid.getNodeMap().get(newX + "," + newY);
             
        if (newX >= grid.getMinX() && newX <= grid.getMaxX() && newY >= grid.getMinY() && newY <= grid.getMaxY()  && !node.isBlocked) {
            agentPosition =node ;
            currentPath.append(agentPosition.letter);
            currentNodePath.add(agentPosition);
            this.lenOfTrain++;
        }
        repaint();
        System.out.println("\n=== Current Game State ===");
        grid.printLabyrinth(currentNode);
        System.out.println("\nCurrent path: " + currentPath + " (length: " + currentPath.length() + ")");
        // if(System.currentTimeMillis() - startTime > timeout){
        //     System.out.println("Time out! You took too long to find the words");
        //     gameScore.displayScore();
        //     break;
        // }
        // Check if current path contains any word
        String pathString = currentPath.toString();
        Set<String> foundInCurrentPath = new HashSet<>(); // Track words found in this path
        
        for (String word : grid.getWords()) {
            int startIndex = 0;
            while ((startIndex = pathString.indexOf(word, startIndex)) != -1) {
                if (!foundInCurrentPath.contains(word)) { // Check if we haven't processed this word in current path yet
                    List<LabyrinthNode> wordPath = currentNodePath.subList(startIndex, startIndex + word.length());
                    
                    if (wt.isPathValid(word, new ArrayList<>(wordPath))) {
                        System.out.println("\n🎉 New path found for word: " + word);
                        gameScore.updateScore(word.length());
                        gameScore.ReinReintMoves(); //initialiser the moves for calculating the score for the next searched word
                        for(LabyrinthNode n : currentNodePath){
                          
                            System.out.println(n.letter);
                        }
                        for(LabyrinthNode n : wordPath){
                            n.isMot = true;
                        }
                        currentPath.setLength(0);
                        this.zero = this.lenOfTrain;
                        this.currentNodePath.clear();
                        // Reset to current position
                        currentPath.append(agentPosition.letter);
                       
                        System.out.println(wt.getTotalPaths());
                        // gameScore.updateScore(wt.getAllPossiblePaths().get(word).size(), wt.getTotalPaths());
                        // gameScore.recordWordFound(word);
                        currentPath.setLength(0); // Reset current path;
                    }
                    foundInCurrentPath.add(word); // Mark this word as found in current path
                    // gameScore.recordWordFound(word);
                }
                startIndex += 1; // Move to next possible position
            }
        }
        
        // Display current score and progress
        System.out.printf("\nMoves made: %d", gameScore.playerMoves);
        
        // Display progress for each word
        System.out.println("\nWord finding progress:");
        Map<String, Integer> progress = wt.getProgress();
        for (String word : grid.getWords()) {
            int found = progress.get(word);
            
            
            System.out.printf("'%s': found %d", word, found);
            
        }

        // Check win condition
        if (agentPosition == grid.getExit() && wt.areAllWordsComplete() || agentPosition == grid.getExit() ) {
            // System.out.println("\n🎉 Congratulations! You've found all word paths and reached the exit!");
            // if(gameScore.score == 100){
            //     System.out.println("You found all words in the minimum moves");
            // }else{
            //     System.out.printf("\nCurrent efficiency score: %.2f%%\n", gameScore.score);
            // }
            
            // gameScore.displayScore();
            
            if(highScore < gameScore.score){
                FloatFileManager.modifyScore(gameScore.score);
                timGUI.setHighScore(gameScore.score);
                timGUI.setAffiche("GAME OVER !!!! \n CONGRATS NEW SCORE UNLOCKED");
                
            }else{
                timGUI.setAffiche("GAME OVER !!!! ");
            }
            timGUI.stopTimer();
            
            SwingUtilities.getWindowAncestor(this).dispose();

            
            

        }
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = getWidth() / SIZEY;
        LabyrinthNode node;
        Map<String, LabyrinthNode> nodeMap = grid.getNodeMap();

        for (int y = grid.getMinY(); y <= grid.getMaxY(); y++) {
            for (int x = grid.getMinX(); x <= grid.getMaxX(); x++) {
                
                node = nodeMap.get(x + "," + y);
                g.setColor(node.isEqual(agentPosition) ? Color.GREEN : node.isBlocked ? Color.BLACK : node.isExit ? Color.RED : node.isMot?  Color.GREEN : Color.WHITE);
                g.fillRect((y - grid.getMinY()) * cellSize, (x - grid.getMinX()) * cellSize, cellSize, cellSize);
                // g.setColor(Color.BLACK);
                // g.drawRect(y * cellSize, x * cellSize, cellSize, cellSize);

                
                if (!node.isBlocked ) {
                    g.setColor(Color.BLUE);
                    g.drawString(String.valueOf(node.letter), (y - grid.getMinY()) * cellSize + cellSize / 2, (x - grid.getMinX()) * cellSize + cellSize / 2);
                }
            }
        }
    }
}

