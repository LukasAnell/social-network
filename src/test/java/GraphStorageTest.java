import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphStorageTest {

    private static final String TEST_FILE = "test_graph.txt";
    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = new Graph();
        User alice = new User(1, "Alice", 23);
        User bob = new User(2, "Bob", 25);
        User carol = new User(3, "Carol", 22);
        graph.addUser(alice);
        graph.addUser(bob);
        graph.addUser(carol);
        graph.addConnection(alice, bob);
        graph.addConnection(alice, carol);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testSaveAndLoadUserCount() throws IOException {
        GraphStorage.saveGraph(graph, TEST_FILE);
        Graph loaded = GraphStorage.loadGraph(TEST_FILE);
        assertEquals(3, loaded.getUsers().size());
    }

    @Test
    void testSaveAndLoadUserFields() throws IOException {
        GraphStorage.saveGraph(graph, TEST_FILE);
        Graph loaded = GraphStorage.loadGraph(TEST_FILE);
        boolean found = false;
        for (User u : loaded.getUsers()) {
            if (
                u.getId() == 1 &&
                u.getName().equals("Alice") &&
                u.getAge() == 23
            ) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testSaveAndLoadConnections() throws IOException {
        GraphStorage.saveGraph(graph, TEST_FILE);
        Graph loaded = GraphStorage.loadGraph(TEST_FILE);
        User alice = null;
        User bob = null;
        for (User u : loaded.getUsers()) {
            if (u.getId() == 1) alice = u;
            if (u.getId() == 2) bob = u;
        }
        assertTrue(loaded.hasConnection(alice, bob));
    }

    @Test
    void testConnectionCount() throws IOException {
        GraphStorage.saveGraph(graph, TEST_FILE);
        Graph loaded = GraphStorage.loadGraph(TEST_FILE);
        User alice = null;
        for (User u : loaded.getUsers()) {
            if (u.getId() == 1) alice = u;
        }
        assertEquals(2, loaded.getConnections(alice).size());
    }

    @Test
    void testConnectionIsUndirected() throws IOException {
        GraphStorage.saveGraph(graph, TEST_FILE);
        Graph loaded = GraphStorage.loadGraph(TEST_FILE);
        User alice = null;
        User bob = null;
        for (User u : loaded.getUsers()) {
            if (u.getId() == 1) alice = u;
            if (u.getId() == 2) bob = u;
        }
        assertTrue(loaded.hasConnection(bob, alice));
    }

    @Test
    void testIsolatedUserSavesAndLoads() throws IOException {
        User diana = new User(4, "Diana", 28);
        graph.addUser(diana);
        GraphStorage.saveGraph(graph, TEST_FILE);
        Graph loaded = GraphStorage.loadGraph(TEST_FILE);
        assertEquals(4, loaded.getUsers().size());
        User loadedDiana = null;
        for (User u : loaded.getUsers()) {
            if (u.getId() == 4) loadedDiana = u;
        }
        assertNotNull(loadedDiana);
        assertEquals(0, loaded.getConnections(loadedDiana).size());
    }

    @Test
    void testLoadNonExistentFileThrows() {
        assertThrows(IOException.class, () ->
            GraphStorage.loadGraph("nonexistent.txt")
        );
    }
}
