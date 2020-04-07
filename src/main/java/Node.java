import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
    private int id;
    private Set<Node> adjacencies;

    public Node(int id) {
        this.id = id;
        this.adjacencies = new HashSet<>();
    }

    public void addEdge(Node neighbor) {
        this.adjacencies.add(neighbor);
    }

    public void removeEdge(Node neighbor) {
        if(this.adjacencies.contains(neighbor)) {
            this.adjacencies.remove(neighbor);
        }
    }

    public int getId() {
        return id;
    }

    public Set<Node> getAdjacencies() {
        return adjacencies;
    }

    public int degree() {
        return this.adjacencies.size();
    }

}
