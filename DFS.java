

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


class DFS {
    private Labyrinth labyrinth;
    private WordTracker wt;
    private GUIForDFS gui;

    public DFS(Labyrinth labyrinth , WordTracker wt , GUIForDFS gui ){
        this.labyrinth = labyrinth;
        this.wt = wt;
        this.gui = gui;
    }

    public SearchResult searchDFS() {
        System.out.println("hello 7");
        SearchResult result = new SearchResult();
        System.out.println("hello 8");
        Set<LabyrinthNode> visited = new HashSet<>();
        Stack<SearchState> stack = new Stack<>();
        System.out.println("hello 10");
        // Initialize with entry node
        stack.push(new SearchState(labyrinth.getEntry(), "", new ArrayList<>()));
        boolean outProcess = false;
        while (!stack.isEmpty() && !outProcess) {
            System.out.println("hello 11");
            SearchState current = stack.pop();
            LabyrinthNode node = current.node;

            if (node.isBlocked) continue; // Skip blocked nodes

            // Add node to path
            String newPath = current.path + node.letter;
            List<LabyrinthNode> newNodePath = new ArrayList<>(current.nodePath);
            newNodePath.add(node);

            // Move agent and update GUI
            System.out.println("palyer moves");
            node.isMot = true;
            SwingUtilities.invokeLater(() -> gui.movePlayerDFS(node, wt));
            if(node.isExit){
                outProcess = true;
            }
            // Introduce delay for animation
            try {
                Thread.sleep(500); // Adjust delay for smoother movement
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Check if current path forms a word
            // if (labyrinth.getWords().contains(newPath)) {
            //     result.wordsFound.add(newPath);
            //     result.wordPaths.put(newPath, new ArrayList<>(newNodePath));
            // }

            visited.add(node); // Mark node as visited
            result.totalMoves++;

            // Push unvisited neighbors onto stack
            for (LabyrinthNode neighbor : node.neighbors) {
                if (!neighbor.isBlocked && !visited.contains(neighbor)) {
                    stack.push(new SearchState(neighbor, newPath, newNodePath));
                }
            }
        }
        return result;
    }
}
































// class DFS {
//     Labyrinth labyrinth;
//     private WordTracker wt;
//     private GameScore gameScore;
//     public GUIForDFS gui ;
//     public DFS(Labyrinth labyrinth){
//         this.labyrinth = labyrinth;
//         this.gameScore = new GameScore( labyrinth );
//         this.wt = new WordTracker( labyrinth , gameScore , null , this);
//         this.gui = new GUIForDFS(labyrinth);
//     }
//     public SearchResult searchDFS() {
//         SearchResult result = new SearchResult();
//         Set<LabyrinthNode> visited = new HashSet<>();
//         Stack<SearchState> stack = new Stack<>();


        
//         // Initialize with entry node
//         stack.push(new SearchState(labyrinth.getEntry(), "", new ArrayList<>()));
        
//         while (!stack.isEmpty()) {
//             SearchState current = stack.pop();
//             LabyrinthNode node = current.node;
            
//             // Skip if node is blocked
//             if (node.isBlocked) continue;
            
//             // Add node to path
//             String newPath = current.path + node.letter;
//             List<LabyrinthNode> newNodePath = new ArrayList<>(current.nodePath);
//             newNodePath.add(node);
//             SwingUtilities.invokeLater(() -> this.gui.movePlayerDFS(node, wt, gameScore));
//             // this.gui.movePlayerDFS(node, wt, gameScore);
//             try {
//                 Thread.sleep(5000);
//             } catch (InterruptedException e) {
//                 System.out.println("Thread interrupted");
//                 Thread.currentThread().interrupt();
//             }
            
//             // Check if current path forms a word
//             if (labyrinth.getWords().contains(newPath)) {
//                 result.wordsFound.add(newPath);
//                 result.wordPaths.put(newPath, new ArrayList<>(newNodePath));
//             }
            
//             // Mark as visited
//             visited.add(node);
//             result.totalMoves++;
            
//             // Add unvisited neighbors to stack
//             for (LabyrinthNode neighbor : node.neighbors) {
//                 if (!neighbor.isBlocked && !visited.contains(neighbor)) {
//                     stack.push(new SearchState(neighbor, newPath, newNodePath));
//                 }
//             }
//         }
        
//         return result;
//     }

//     //public static void InsertLabyrinth(LabyrinthNode entry, List<String> words) {
//         //Labyrinth labyrinth = new Labyrinth();
//         //labyrinth.insertLabyrinth(entry, words);
//     //}
// }
