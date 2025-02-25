import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.*;
import javax.swing.JPanel;

public class GUIForDFS extends JPanel {
    private Labyrinth grid;
    private int SIZEX ;
    private int SIZEY; // Example size
    private LabyrinthNode agentPosition;
    private StringBuilder currentPath;
    private List<LabyrinthNode> currentNodePath;
    public GUIForDFS(Labyrinth grid) {
        this.currentPath = new StringBuilder();
        this.currentNodePath = new ArrayList<LabyrinthNode>();
        this.setPreferredSize(new Dimension(500, 500));
        this.setFocusable(true);
        this.SIZEY = grid.getMaxY() + Math.abs(grid.getMinY()) + 1;
        this.SIZEX = grid.getMaxX() + Math.abs(grid.getMinX()) + 1;
        // TODO Auto-generated constructor stub
        System.out.println("hello hey");
        initializeLabyrinth(grid);
    }

    private void initializeLabyrinth(Labyrinth grid){
        this.grid = grid;
        agentPosition = grid.getEntry();
        currentPath.append(agentPosition.letter);
        currentNodePath.add(agentPosition);
        System.out.println("hello hey1");

        repaint();
    }

    public void movePlayerDFS(LabyrinthNode node , WordTracker wt){
        int newX = node.x;
        int newY = node.y;
        System.out.println("hello hey 5");

        if (!node.isBlocked) {
            agentPosition =node ;
            currentPath.append(agentPosition.letter);
            currentNodePath.add(agentPosition);
        }
        repaint();
        System.out.println("\n=== Current Game State ===");
        // grid.printLabyrinth(currentNode);
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
                        for(LabyrinthNode n : currentNodePath){
                          
                            System.out.println(n.letter);
                        }
                        for(LabyrinthNode n : wordPath){
                            n.isMot = true;
                        }
                        currentPath.setLength(0);
                       
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
}


    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("hey super is overrided");
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