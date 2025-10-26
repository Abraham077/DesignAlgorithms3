package model;

public class Edge implements Comparable<Edge> {
    private int u;
    private int v;
    private double weight;

    public Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public int getU() { return u; }
    public int getV() { return v; }
    public double getWeight() { return weight; }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %.2f)", u, v, weight);
    }
}
