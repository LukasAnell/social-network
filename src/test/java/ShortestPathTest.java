import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShortestPathTest {

    private Graph graph;
    private User alice;
    private User bob;
    private User carol;
    private User diana;
    private User eve;

    @BeforeEach
    void setUp() {
        graph = new Graph();
        alice = new User(1, "Alice", 23);
        bob = new User(2, "Bob", 25);
        carol = new User(3, "Carol", 22);
        diana = new User(4, "Diana", 28);
        eve = new User(5, "Eve", 24);

        graph.addUser(alice);
        graph.addUser(bob);
        graph.addUser(carol);
        graph.addUser(diana);
        graph.addUser(eve);

        //        1       5
        // Alice --- Bob --- Carol
        //   \               /
        //  3 \             / 2
        //     \           /
        //      Diana --- Eve
        //            4
        graph.addConnection(alice, bob, 1);
        graph.addConnection(bob, carol, 5);
        graph.addConnection(alice, diana, 3);
        graph.addConnection(diana, eve, 4);
        graph.addConnection(eve, carol, 2);
    }

    // --- Dijkstra ---

    @Test
    void testShortestPathDirectConnection() {
        List<User> path = ShortestPath.dijkstra(graph, alice, bob);
        assertEquals(alice, path.get(0));
        assertEquals(bob, path.get(path.size() - 1));
    }

    @Test
    void testShortestPathChoosesLowestWeight() {
        List<User> path = ShortestPath.dijkstra(graph, alice, carol);
        // direct path Alice->Bob->Carol = 1+5 = 6
        // indirect path Alice->Diana->Eve->Carol = 3+4+2 = 9
        // shortest is Alice->Bob->Carol
        assertEquals(3, path.size());
        assertEquals(alice, path.get(0));
        assertEquals(bob, path.get(1));
        assertEquals(carol, path.get(2));
    }

    @Test
    void testShortestPathWeight() {
        List<User> path = ShortestPath.dijkstra(graph, alice, carol);
        assertEquals(6, ShortestPath.pathWeight(graph, path));
    }

    @Test
    void testShortestPathToSelf() {
        List<User> path = ShortestPath.dijkstra(graph, alice, alice);
        assertEquals(1, path.size());
        assertEquals(alice, path.get(0));
    }

    @Test
    void testShortestPathNoPathReturnsEmpty() {
        User frank = new User(6, "Frank", 30);
        graph.addUser(frank);
        List<User> path = ShortestPath.dijkstra(graph, alice, frank);
        assertEquals(0, path.size());
    }

    @Test
    void testShortestPathIncludesEndpoints() {
        List<User> path = ShortestPath.dijkstra(graph, alice, carol);
        assertEquals(alice, path.get(0));
        assertEquals(carol, path.get(path.size() - 1));
    }

    @Test
    void testShortestPathInvalidUserThrows() {
        User ghost = new User(99, "Ghost", 0);
        assertThrows(IllegalArgumentException.class, () ->
            ShortestPath.dijkstra(graph, alice, ghost)
        );
    }

    // --- pathWeight ---

    @Test
    void testPathWeightSingleEdge() {
        List<User> path = List.of(alice, bob);
        assertEquals(1, ShortestPath.pathWeight(graph, path));
    }

    @Test
    void testPathWeightMultipleEdges() {
        List<User> path = List.of(alice, diana, eve, carol);
        assertEquals(9, ShortestPath.pathWeight(graph, path));
    }

    @Test
    void testPathWeightEmptyPathThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            ShortestPath.pathWeight(graph, List.of())
        );
    }

    @Test
    void testPathWeightDisconnectedThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            ShortestPath.pathWeight(graph, List.of(alice, carol))
        );
    }
}
