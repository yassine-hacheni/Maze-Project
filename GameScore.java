import java.util.HashMap;
import java.util.Map;

class GameScore {
    Labyrinth labyrinth;
    Djickastra djickastra;
    int playerMoves;
    // int minimumMoves;
    double score;
    int reintMoves;
    Map<String, Integer> wordFoundCounts;
    
    public GameScore(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
        this.playerMoves = 0;
        //djickastra.findShortestPathToExit();
        // this.minimumMoves = djickastra.getTotalMoves();
        this.reintMoves = 0;
        this.wordFoundCounts = new HashMap<>();
        for (String word : labyrinth.getWords()) {
            wordFoundCounts.put(word, 0);
        }
    }
    
    // public void updateMinimumMoves(){
    //     this.minimumMoves++;
    // }
    public void incrementMoves() {
        this.playerMoves++;
    }

    public void incrementReintMoves() {
        this.reintMoves++;
    }

    public void ReinReintMoves() {
        this.reintMoves = 0;
    }
    
    // public void recordWordFound(String word) {
    //     wordFoundCounts.put(word, wordFoundCounts.get(word) + 1);
    // }
    
    public void updateScore(int wordLength) {
        this.score += (double) (wordLength * 100) / (labyrinth.getWords().size() * this.reintMoves);
        System.out.println("score updated "+ this.score);
    }
    
    public void displayScore() {
        System.out.println("\n=== Final Score ===");
        // System.out.printf("Minimum possible moves: %d\n", minimumMoves);
        System.out.printf("Your total moves: %d\n", playerMoves);
        System.out.printf("Efficiency score: %.2f%%\n", score);
        
        System.out.println("\nWord Finding Statistics:");
        for (Map.Entry<String, Integer> entry : wordFoundCounts.entrySet()) {
            System.out.printf("'%s': found %d times\n", entry.getKey(), entry.getValue());
        }
    }

    public double getScore(){
        return this.score;
    }
}