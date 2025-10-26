package algorithm;

public class UnionFind {
    private int[] parent;
    private int[] rank;
    private int operations;

    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        operations = 0;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        operations++;
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        operations++;
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            // union by rank
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }

    public int getOperations() {
        return operations;
    }
}