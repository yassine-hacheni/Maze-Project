import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

class DFS {
    Labyrinth labyrinth;
    public DFS(Labyrinth labyrinth){
        this.labyrinth = labyrinth;
    }
    public SearchResult searchDFS() {
        SearchResult result = new SearchResult();
        Set<LabyrinthNode> visited = new HashSet<>();
        Stack<SearchState> stack = new Stack<>();
        
        // Initialize with entry node
        stack.push(new SearchState(labyrinth.getEntry(), "", new ArrayList<>()));
        
        while (!stack.isEmpty()) {
            SearchState current = stack.pop();
            LabyrinthNode node = current.node;
            
            // Skip if node is blocked
            if (node.isBlocked) continue;
            
            // Add node to path
            String newPath = current.path + node.letter;
            List<LabyrinthNode> newNodePath = new ArrayList<>(current.nodePath);
            newNodePath.add(node);
            
            // Check if current path forms a word
            if (labyrinth.getWords().contains(newPath)) {
                result.wordsFound.add(newPath);
                result.wordPaths.put(newPath, new ArrayList<>(newNodePath));
            }
            
            // Mark as visited
            visited.add(node);
            result.totalMoves++;
            
            // Add unvisited neighbors to stack
            for (LabyrinthNode neighbor : node.neighbors) {
                if (!neighbor.isBlocked && !visited.contains(neighbor)) {
                    stack.push(new SearchState(neighbor, newPath, newNodePath));
                }
            }
        }
        
        return result;
    }

    //public static void InsertLabyrinth(LabyrinthNode entry, List<String> words) {
        //Labyrinth labyrinth = new Labyrinth();
        //labyrinth.insertLabyrinth(entry, words);
    //}
}
