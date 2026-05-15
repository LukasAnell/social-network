import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphTest {

    private Graph graph;
    private User alice;
    private User bob;
    private User carol;

    @BeforeEach
    void setUp() {
        graph = new Graph();
        alice = new User(1, "Alice", 23);
        bob = new User(2, "Bob", 25);
        carol = new User(3, "Carol", 22);
    }

    // --- User management ---

    @Test
    void testAddUser() {
        graph.addUser(alice);
        assertTrue(graph.hasUser(alice));
    }

    @Test
    void testAddDuplicateUserThrows() {
        graph.addUser(alice);
        assertThrows(IllegalArgumentException.class, () ->
            graph.addUser(alice)
        );
    }

    @Test
    void testRemoveUser() {
        graph.addUser(alice);
        graph.removeUser(alice);
        assertFalse(graph.hasUser(alice));
    }

    @Test
    void testRemoveNonExistentUserThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            graph.removeUser(alice)
        );
    }

    @Test
    void testGetUsers() {
        graph.addUser(alice);
        graph.addUser(bob);
        assertEquals(2, graph.getUsers().size());
    }

    // --- Connection management ---

    @Test
    void testAddConnection() {
        graph.addUser(alice);
        graph.addUser(bob);
        graph.addConnection(alice, bob);
        assertTrue(graph.hasConnection(alice, bob));
    }

    @Test
    void testConnectionIsUndirected() {
        graph.addUser(alice);
        graph.addUser(bob);
        graph.addConnection(alice, bob);
        assertTrue(graph.hasConnection(bob, alice));
    }

    @Test
    void testAddConnectionNonExistentUserThrows() {
        graph.addUser(alice);
        assertThrows(IllegalArgumentException.class, () ->
            graph.addConnection(alice, bob)
        );
    }

    @Test
    void testAddDuplicateConnectionThrows() {
        graph.addUser(alice);
        graph.addUser(bob);
        graph.addConnection(alice, bob);
        assertThrows(IllegalArgumentException.class, () ->
            graph.addConnection(alice, bob)
        );
    }

    @Test
    void testRemoveConnection() {
        graph.addUser(alice);
        graph.addUser(bob);
        graph.addConnection(alice, bob);
        graph.removeConnection(alice, bob);
        assertFalse(graph.hasConnection(alice, bob));
    }

    @Test
    void testRemoveConnectionIsUndirected() {
        graph.addUser(alice);
        graph.addUser(bob);
        graph.addConnection(alice, bob);
        graph.removeConnection(alice, bob);
        assertFalse(graph.hasConnection(bob, alice));
    }

    @Test
    void testRemoveNonExistentConnectionThrows() {
        graph.addUser(alice);
        graph.addUser(bob);
        assertThrows(IllegalArgumentException.class, () ->
            graph.removeConnection(alice, bob)
        );
    }

    @Test
    void testRemoveUserAlsoRemovesConnections() {
        graph.addUser(alice);
        graph.addUser(bob);
        graph.addConnection(alice, bob);
        graph.removeUser(alice);
        assertFalse(graph.hasConnection(bob, alice));
        assertEquals(0, graph.getConnections(bob).size());
    }

    // --- Getters ---

    @Test
    void testGetConnections() {
        graph.addUser(alice);
        graph.addUser(bob);
        graph.addUser(carol);
        graph.addConnection(alice, bob);
        graph.addConnection(alice, carol);
        List<User> connections = graph.getConnections(alice);
        assertEquals(2, connections.size());
    }

    @Test
    void testGetConnectionsNonExistentUserThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            graph.getConnections(alice)
        );
    }
}
