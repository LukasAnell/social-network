import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class GraphTraversal {

    public static List<User> bfs(Graph graph, User start) {
        if (!graph.hasUser(start)) {
            throw new IllegalArgumentException();
        }

        Set<User> visited = new HashSet<>();
        List<User> result = new ArrayList<>();

        Queue<User> queue = new LinkedList<>();
        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            User currentUser = queue.poll();
            result.add(currentUser);

            for (User u : graph.getConnections(currentUser)) {
                if (!visited.contains(u)) {
                    visited.add(u);
                    queue.add(u);
                }
            }
        }

        return result;
    }

    public static List<User> dfs(Graph graph, User start) {
        if (!graph.hasUser(start)) {
            throw new IllegalArgumentException();
        }

        Set<User> visited = new HashSet<>();
        List<User> result = new ArrayList<>();

        dfs(graph, visited, start, result);

        return result;
    }

    private static void dfs(
        Graph graph,
        Set<User> visited,
        User start,
        List<User> result
    ) {
        visited.add(start);
        result.add(start);

        for (User u : graph.getConnections(start)) {
            if (!visited.contains(u)) {
                dfs(graph, visited, u, result);
            }
        }
    }

    public static boolean isConnected(Graph graph, User a, User b) {
        if (!graph.hasUser(b)) {
            throw new IllegalArgumentException();
        }

        return bfs(graph, a).contains(b);
    }

    public static List<User> reachableFrom(Graph graph, User start) {
        List<User> connectedUsers = bfs(graph, start);
        connectedUsers.remove(start);

        return connectedUsers;
    }
}
