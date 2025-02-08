
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

class Labyrinth {
    // Movement mapping using numpad layout
    public static final Map<Integer, int[]> DIRECTION_MAP = new HashMap<>() {{
        put(7, new int[]{-1, -1}); // up-left
        put(8, new int[]{-1, 0});  // up
        put(9, new int[]{-1, 1});  // up-right
        put(4, new int[]{0, -1});  // left
        put(6, new int[]{0, 1});   // right
        put(1, new int[]{1, -1});  // down-left
        put(2, new int[]{1, 0});   // down
        put(3, new int[]{1, 1});   // down-right
    }};
    private List<LabyrinthNode> nodes;
    private LabyrinthNode entry;
    private LabyrinthNode exit;
    private List<String> words;
    private Random random;
    private int maxWordLength;
    private int minX, maxX, minY, maxY;
    private double niveau;
    private Map<Character , Integer> letterDistribution = new HashMap<>();
    List<String> poses = new ArrayList<String>(); 

    public Labyrinth(String difficulty) {
        nodes = new ArrayList<>();
        random = new Random();
        words = loadWords(difficulty);
        maxWordLength = words.stream()
            .mapToInt(String::length)
            .max()
            .orElse(0);
            this.minX = 0;
            this.minY = 0;
        for(Character c : "abcdefghijklmnopqrstuvwxyz".toCharArray()){
            letterDistribution.put(c, 0);
        }
        generateLabyrinth();
    }



    

    

        private List<String> loadWords(String difficulty) {
        List<String> allWords = new ArrayList<>();
        List<String> selectedWords = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allWords.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            System.out.println("Error reading words.txt file. Make sure it exists in the current directory.");
            e.printStackTrace();
            System.exit(1);
        }

        int wordCount;
        switch (difficulty.toLowerCase()) {
            case "easy": wordCount = 3; niveau = 0.2 ; maxX = random.nextInt(5,9) ; maxY = 5; break;
            case "medium": wordCount = 8; niveau = 0.3;  maxX = random.nextInt(7,11); maxY = 7;break;
            case "hard": wordCount = 12; niveau = 0.4; maxX = random.nextInt(9,13) ; maxY = 9; break;
            default: wordCount = 5; niveau = 0.3 ;  maxX = random.nextInt(7,11); maxY = 7;
        }
        String w;
        while (selectedWords.size() < wordCount && !allWords.isEmpty()) {
            int index = random.nextInt(allWords.size());
            w = allWords.remove(index);
            System.out.println(w);
            selectedWords.add(w);
        }
        
        return selectedWords;
    }

    private void placeWord(String word, int startX, int startY, int direction, Map<String, LabyrinthNode> positionMap) {
        // Define movement for each direction using numpad layout
        boolean pass = true;
        int[] dx = {1, 1, 0, -1, -1, -1, 0, 1};  // Down, Down-Right, Right, Up-Right, Up, Up-Left, Left, Down-Left
        int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};
        
        // Place each letter of the word
        for (int i = 0; i < word.length(); i++) {
            // Calculate new position based on direction
            int x = (startX + (dx[direction] * i));
            int y = (startY + (dy[direction] * i));
            String pos = x + "," + y;
            // if((x < 0) || (y < 0) || (x >= maxX) || (y >= maxY)){
            //     direction = (direction + 1) % 8;
            //     pass = false;
            // }
            // String pos = x + "," + y;
            // System.out.println(i);
            // int d = 0;
            // int tx = x;
            // int ty = y;
            // while(d<8 && (!pass || hasPos(poses,pos))){
            //     tx = x + dx[d];
            //     ty = y + dy[d];
            //     pos = x + "," + y;
            //     pass = true;
            //     if((tx < 0) || (ty < 0) || (tx >= maxX) || (ty >= maxY)){
            //         pass = false;
            //     }
            //     d++;
            // }

            // x= tx;
            // y = ty;
            // poses.add(pos);
            // Create new node for this letter
            LabyrinthNode node = new LabyrinthNode(word.charAt(i), x, y);
            node.isNecessary = true;
            // Add node to position map
            positionMap.put(pos, node);
            letterDistribution.put(word.charAt(i), letterDistribution.get(word.charAt(i)) + 1);
            // If not the first letter, connect with previous letter
            System.out.println(pos + " is the position of the letter " + word.charAt(i));
            if (i > 0) {
                String prevPos = (startX + (dx[direction] * (i-1))) + "," + 
                               (startY + (dy[direction] * (i-1)));
                LabyrinthNode prevNode = positionMap.get(prevPos);
                if (prevNode != null) {
                    prevNode.addNeighbor(node);
                    node.addNeighbor(prevNode);
                }
            }
        }
    }

    private boolean canPlaceWord(String word, int startX, int startY, int direction, Map<String, LabyrinthNode> positionMap) {
        int[] dx = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};
        
        for (int i = 0; i < word.length(); i++) {
            int x = startX + (dx[direction] * i);
            int y = startY + (dy[direction] * i);
            String pos = x + "," + y;
            
            if (positionMap.containsKey(pos)) {
                LabyrinthNode existing = positionMap.get(pos);
                if (existing.letter != word.charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }
   

    public boolean hasPos(List<String> poses , String pos){
        for(String p : poses){
            if(p.equals(pos)){
                return true;
            }
        }
        return false;
    }

    
    private void connectNeighbors(LabyrinthNode node, Map<String, LabyrinthNode> positionMap) {
        // Define all 8 directions (numpad layout)
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};  // Row changes
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};  // Column changes
        
        // Check all 8 surrounding positions
        for (int i = 0; i < 8; i++) {
            int newX = node.x + dx[i];
            int newY = node.y + dy[i];
            String neighborPos = newX + "," + newY;
            
            // Get neighbor at this position if it exists
            LabyrinthNode neighbor = positionMap.get(neighborPos);
            
            // Connect nodes if neighbor exists and neither is blocked
            if (neighbor != null && !node.isBlocked && !neighbor.isBlocked) {
                node.addNeighbor(neighbor);
                neighbor.addNeighbor(node);
            }
        }
    }
    private void generateLabyrinth() {
        Map<String, LabyrinthNode> positionMap = new HashMap<>();
        int[] dx = {0,0,maxX,maxX};
        int[] dy = {0,maxY , 0 , maxY};  // Down, Down-Right, Right, Up-Right, Up, Up-Left, Left, Down-Left
        // First, place all words
        int i = 0;
        for (String word : words) {
            boolean placed = false;
           
            while (!placed) {
                int startX = random.nextInt(maxX);
                int startY = random.nextInt(maxY);
                int midX = maxX /2 ;
                int midY = maxY /2;
                if(startX< midX && startY < midY){//0 1 2
                    int[] directioni = {0,1,2};
                    int direction = directioni[random.nextInt(3)];
                }else if ((startX > midX) && (startY < midY)){//2 3 4
                    int[] directioni = {0,1,3};
                    int direction = directioni[random.nextInt(3)];
                }else if((startY > midY)&&(startX < midX)){//0 7 6
                    int[] directioni = {0,6,7};
                    int direction = directioni[random.nextInt(3)];
                }else if((startX > midX) && (startY > midY)){//4 5 6
                    int[] directioni = {4,5,6};
                    int direction = directioni[random.nextInt(3)];
                }
                int direction = random.nextInt(8);
                if (canPlaceWord(word, startX, startY, direction, positionMap)) {
                    placeWord(word, startX, startY, direction, positionMap);
                    placed = true;
                }
            }
            i++;
        }

        // Find boundaries
        updateBoundaries(positionMap);

        // Fill remaining spaces with random letters from words
        fillRemainingSpaces(positionMap);

        // Add blocked cells (20% chance for each empty cell)
        addBlockedCells(positionMap);

        

        // Set entry and exit points on borders
        setEntryAndExit(positionMap);

        nodes.addAll(positionMap.values());
        
        // Connect nodes with neighbors
        for (LabyrinthNode node : nodes) {
            if (!node.isBlocked) {
                connectNeighbors(node, positionMap);
            }
        }
    }


    
private char getRandomLetter() {
    return (char) ('a' + random.nextInt(26));
}

private boolean isValidPosition(int x, int y) {
    return x >= 0 && x < 8 && y >= 0 && y < 8;
}
    private void updateBoundaries(Map<String, LabyrinthNode> positionMap) {
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
        
        for (Map.Entry<String, LabyrinthNode> entry : positionMap.entrySet()) {
            LabyrinthNode node = entry.getValue();
            minX = Math.min(minX, node.x);
            maxX = Math.max(maxX, node.x);
            minY = Math.min(minY, node.y);
            maxY = Math.max(maxY, node.y);
        }
    }

    private void fillRemainingSpaces(Map<String, LabyrinthNode> positionMap) {
        String allLetters = "abcdefghijklmnopqrstuvwxyz";
        int i = random.nextInt(26);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                String pos = x + "," + y;
                if (!positionMap.containsKey(pos)) {
                    char Letter = allLetters.charAt(i);
                    LabyrinthNode node = new LabyrinthNode(Letter, x, y);
                    positionMap.put(pos, node);
                    i = (i + 1) % 26;
                }
            }
        }
    }

    private void addBlockedCells(Map<String, LabyrinthNode> positionMap) {
        for (LabyrinthNode node : new ArrayList<>(positionMap.values())) {
            if (!isOnBorder(node) && random.nextDouble() < niveau && !node.isNecessary) { //niveau chance
                node.isBlocked = true;
            }
        }
    }

    private boolean isOnBorder(LabyrinthNode node) {
        return node.x == minX || node.x == maxX || node.y == minY || node.y == maxY;
    }

    private void setEntryAndExit(Map<String, LabyrinthNode> positionMap) {
        List<LabyrinthNode> borderNodes = positionMap.values().stream()
            .filter(node -> !node.isBlocked && isOnBorder(node))
            .collect(Collectors.toList());

        // Set entry point
        entry = borderNodes.get(random.nextInt(borderNodes.size()));
        entry.isEntry = true;
        borderNodes.remove(entry);

        // Set exit point
        exit = borderNodes.get(random.nextInt(borderNodes.size()));
        exit.isExit = true;
    }

    public void printLabyrinth(LabyrinthNode currentNode) {
        // Print column numbers
        System.out.print("    ");
        for (int y = minY; y <= maxY; y++) {
            System.out.printf("%3d ", y);
        }
        System.out.println("\n");

        Map<String, LabyrinthNode> nodeMap = new HashMap<>();
        for (LabyrinthNode node : nodes) {
            nodeMap.put(node.x + "," + node.y, node);
        }

        for (int x = minX; x <= maxX; x++) {
            System.out.printf("%2d  ", x);

            for (int y = minY; y <= maxY; y++) {
                LabyrinthNode node = nodeMap.get(x + "," + y);
            
                if (node == currentNode) {
                    System.out.print("*" + node.letter + "*");
                } else if (node.isBlocked) {
                    System.out.print("[#]");
                } else if (node.isEntry) {
                    System.out.print("(E)");
                } else if (node.isExit) {
                    System.out.print("(X)");
                } else {
                    System.out.print(" " + node.letter + " ");
                }
            
                if (y < maxY) {
                    LabyrinthNode rightNeighbor = nodeMap.get(x + "," + (y + 1));
                    if (rightNeighbor != null && !node.isBlocked && !rightNeighbor.isBlocked 
                            && node.neighbors.contains(rightNeighbor)) {
                        System.out.print("---");
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            
            System.out.println();

           
            
            // for (int y = minY; y <= maxY; y++) {
            //     LabyrinthNode node = nodeMap.get(x + "," + y);
            //     LabyrinthNode downNeighbor = nodeMap.get((x + 1) + "," + y);
                
            //         if (downNeighbor != null && !node.isBlocked && !downNeighbor.isBlocked 
            //                 && node.neighbors.contains(downNeighbor)) {
            //             System.out.print(" |  ");
            //         } else {
            //             System.out.print("    ");
            //         }
                
            // }
        }
    }
    
    
   
    

    

    

    // public boolean isWordComplete(String word) {
    //     return wordTracker.foundPaths.get(word).size() >= wordTracker.allPossiblePaths.get(word).size();
    // }

   

   



        // Method to print labyrinth with highlighted shortest path
    public void printLabyrinthWithPath(List<LabyrinthNode> path) {
        Set<LabyrinthNode> pathSet = new HashSet<>(path);
        
        // Print column numbers
        System.out.print("    ");
        for (int y = minY; y <= maxY; y++) {
            System.out.printf("%3d ", y - minY);
        }
        System.out.println("\n");
        
        Map<String, LabyrinthNode> nodeMap = new HashMap<>();
        for (LabyrinthNode node : nodes) {
            nodeMap.put(node.x + "," + node.y, node);
        }
        
        for (int x = minX; x <= maxX; x++) {
            System.out.printf("%2d  ", x - minX);
            
            for (int y = minY; y <= maxY; y++) {
                LabyrinthNode node = nodeMap.get(x + "," + y);
                if (pathSet.contains(node)) {
                    System.out.print("*" + node.letter + "*");
                } else if (node.isBlocked) {
                    System.out.print("[#]");
                } else if (node.isEntry) {
                    System.out.print("(E)");
                } else if (node.isExit) {
                    System.out.print("(X)");
                } else {
                    System.out.print(" " + node.letter + " ");
                }
                
                // Print horizontal connections
                if (y < maxY) {
                    LabyrinthNode rightNeighbor = nodeMap.get(x + "," + (y + 1));
                    if (rightNeighbor != null && !node.isBlocked && !rightNeighbor.isBlocked 
                            && node.neighbors.contains(rightNeighbor)) {
                        if (pathSet.contains(node) && pathSet.contains(rightNeighbor)) {
                            System.out.print("═══"); // Special character for path
                        } else {
                            System.out.print("---");
                        }
                    } else {
                        System.out.print("   ");
                    }
                }
            }
            System.out.println();
            
            // Print vertical connections
            if (x < maxX) {
                System.out.print("    ");
                for (int y = minY; y <= maxY; y++) {
                    LabyrinthNode node = nodeMap.get(x + "," + y);
                    LabyrinthNode downNeighbor = nodeMap.get((x + 1) + "," + y);
                    if (downNeighbor != null && !node.isBlocked && !downNeighbor.isBlocked 
                            && node.neighbors.contains(downNeighbor)) {
                        if (pathSet.contains(node) && pathSet.contains(downNeighbor)) {
                            System.out.print(" ║  "); // Special character for path
                        } else {
                            System.out.print(" |  ");
                        }
                    } else {
                        System.out.print("    ");
                    }
                }
                System.out.println();
            }
        }
}


    public static Map<Integer, int[]> getDirectionMap() {
        return DIRECTION_MAP;
    }

    public List<LabyrinthNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<LabyrinthNode> nodes) {
        this.nodes = nodes;
    }

    public LabyrinthNode getEntry() {
        return entry;
    }

    public void setEntry(LabyrinthNode entry) {
        this.entry = entry;
    }

    public LabyrinthNode getExit() {
        return exit;
    }

    public void setExit(LabyrinthNode exit) {
        this.exit = exit;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    public void setMaxWordLength(int maxWordLength) {
        this.maxWordLength = maxWordLength;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

}
