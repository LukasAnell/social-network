import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FriendSuggestionsTest {

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

        // Alice - Bob - Carol
        //          |     |
        //         Diana--Eve
        graph.addConnection(alice, bob, 1);
        graph.addConnection(bob, carol, 1);
        graph.addConnection(bob, diana, 1);
        graph.addConnection(carol, eve, 1);
        graph.addConnection(diana, eve, 1);
    }

    // --- suggest ---

    @Test
    void testSuggestExcludesDirectFriends() {
        List<User> suggestions = FriendSuggestions.suggest(graph, alice);
        assertFalse(suggestions.contains(bob));
    }

    @Test
    void testSuggestExcludesSelf() {
        List<User> suggestions = FriendSuggestions.suggest(graph, alice);
        assertFalse(suggestions.contains(alice));
    }

    @Test
    void testSuggestIncludesFriendsOfFriends() {
        List<User> suggestions = FriendSuggestions.suggest(graph, alice);
        assertTrue(suggestions.contains(carol));
        assertTrue(suggestions.contains(diana));
    }

    @Test
    void testSuggestSortedByMutualFriends() {
        List<User> suggestions = FriendSuggestions.suggest(graph, alice);
        // carol and diana both have 1 mutual friend (bob) with alice
        // eve has 0 mutuals with alice so should not appear
        assertTrue(suggestions.contains(carol));
        assertTrue(suggestions.contains(diana));
        assertFalse(suggestions.contains(eve));
    }

    @Test
    void testSuggestIsolatedUserReturnsEmpty() {
        User frank = new User(6, "Frank", 30);
        graph.addUser(frank);
        List<User> suggestions = FriendSuggestions.suggest(graph, frank);
        assertEquals(0, suggestions.size());
    }

    @Test
    void testSuggestInvalidUserThrows() {
        User ghost = new User(99, "Ghost", 0);
        assertThrows(IllegalArgumentException.class, () ->
            FriendSuggestions.suggest(graph, ghost)
        );
    }

    // --- mutualFriendCount ---

    @Test
    void testMutualFriendCountNone() {
        assertEquals(0, FriendSuggestions.mutualFriendCount(graph, alice, eve));
    }

    @Test
    void testMutualFriendCountOne() {
        assertEquals(
            1,
            FriendSuggestions.mutualFriendCount(graph, alice, carol)
        );
    }

    @Test
    void testMutualFriendCountTwo() {
        assertEquals(
            2,
            FriendSuggestions.mutualFriendCount(graph, carol, diana)
        );
    }

    @Test
    void testMutualFriendCountInvalidUserThrows() {
        User ghost = new User(99, "Ghost", 0);
        assertThrows(IllegalArgumentException.class, () ->
            FriendSuggestions.mutualFriendCount(graph, alice, ghost)
        );
    }

    @Test
    void testMutualFriendCountSymmetric() {
        assertEquals(
            FriendSuggestions.mutualFriendCount(graph, alice, carol),
            FriendSuggestions.mutualFriendCount(graph, carol, alice)
        );
    }
}
