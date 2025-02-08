import java.util.List;

class SearchState {
        LabyrinthNode node;
        String path;
        List<LabyrinthNode> nodePath;
        
        SearchState(LabyrinthNode node, String path, List<LabyrinthNode> nodePath) {
            this.node = node;
            this.path = path;
            this.nodePath = nodePath;
        }
}
