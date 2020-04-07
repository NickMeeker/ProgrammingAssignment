import java.util.*;

public class Graph {
    private Map<Integer, Node> nodes;

    public Graph() {
        this.nodes = new HashMap<>();
        this.initialNode();
    }

    public void initialNode() {
        // create initial node and connect it to itself
        this.nodes.put(0, new Node(0));
        this.nodes.get(0).addEdge(this.nodes.get(0));
    }

    public double chanceOfBeingBirthEdge(int id) {

        int edges = 0;
        for (int key : nodes.keySet()) {
            edges += nodes.get(key).degree();
        }

        // normally we'd need to divide edges by 2
        // since we double-counted them all, but
        // this algorithm calls for dividing the degree
        // of each node by edges * 2, so no need here
        return ((double) nodes.get(id).degree()) / edges;
    }

    public double chanceOfBeingKilled(int id) {

        int edges = 0;
        for (int key : nodes.keySet()) {
            edges += nodes.get(key).degree();
        }

        return ((double) nodes.size() - nodes.get(id).degree()) / (Math.pow(nodes.size(), 2) - edges);
    }

    public int getRandomNode(Map<Integer, Double> probabilities) {
        Random rand = new Random();
        double p = rand.nextDouble();
        double cumulativeProbability = 0.0;
        int id = -1;
        for(int key : probabilities.keySet()) {
            cumulativeProbability += probabilities.get(key);
            // meets beats
            if(cumulativeProbability >= p) {
                id = key;
            }
        }

        return id;
    }

    public void birth(int newNodeId) {

        Node newNode = new Node(newNodeId);
        nodes.put(newNodeId, newNode);

        Map<Integer, Double> probabilities = new HashMap<>();

        for(int id : nodes.keySet()) {
            probabilities.put(id, chanceOfBeingBirthEdge(id));
        }

        int adjNodeId = getRandomNode(probabilities);

        nodes.get(newNodeId).addEdge(nodes.get(adjNodeId));
        nodes.get(adjNodeId).addEdge(nodes.get(newNodeId));
    }

    public void death(double[] chanceOfBeingChosenForDeletion) {
        Map<Integer, Double> probabilities = new HashMap<>();

        for(int id : nodes.keySet()) {
            probabilities.put(id, chanceOfBeingKilled(id));
        }

        int id = getRandomNode(probabilities);

        Node node = nodes.get(id);

        for(Node neighbor : node.getAdjacencies()) {
            if(neighbor.degree() < chanceOfBeingChosenForDeletion.length) {
                chanceOfBeingChosenForDeletion[neighbor.degree()]++;
            }
        }

        // unlink this node to all of its adjacencies
        for(int key : nodes.keySet()) {
            if(nodes.get(key).getAdjacencies().contains(node)) {
                nodes.get(key).getAdjacencies().remove(key);
            }
        }

        nodes.remove(id);
    }

    public int size() {
        return this.nodes.size();
    }

    public int countEdges() {
        int edges = 0;
        for (int key : nodes.keySet()) {
            edges += nodes.get(key).degree();
        }

        return edges / 2;
    }

    public double[] getDistributionOfDegrees() {
        double[] distribution = new double[8];
        Arrays.fill(distribution, 0);

        for(int id : nodes.keySet()) {
            if(nodes.get(id).degree() < 8) {
                distribution[nodes.get(id).degree()]++;
            }
        }
        return distribution;
    }
}
