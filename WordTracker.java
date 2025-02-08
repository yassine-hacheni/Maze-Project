import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class WordTracker {
    private Map<String, List<List<LabyrinthNode>>> allPossiblePaths; // Word -> All possible paths
    private Map<String, Set<String>> foundPaths;  // Word -> Set of found path signatures
    private Labyrinth labyrinth;
    private BFS bfs;
    private DFS dfs;
    private int totalPaths;
    private GameScore gameScore;
    public WordTracker(Labyrinth labyrinth, GameScore gameScore,BFS bfs, DFS dfs) {
        this.labyrinth = labyrinth;
        this.allPossiblePaths = new HashMap<>();
        this.foundPaths = new HashMap<>();
        this.bfs = bfs;
        this.dfs = dfs;
        this.totalPaths = 0;
        this.gameScore = gameScore;
        initializeFromSearch();
        //initializeFromSearch();
    }

    public boolean areAllWordsComplete() {
        return labyrinth.getWords().stream().allMatch(word -> 
            foundPaths.containsKey(word) && !foundPaths.get(word).isEmpty());
    }

    public Map<String, Integer> getProgress() {
        Map<String, Integer> progress = new HashMap<>();
        for (String word : labyrinth.getWords()) {
            Set<String> paths = foundPaths.get(word);
            progress.put(word, paths != null ? paths.size() : 0);
        }
        return progress;
    }

    private String getPathSignature(List<LabyrinthNode> path) {
        return path.stream()
            .map(n -> n.x + "," + n.y)
            .collect(Collectors.joining(";"));
    }

    public boolean isPathNew(String word, List<LabyrinthNode> path) {
       String pathSignature = getPathSignature(path);
        return this.foundPaths.get(word).add(pathSignature);
    }

    private void findAllWordPaths(LabyrinthNode node, List<LabyrinthNode> currentPath, String currentWord) {
        if (node == null || node.isBlocked || currentWord.length() > labyrinth.getMaxWordLength()) {
            return;
        }

        List<LabyrinthNode> newPath = new ArrayList<>(currentPath);
        newPath.add(node);
        String newWord = currentWord + node.letter;

        if (labyrinth.getWords().contains(newWord)) {
            allPossiblePaths.get(newWord).add(newPath);
            totalPaths++;
            printPath(newPath , newWord);
        }

        for (LabyrinthNode neighbor : node.neighbors) {
            if (!neighbor.isBlocked && !currentPath.contains(neighbor)) {
                findAllWordPaths(neighbor, newPath, newWord);
                
            }
        }
        gameScore.updateMinimumMoves();
    }

    private void initializeFromSearch() {
        for (String word : labyrinth.getWords()) {
            allPossiblePaths.put(word, new ArrayList<>());
            foundPaths.put(word, new HashSet<>());
        }
        
        // Find all possible paths for each word
        for (LabyrinthNode startNode : labyrinth.getNodes()) {
            if (!startNode.isBlocked) {
                this.findAllWordPaths(startNode, new ArrayList<>(), "");
            }
        }
    }

    public void printPath(List<LabyrinthNode> path , String word) {
        System.out.println("\nPath taken:" + word);
        for (LabyrinthNode node : path) {
            System.out.print("(" + node.x + "," + node.y + ":" + node.letter + ") -> ");
        }
    }


    public void printSearchResults(SearchResult result, String searchType) {
        System.out.println("\n=== " + searchType + " Search Results ===");
        System.out.println("Total moves made: " + result.totalMoves);
        System.out.println("Words found (" + result.wordsFound.size() + "/" + labyrinth.getWords().size() + "):");
        
        for (String word : result.wordsFound) {
            System.out.println("\nWord: " + word);
            System.out.println("Path taken:");
            List<LabyrinthNode> path = result.wordPaths.get(word);
            for (int i = 0; i < path.size(); i++) {
                LabyrinthNode node = path.get(i);
                System.out.print("(" + node.x + "," + node.y + ":" + node.letter + ")");
                if (i < path.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
    }


    // Method to run both searches and compare results
    public void runSearches() {
        System.out.println("\n=== Running Automated Searches ===");
        
        // Run BFS
        SearchResult bfsResult = bfs.searchBFS();
        this.printSearchResults(bfsResult, "BFS");
        
        // Run DFS
        SearchResult dfsResult = dfs.searchDFS();
        this.printSearchResults(dfsResult, "DFS");
        
        // Compare results
        System.out.println("\n=== Search Comparison ===");
        System.out.println("BFS total moves: " + bfsResult.totalMoves);
        System.out.println("DFS total moves: " + dfsResult.totalMoves);
        System.out.println("BFS words found: " + bfsResult.wordsFound.size());
        System.out.println("DFS words found: " + dfsResult.wordsFound.size());
    }

    public Map<String, List<List<LabyrinthNode>>> getAllPossiblePaths() {
        return allPossiblePaths;
    }

    public void setAllPossiblePaths(Map<String, List<List<LabyrinthNode>>> allPossiblePaths) {
        this.allPossiblePaths = allPossiblePaths;
    }

    public Map<String, Set<String>> getFoundPaths() {
        return foundPaths;
    }

    public void setFoundPaths(Map<String, Set<String>> foundPaths) {
        this.foundPaths = foundPaths;
    }

    public Labyrinth getLabyrinth() {
        return labyrinth;
    }

    public void setLabyrinth(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }

    public BFS getBfs() {
        return bfs;
    }

    public void setBfs(BFS bfs) {
        this.bfs = bfs;
    }

    public DFS getDfs() {
        return dfs;
    }

    public void setDfs(DFS dfs) {
        this.dfs = dfs;
    }

    public int getTotalPaths() {
        return totalPaths;
    }

}
