import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class BFS {
    Labyrinth labyrinth;
    private GUIForDFS gui;
    private WordTracker wt;
    
    public BFS(Labyrinth labyrinth , WordTracker wt , GUIForDFS gui){
        
        this.labyrinth = labyrinth;
        this.wt = wt;
        this.gui = gui;
    }
        public  SearchResult searchBFS() {
        SearchResult result = new SearchResult();
        Queue<LabyrinthNode> queue = new LinkedList<>();
        Set<LabyrinthNode> visited = new HashSet<>();
        Map<LabyrinthNode, String> paths = new HashMap<>();
        Map<LabyrinthNode, List<LabyrinthNode>> nodePaths = new HashMap<>();
        

        // Start from entry
        queue.offer(labyrinth.getEntry());
        paths.put(labyrinth.getEntry(), String.valueOf(labyrinth.getEntry().letter));
        nodePaths.put(labyrinth.getEntry(), new ArrayList<>(Arrays.asList(labyrinth.getEntry())));
        boolean outProcess = false;
        while (!queue.isEmpty() && !outProcess) {
            LabyrinthNode current = queue.poll();
            if (!visited.contains(current)) {
                visited.add(current);
                current.isMot = true;
                SwingUtilities.invokeLater(() -> gui.movePlayerDFS(current, wt));
                result.totalMoves++;

                // Check if current path forms a word
                String currentPath = paths.get(current);
                if (labyrinth.getWords().contains(currentPath)) {
                    result.wordsFound.add(currentPath);
                    result.wordPaths.put(currentPath, new ArrayList<>(nodePaths.get(current)));
                }

                // Explore neighbors
                for (LabyrinthNode neighbor : current.neighbors) {
                    if (!neighbor.isBlocked && !visited.contains(neighbor)) {
                        queue.offer(neighbor);
                        paths.put(neighbor, paths.get(current) + neighbor.letter);
                        List<LabyrinthNode> newPath = new ArrayList<>(nodePaths.get(current));
                        newPath.add(neighbor);
                        nodePaths.put(neighbor, newPath);
                        try {
                            Thread.sleep(100); 
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        
                        
                        if (neighbor.isExit) {
                            outProcess = true;
                        }

                    }
                }
            }
        }
        return result;
    }
}
