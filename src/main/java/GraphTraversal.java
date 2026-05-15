import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class GraphTraversal {

    public static List<User> bfs(Graph graph, User start) {
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
        return null;
    }

    public static boolean isConnected(Graph graph, User a, User b) {
        //

        return false;
    }

    public static List<User> reachableFrom(Graph graph, User start) {
        //

        return null;
    }
}
