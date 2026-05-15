import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphTraversalTest {

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

        // Alice - Bob - Carol - Diana
        //               |
        //              Eve
        graph.addConnection(alice, bob);
        graph.addConnection(bob, carol);
        graph.addConnection(carol, diana);
        graph.addConnection(carol, eve);
    }

    // --- BFS ---

    @Test
    void testBfsStartNodeIsFirst() {
        List<User> result = GraphTraversal.bfs(graph, alice);
        assertEquals(alice, result.get(0));
    }

    @Test
    void testBfsVisitsAllConnectedUsers() {
        List<User> result = GraphTraversal.bfs(graph, alice);
        assertEquals(5, result.size());
    }

    @Test
    void testBfsOrder() {
        List<User> result = GraphTraversal.bfs(graph, alice);
        // Alice should come before Bob, Bob before Carol, Carol before Diana and Eve
        assertTrue(result.indexOf(alice) < result.indexOf(bob));
        assertTrue(result.indexOf(bob) < result.indexOf(carol));
        assertTrue(result.indexOf(carol) < result.indexOf(diana));
        assertTrue(result.indexOf(carol) < result.indexOf(eve));
    }

    @Test
    void testBfsIsolatedNode() {
        User frank = new User(6, "Frank", 30);
        graph.addUser(frank);
        List<User> result = GraphTraversal.bfs(graph, frank);
        assertEquals(1, result.size());
        assertEquals(frank, result.get(0));
    }

    @Test
    void testBfsInvalidStartThrows() {
        User ghost = new User(99, "Ghost", 0);
        assertThrows(IllegalArgumentException.class, () ->
            GraphTraversal.bfs(graph, ghost)
        );
    }

    // --- DFS ---

    @Test
    void testDfsStartNodeIsFirst() {
        List<User> result = GraphTraversal.dfs(graph, alice);
        assertEquals(alice, result.get(0));
    }

    @Test
    void testDfsVisitsAllConnectedUsers() {
        List<User> result = GraphTraversal.dfs(graph, alice);
        assertEquals(5, result.size());
    }

    @Test
    void testDfsIsolatedNode() {
        User frank = new User(6, "Frank", 30);
        graph.addUser(frank);
        List<User> result = GraphTraversal.dfs(graph, frank);
        assertEquals(1, result.size());
        assertEquals(frank, result.get(0));
    }

    @Test
    void testDfsInvalidStartThrows() {
        User ghost = new User(99, "Ghost", 0);
        assertThrows(IllegalArgumentException.class, () ->
            GraphTraversal.dfs(graph, ghost)
        );
    }

    @Test
    void testDfsContainsAllUsers() {
        List<User> result = GraphTraversal.dfs(graph, alice);
        assertTrue(result.contains(alice));
        assertTrue(result.contains(bob));
        assertTrue(result.contains(carol));
        assertTrue(result.contains(diana));
        assertTrue(result.contains(eve));
    }

    // --- isConnected ---

    @Test
    void testIsConnectedDirectConnection() {
        assertTrue(GraphTraversal.isConnected(graph, alice, bob));
    }

    @Test
    void testIsConnectedIndirectConnection() {
        assertTrue(GraphTraversal.isConnected(graph, alice, diana));
    }

    @Test
    void testIsConnectedIsolatedNode() {
        User frank = new User(6, "Frank", 30);
        graph.addUser(frank);
        assertFalse(GraphTraversal.isConnected(graph, alice, frank));
    }

    @Test
    void testIsConnectedInvalidUserThrows() {
        User ghost = new User(99, "Ghost", 0);
        assertThrows(IllegalArgumentException.class, () ->
            GraphTraversal.isConnected(graph, alice, ghost)
        );
    }

    @Test
    void testIsConnectedSameUser() {
        assertTrue(GraphTraversal.isConnected(graph, alice, alice));
    }

    // --- reachableFrom ---

    @Test
    void testReachableFromExcludesStart() {
        List<User> result = GraphTraversal.reachableFrom(graph, alice);
        assertFalse(result.contains(alice));
    }

    @Test
    void testReachableFromCount() {
        List<User> result = GraphTraversal.reachableFrom(graph, alice);
        assertEquals(4, result.size());
    }

    @Test
    void testReachableFromIsolatedNode() {
        User frank = new User(6, "Frank", 30);
        graph.addUser(frank);
        List<User> result = GraphTraversal.reachableFrom(graph, frank);
        assertEquals(0, result.size());
    }

    @Test
    void testReachableFromInvalidUserThrows() {
        User ghost = new User(99, "Ghost", 0);
        assertThrows(IllegalArgumentException.class, () ->
            GraphTraversal.reachableFrom(graph, ghost)
        );
    }
}
