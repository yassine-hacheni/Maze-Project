import java.io.*;
import java.util.*;
import java.util.stream.Collectors;








// Helper class for Dijkstra's algorithm


// Method to display the shortest path


    


    
    // Helper class to store search state   
    




// class Main {
//     public static void main(String[] args) {
//         Scanner scanner = new Scanner(System.in);
//         System.out.println("Choose difficulty (easy/medium/hard): ");
//         String difficulty = scanner.nextLine().trim();
        
//         Labyrinth labyrinth = new Labyrinth(difficulty);
//         labyrinth.play();
//     }
// }

class Main {
    public static void main(String[] args) {
        
        
        // System.out.println("Choose mode:");
        // System.out.println("1. Play game");
        // System.out.println("2. Run automated searches (BFS/DFS)");
        // System.out.println("3. Show shortest path to exit");
        
        // String choice = scanner.nextLine().trim();
        // if (choice.equals("1")) {
        //     gamePlay.play();
        // } else if (choice.equals("2")) {
        //     wt.runSearches();
        // }else if(choice.equals("3")){
        //     djickastra.displayShortestPathToExit();
        // }
        new WordMazeGameGUI();
    }
}