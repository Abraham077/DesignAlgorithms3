package algorithm;

import model.Edge;
import model.Graph;

import java.util.*;

public class PrimMST {
    private List<Edge> mstEdges;
    private double totalWeight;
    private long executionTimeMs;
    private int operationCount;

    public PrimMST(Graph graph) {
        long startTime = System.currentTimeMillis();
        this.operationCount = 0;
        this.mstEdges = new ArrayList<>();
        this.totalWeight = 0.0;

        int V = graph.getV();
        if (V == 0) return;

        if (!graph.isConnected()) {
            throw new IllegalArgumentException("Graph is disconnected. MST does not exist.");
        }

        double[] key = new double[V];
        boolean[] inMST = new boolean[V];
        int[] parent = new int[V];

        Arrays.fill(key, Double.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[0] = 0.0;

        PriorityQueue<double[]> pq = new PriorityQueue<>((a, b) -> Double.compare(a[0], b[0]));
        pq.offer(new double[]{0.0, 0});

        while (!pq.isEmpty()) {
            double[] current = pq.poll();
            double uKey = current[0];
            int u = (int) current[1];

            if (inMST[u]) continue;
            inMST[u] = true;

            if (parent[u] != -1) {
                mstEdges.add(new Edge(parent[u], u, uKey));
                totalWeight += uKey;
            }

            for (Edge e : graph.getAdj().get(u)) {
                int v = e.getV();
                double weight = e.getWeight();

                if (!inMST[v] && weight < key[v]) {
                    operationCount++;
                    key[v] = weight;
                    parent[v] = u;
                    pq.offer(new double[]{weight, v});
                }
            }
        }

        this.executionTimeMs = System.currentTimeMillis() - startTime;
    }

    public List<Edge> getMstEdges() { return new ArrayList<>(mstEdges); }
    public double getTotalWeight() { return totalWeight; }
    public long getExecutionTimeMs() { return executionTimeMs; }
    public int getOperationCount() { return operationCount; }
}