import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;

class GamePlay {
    Labyrinth labyrinth;
    WordTracker wt;    
    
    LabyrinthNode currentNode ;
    StringBuilder currentPath ;
    List<LabyrinthNode> currentNodePath;
    GameScore gameScore ;
   
    public GamePlay(Labyrinth labyrinth , GameScore gameScore , WordTracker wordTracker) {
        this.labyrinth = labyrinth;
        this.wt = wordTracker;
        currentNode = labyrinth.getEntry();
        currentPath = new StringBuilder();
        currentNodePath = new ArrayList<>();
        this.gameScore = gameScore;
    }

    public void play() {
        
        // Scanner scanner = new Scanner(System.in);
        // Initialize with starting node
        currentPath.append(labyrinth.getEntry().letter);
        currentNodePath.add(labyrinth.getEntry());
        
        System.out.println("\n=== Welcome to the Word Labyrinth! ===");
        System.out.println("Use numpad keys for movement:");
        System.out.println("7 8 9\n4 5 6\n1 2 3");
        System.out.println("\nWords to find: " + labyrinth.getWords());
        // System.out.printf("\nMinimum moves required to reach exit: %d\n", gameScore.minimumMoves);
        long startTime = System.currentTimeMillis();
        JFrame frame = new JFrame("Word Labyrinth");
        labyrinth.printLabyrinth(currentNode);

        WordLabyrinthGUI panel = new WordLabyrinthGUI(labyrinth, currentNode, gameScore, wt, currentPath, currentNodePath);
        // for the GUI 
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // *************
        // while (true) {
        //     System.out.println("\n=== Current Game State ===");
        //     labyrinth.printLabyrinth(currentNode);
        //     System.out.println("\nCurrent path: " + currentPath + " (length: " + currentPath.length() + ")");
        //     // if(System.currentTimeMillis() - startTime > timeout){
        //     //     System.out.println("Time out! You took too long to find the words");
        //     //     gameScore.displayScore();
        //     //     break;
        //     // }
        //     // Check if current path contains any word
        //     String pathString = currentPath.toString();
        //     Set<String> foundInCurrentPath = new HashSet<>(); // Track words found in this path
            
        //     for (String word : labyrinth.getWords()) {
        //         int startIndex = 0;
        //         while ((startIndex = pathString.indexOf(word, startIndex)) != -1) {
        //             if (!foundInCurrentPath.contains(word)) { // Check if we haven't processed this word in current path yet
        //                 List<LabyrinthNode> wordPath = currentNodePath.subList(startIndex, startIndex + word.length());
                        
        //                 if (wt.isPathNew(word, new ArrayList<>(wordPath))) {
        //                     System.out.println("\n🎉 New path found for word: " + word);
        //                     System.out.println(wt.getTotalPaths());
        //                     gameScore.updateScore(wt.getAllPossiblePaths().get(word).size(), wt.getTotalPaths());
        //                     gameScore.recordWordFound(word);
        //                     currentPath.setLength(0); // Reset current path;
        //                 }
        //                 foundInCurrentPath.add(word); // Mark this word as found in current path
        //                 gameScore.recordWordFound(word);
        //             }
        //             startIndex += 1; // Move to next possible position
        //         }
        //     }
            
        //     // Display current score and progress
        //     System.out.printf("\nMoves made: %d", gameScore.playerMoves);
            
        //     // Display progress for each word
        //     System.out.println("\nWord finding progress:");
        //     Map<String, Integer> progress = wt.getProgress();
        //     for (String word : labyrinth.getWords()) {
        //         int found = progress.get(word);
        //         int total = wt.getAllPossiblePaths().get(word).size();
        //         if(total != 0){
        //             System.out.printf("'%s': found %d/%d paths\n", word, found, total);
        //         }
                
        //     }

        //     // Check win condition
        //     if (currentNode == labyrinth.getExit() && wt.areAllWordsComplete()) {
        //         System.out.println("\n🎉 Congratulations! You've found all word paths and reached the exit!");
        //         if(gameScore.score == 100){
        //             System.out.println("You found all words in the minimum moves");
        //         }else{
        //             System.out.printf("\nCurrent efficiency score: %.2f%%\n", gameScore.score);
        //         }
                
        //         gameScore.displayScore();
        //         break;
        //     }

        //     System.out.println("\nEnter move (1-9, except 5) or 'r' to reset path or 'q' to quit: ");
        //     String input = scanner.nextLine().trim();
            
        //     if (input.equalsIgnoreCase("q")) {
        //         gameScore.displayScore();
        //         break;
        //     }

        //     if (input.equalsIgnoreCase("r")) {
        //         System.out.println("\n🔄 Resetting current path...");
        //         currentPath.setLength(0);
        //         currentNodePath.clear();
        //         // Reset to current position
        //         currentPath.append(currentNode.letter);
        //         currentNodePath.add(currentNode);
        //         continue;
        //     }
            
        //     try {
        //         int moveKey = Integer.parseInt(input);
        //         if (moveKey == 5) {
        //             System.out.println("Invalid move! 5 is not a valid direction.");
        //             continue;
        //         }
                
        //         if (Labyrinth.DIRECTION_MAP.containsKey(moveKey)) {
        //             int[] dir = Labyrinth.DIRECTION_MAP.get(moveKey);
        //             final LabyrinthNode currentPos = currentNode;
                    
        //             LabyrinthNode nextNode = labyrinth.getNodes().stream()
        //                 .filter(n -> n.x == currentPos.x + dir[0] && n.y == currentPos.y + dir[1])
        //                 .findFirst()
        //                 .orElse(null);

        //             if (nextNode != null && !nextNode.isBlocked && currentPos.neighbors.contains(nextNode)) {
        //                 gameScore.incrementMoves(); // Increment moves counter
                       
        //                 currentNode = nextNode;
        //                 currentPath.append(currentNode.letter);
        //                 currentNodePath.add(currentNode);
        //             } else {
        //                 System.out.println("Invalid move! Cannot move in that direction.");
        //             }
        //         } else {
        //             System.out.println("Invalid move number!");
        //         }
        //     } catch (NumberFormatException e) {
        //         System.out.println("Invalid input! Please enter a number, 'r' to reset, or 'q' to quit.");
        //     }
        // }
    }

    public void searchFromNode(LabyrinthNode node , StringBuilder currentPath , Set<LabyrinthNode> visited , Map<String, Integer> wordOccurrences) {
    if (node == null || node.isBlocked || currentPath.length() > labyrinth.getMaxWordLength()) {
            return;
        }

        // Add current node to path
        visited.add(node);
        currentPath.append(node.letter);

        // Check if current path is a word we're looking for
        if (labyrinth.getWords().contains(currentPath.toString())) {
            wordOccurrences.put(currentPath.toString(), wordOccurrences.get(currentPath.toString()) + 1);
        }

        // Continue search through neighbors
        for (LabyrinthNode neighbor : currentNode.neighbors) {
            if (!neighbor.isBlocked && !visited.contains(neighbor)) {
                this.searchFromNode(neighbor, currentPath, new HashSet<>(visited), wordOccurrences);
            }
        }
    }

    public Map<String, Integer> findAllWordOccurrences() {
        Map<String, Integer> wordOccurrences = new HashMap<>();
        for (String word : labyrinth.getWords()) {
            wordOccurrences.put(word, 0);
        }

        // For each node as a starting point
        for (LabyrinthNode startNode : labyrinth.getNodes()) {
            if (!startNode.isBlocked) {
                this.searchFromNode(startNode, new StringBuilder(), new HashSet<>(), wordOccurrences);
            }
        }
        return wordOccurrences;
    }
}
