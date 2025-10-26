package model;

import java.util.*;

public class Graph {
    private int V;
    private List<Edge> edges;
    private List<List<Edge>> adj;

    public Graph(int V) {
        this.V = V;
        this.edges = new ArrayList<>();
        this.adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, double weight) {
        Edge e = new Edge(u, v, weight);
        edges.add(e);
        adj.get(u).add(e);
        adj.get(v).add(new Edge(v, u, weight));
    }

    public int getV() { return V; }
    public List<Edge> getEdges() { return new ArrayList<>(edges); }
    public List<List<Edge>> getAdj() { return adj; }

    public boolean isConnected() {
        if (V == 0) return true;
        boolean[] visited = new boolean[V];
        dfs(0, visited);
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    private void dfs(int u, boolean[] visited) {
        visited[u] = true;
        for (Edge e : adj.get(u)) {
            if (!visited[e.getV()]) {
                dfs(e.getV(), visited);
            }
        }
    }
}