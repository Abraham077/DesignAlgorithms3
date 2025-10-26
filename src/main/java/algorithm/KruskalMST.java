package algorithm;

import model.Edge;
import model.Graph;

import java.util.*;

public class KruskalMST {
    private List<Edge> mstEdges;
    private double totalWeight;
    private long executionTimeMs;
    private int operationCount;

    public KruskalMST(Graph graph) {
        long startTime = System.currentTimeMillis();
        this.mstEdges = new ArrayList<>();
        this.totalWeight = 0.0;

        int V = graph.getV();
        if (V == 0) {
            this.executionTimeMs = System.currentTimeMillis() - startTime;
            this.operationCount = 0;
            return;
        }

        if (!graph.isConnected()) {
            throw new IllegalArgumentException("Graph is disconnected. MST does not exist.");
        }

        List<Edge> edges = new ArrayList<>(graph.getEdges());
        edges.sort(Edge::compareTo);


        int sortOperations = (int) (edges.size() * (Math.log(edges.size() + 1) / Math.log(2)));

        UnionFind uf = new UnionFind(V);
        int edgesAdded = 0;

        for (Edge e : edges) {
            int u = e.getU();
            int v = e.getV();
            double w = e.getWeight();

            if (uf.find(u) != uf.find(v)) {
                mstEdges.add(e);
                totalWeight += w;
                uf.union(u, v);
                edgesAdded++;
                if (edgesAdded == V - 1) break;
            }
        }

        this.operationCount = sortOperations + uf.getOperations();
        this.executionTimeMs = System.currentTimeMillis() - startTime;
    }

    public List<Edge> getMstEdges() { return new ArrayList<>(mstEdges); }
    public double getTotalWeight() { return totalWeight; }
    public long getExecutionTimeMs() { return executionTimeMs; }
    public int getOperationCount() { return operationCount; }
}