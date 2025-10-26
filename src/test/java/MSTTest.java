import algorithm.KruskalMST;
import algorithm.PrimMST;
import model.Graph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class MSTTest {

    private Graph smallGraph;
    private Graph disconnectedGraph;

    @BeforeEach
    void setUp() {
        smallGraph = new Graph(4);
        smallGraph.addEdge(0, 1, 10);
        smallGraph.addEdge(0, 2, 6);
        smallGraph.addEdge(0, 3, 5);
        smallGraph.addEdge(1, 3, 15);
        smallGraph.addEdge(2, 3, 4);

        disconnectedGraph = new Graph(4);
        disconnectedGraph.addEdge(0, 1, 5);
        disconnectedGraph.addEdge(2, 3, 7);
    }


    @Test
    void test_MSTCostsAreEqual() {
        PrimMST prim = new PrimMST(smallGraph);
        KruskalMST kruskal = new KruskalMST(smallGraph);
        assertEquals(prim.getTotalWeight(), kruskal.getTotalWeight(), 1e-6,
                "MST total cost must be identical for both algorithms");
    }

    @Test
    void test_MSTHasVMinus1Edges() {
        int V = smallGraph.getV();
        PrimMST prim = new PrimMST(smallGraph);
        KruskalMST kruskal = new KruskalMST(smallGraph);

        assertEquals(V - 1, prim.getMstEdges().size(),
                "Prim MST must have V-1 edges");
        assertEquals(V - 1, kruskal.getMstEdges().size(),
                "Kruskal MST must have V-1 edges");
    }

    @Test
    void test_MSTIsConnectedAndAcyclic() {
        PrimMST prim = new PrimMST(smallGraph);
        assertTrue(isConnectedAndAcyclic(prim.getMstEdges(), smallGraph.getV()),
                "Prim MST must be connected and acyclic");

        KruskalMST kruskal = new KruskalMST(smallGraph);
        assertTrue(isConnectedAndAcyclic(kruskal.getMstEdges(), smallGraph.getV()),
                "Kruskal MST must be connected and acyclic");
    }

    @Test
    void test_DisconnectedGraphThrowsException() {
        IllegalArgumentException primEx = assertThrows(
                IllegalArgumentException.class,
                () -> new PrimMST(disconnectedGraph),
                "Prim should reject disconnected graph"
        );
        assertTrue(primEx.getMessage().contains("disconnected"));

        IllegalArgumentException kruskalEx = assertThrows(
                IllegalArgumentException.class,
                () -> new KruskalMST(disconnectedGraph),
                "Kruskal should reject disconnected graph"
        );
        assertTrue(kruskalEx.getMessage().contains("disconnected"));
    }


    @Test
    void test_ExecutionTimeIsNonNegative() {
        PrimMST prim = new PrimMST(smallGraph);
        KruskalMST kruskal = new KruskalMST(smallGraph);

        assertTrue(prim.getExecutionTimeMs() >= 0, "Prim execution time must be ≥ 0");
        assertTrue(kruskal.getExecutionTimeMs() >= 0, "Kruskal execution time must be ≥ 0");
    }

    @Test
    void test_OperationCountIsNonNegative() {
        PrimMST prim = new PrimMST(smallGraph);
        KruskalMST kruskal = new KruskalMST(smallGraph);

        assertTrue(prim.getOperationCount() >= 0, "Prim operation count must be ≥ 0");
        assertTrue(kruskal.getOperationCount() >= 0, "Kruskal operation count must be ≥ 0");
    }

    @Test
    void test_ResultsAreReproducible() {
        PrimMST prim1 = new PrimMST(smallGraph);
        PrimMST prim2 = new PrimMST(smallGraph);
        assertEquals(prim1.getTotalWeight(), prim2.getTotalWeight(), 1e-6);
        assertEquals(prim1.getMstEdges().size(), prim2.getMstEdges().size());

        KruskalMST k1 = new KruskalMST(smallGraph);
        KruskalMST k2 = new KruskalMST(smallGraph);
        assertEquals(k1.getTotalWeight(), k2.getTotalWeight(), 1e-6);
        assertEquals(k1.getMstEdges().size(), k2.getMstEdges().size());
    }


    private boolean isConnectedAndAcyclic(List<model.Edge> mstEdges, int V) {
        if (mstEdges.size() != V - 1) return false;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (model.Edge e : mstEdges) {
            adj.get(e.getU()).add(e.getV());
            adj.get(e.getV()).add(e.getU());
        }

        boolean[] visited = new boolean[V];
        dfs(0, -1, adj, visited);

        for (boolean v : visited) {
            if (!v) return false;
        }

        return true;
    }

    private void dfs(int u, int parent, List<List<Integer>> adj, boolean[] visited) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (v == parent) continue;
            if (visited[v]) {
                return;
            }
            dfs(v, u, adj, visited);
        }
    }
}