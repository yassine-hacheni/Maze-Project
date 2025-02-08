import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SearchResult {
    public List<String> wordsFound;
    public int totalMoves;
    public Map<String, List<LabyrinthNode>> wordPaths;

    public SearchResult() {
        this.wordsFound = new ArrayList<>();
        this.totalMoves = 0;
        this.wordPaths = new HashMap<>();
    }

    // Method to print search results
   
}
