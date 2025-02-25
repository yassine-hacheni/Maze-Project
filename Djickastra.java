import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import javax.swing.JFrame;

class Djickastra {
    public List<LabyrinthNode> shortestPath;
    public int totalMoves;
    public Labyrinth labyrinth;
    public GUIForDFS gui;
    private JFrame frame ;
        
    public Djickastra(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
        this.gui = new GUIForDFS(labyrinth);
    }

    public class NodeDistance {
        LabyrinthNode node;
        int distance;
        
        public NodeDistance(LabyrinthNode node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    public void findShortestPathToExit() {
        // Priority queue to get node with minimum distance
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(
            Comparator.comparingInt(nd -> nd.distance)
        );
        
        // Track distances and previous nodes for path reconstruction
        Map<LabyrinthNode, Integer> distances = new HashMap<>();
        Map<LabyrinthNode, LabyrinthNode> previousNodes = new HashMap<>();
        Set<LabyrinthNode> visited = new HashSet<>();
        
        // Initialize distances
        for (LabyrinthNode node : labyrinth.getNodes()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(labyrinth.getEntry(), 0);
        pq.offer(new NodeDistance(labyrinth.getEntry(), 0));
        
        // Dijkstra's algorithm
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            LabyrinthNode currentNode = current.node;
            
            if (currentNode == labyrinth.getExit()) {
                break; // Found the exit
            }
            
            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);
            
            // Check all neighbors
            for (LabyrinthNode neighbor : currentNode.neighbors) {
                if (!neighbor.isBlocked && !visited.contains(neighbor)) {
                    int newDistance = distances.get(currentNode) + 1; // Cost is 1 for each move
                    
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        previousNodes.put(neighbor, currentNode);
                        pq.offer(new NodeDistance(neighbor, newDistance));
                    }
                }
            }
        }
                // Reconstruct path
        List<LabyrinthNode> path = new ArrayList<>();
        LabyrinthNode current = labyrinth.getExit();
        
        while (current != null) {
            path.add(0, current);
            current = previousNodes.get(current);
        }
        
        // Check if path was found
        if (path.size() <= 1 || path.get(0) != labyrinth.getEntry()) {
            System.out.println("No path found");
        }
        
        this.shortestPath = path;
        this.totalMoves = distances.get(labyrinth.getExit());
    }



    
    public void displayShortestPathToExit() {
        this.findShortestPathToExit();
        
        if (this.totalMoves == -1) {
            System.out.println("\n❌ No path exists from entry to exit!");
            return;
        }
        
        System.out.println("\n=== Shortest Path to Exit ===");
        System.out.println("Minimum moves required: " + this.totalMoves);
        System.out.println("\nPath:");
        
        for (int i = 0; i < this.shortestPath.size(); i++) {
            LabyrinthNode node = this.shortestPath.get(i);
            node.isMot = true;
        }

        this.frame = new JFrame("Word Labyrinth DFS Show");
        frame.add(this.gui);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //     System.out.printf("(%d,%d:%c)", node.x, node.y, node.letter);
        //     if (i < this.shortestPath.size() - 1) {
        //         System.out.print(" → ");
        //     }
        // }
        // System.out.println("\n");
        
        // // Display the path on the labyrinth
        // this.labyrinth.printLabyrinthWithPath(this.shortestPath);
    }




    public List<LabyrinthNode> getShortestPath() {
        return shortestPath;
    }




    public void setShortestPath(List<LabyrinthNode> shortestPath) {
        this.shortestPath = shortestPath;
    }




    public int getTotalMoves() {
        return totalMoves;
    }




    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }




    public Labyrinth getLabyrinth() {
        return labyrinth;
    }




    public void setLabyrinth(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
    }
    
    
} 
