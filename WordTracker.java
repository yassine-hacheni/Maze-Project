import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

class WordTracker {
    // private Map<String, List<List<LabyrinthNode>>> allPossiblePaths; // Word -> All possible paths
    private ArrayList<String> foundwords;  // Word -> Set of found path signatures
    // private List<List<LabyrinthNode>> foundPathsList;
    private Labyrinth labyrinth;
    private BFS bfs;
    private DFS dfs;
    private int totalPaths;
    private GameScore gameScore;
    public WordTracker(Labyrinth labyrinth, GameScore gameScore) {
        this.labyrinth = labyrinth;
        // this.allPossiblePaths = new HashMap<>();
        // this.foundPaths = new HashMap<>();
        this.foundwords = new ArrayList<String>();
        this.totalPaths = 0;
        this.gameScore = gameScore;
        // this.foundPathsList = new ArrayList<>();
        // initializeFromSearch();
        //initializeFromSearch();
    }

    // public List<List<LabyrinthNode>> getFoundPathsList() {
    //     return foundPathsList;
    // }

    // public void setFoundPathsList(List<List<LabyrinthNode>> foundPathsList) {
    //     this.foundPathsList = foundPathsList;
    // }

    public void setTotalPaths(int totalPaths) {
        this.totalPaths = totalPaths;
    }

    public GameScore getGameScore() {
        return gameScore;
    }

    public void setGameScore(GameScore gameScore) {
        this.gameScore = gameScore;
    }

    public boolean areAllWordsComplete() {
        return labyrinth.getWords().size() == this.foundwords.size();
    }

    public Map<String, Integer> getProgress() {
        Map<String, Integer> progress = new HashMap<>();
        for (String word : labyrinth.getWords()) {
            String paths = foundwords.stream()
                .filter(w -> w.equals(word))
                .findFirst()
                .orElse(null);
            progress.put(word, paths != null ? 1: 0);
        }
        return progress;
    }

    public String getPathSignature(List<LabyrinthNode> path) {
        return path.stream()
            .map(n -> n.x + "," + n.y)
            .collect(Collectors.joining(";"));
    }

    public boolean isPathValid(String word, List<LabyrinthNode> path) {
       String pathSignature = getPathSignature(path);
       if(labyrinth.existPaths.get(word).equals(pathSignature)){
        this.foundwords.add(word);
        return true;

       }else{
        return false;
       }
    }

    // private void findAllWordPaths(LabyrinthNode node, List<LabyrinthNode> currentPath, String currentWord) {
    //     if (node == null || node.isBlocked || currentWord.length() > labyrinth.getMaxWordLength()) {
    //         return;
    //     }

    //     List<LabyrinthNode> newPath = new ArrayList<>(currentPath);
    //     newPath.add(node);
    //     String newWord = currentWord + node.letter;

    //     if (labyrinth.getWords().contains(newWord)) {
    //         allPossiblePaths.get(newWord).add(newPath);
    //         totalPaths++;
    //         printPath(newPath , newWord);
    //     }

    //     for (LabyrinthNode neighbor : node.neighbors) {
    //         if (!neighbor.isBlocked && !currentPath.contains(neighbor)) {
    //             findAllWordPaths(neighbor, newPath, newWord);
                
    //         }
    //     }
    //     gameScore.updateMinimumMoves();
    // }

    // private void initializeFromSearch() {
    //     for (String word : labyrinth.getWords()) {
    //         allPossiblePaths.put(word, new ArrayList<>());
    //         foundPaths.put(word, new HashSet<>());
    //     }
        
    //     // Find all possible paths for each word
    //     for (LabyrinthNode startNode : labyrinth.getNodes()) {
    //         if (!startNode.isBlocked) {
    //             this.findAllWordPaths(startNode, new ArrayList<>(), "");
    //         }
    //     }
    // }

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

    public void runBFS(BFS bfs){
        SearchResult bfsResult = bfs.searchBFS();
        this.printSearchResults(bfsResult, "BFS");
        System.out.println("BFS total moves: " + bfsResult.totalMoves);
        System.out.println("BFS words found: " + bfsResult.wordsFound.size());

    }

    public void runDFS(DFS dfs){
        SearchResult dfsResult = dfs.searchDFS();
        this.printSearchResults(dfsResult, "DFS");
        
        // Compare results
        System.out.println("\n=== Search Score ===");
        System.out.println("DFS total moves: " + dfsResult.totalMoves);
       
        System.out.println("DFS words found: " + dfsResult.wordsFound.size());
    }
    // Method to run both searches and compare results
    // public void runSearches(String x) {
        
    //     if(x.equals("1")){
    //         this.runDFS();
        
    //     }else{
    //         this.runBFS();
    //     }
        
    // }

    // public Map<String, List<List<LabyrinthNode>>> getAllPossiblePaths() {
    //     return allPossiblePaths;
    // }

    // public void setAllPossiblePaths(Map<String, List<List<LabyrinthNode>>> allPossiblePaths) {
    //     this.allPossiblePaths = allPossiblePaths;
    // }

    // public Map<String, String> getFoundPaths() {
    //     return foundPaths;
    // }

    // public void setFoundPaths(Map<String, String foundPaths) {
    //     this.foundPaths = foundPaths;
    // }

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
