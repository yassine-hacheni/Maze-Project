import java.util.HashSet;
import java.util.Set;

class LabyrinthNode {
    public char letter;
    public int x, y;
    public Set<LabyrinthNode> neighbors;
    public boolean isBlocked;
    public boolean isEntry;
    public boolean isExit;
    public boolean isNecessary;
    public boolean isMot = false;

    public LabyrinthNode(char letter, int x, int y) {
        this.letter = letter;
        this.x = x;
        this.y = y;
        this.neighbors = new HashSet<>();
        this.isBlocked = false;
        this.isEntry = false;
        this.isExit = false;
        this.isNecessary = false;
    }

    public void addNeighbor(LabyrinthNode node) {
        if (!isBlocked && !node.isBlocked) {
            neighbors.add(node);
        }
    }

    public boolean isEqual(LabyrinthNode node) {
        return this.x == node.x && this.y == node.y;
    }
}

