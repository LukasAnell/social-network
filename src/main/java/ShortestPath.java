import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class ShortestPath {

    record QueueObject(int distance, User user) {}

    public static List<User> dijkstra(Graph graph, User start, User end) {
        if (!graph.hasUser(start) || !graph.hasUser(end)) {
            throw new IllegalArgumentException();
        }

        // stores pairs of distance, node
        PriorityQueue<QueueObject> priorityQueue = new PriorityQueue<>(
            (a, b) -> a.distance - b.distance
        );

        // stores shortest distance from source
        HashMap<User, Integer> distance = new HashMap<>();
        HashMap<User, User> previous = new HashMap<>();

        // the distance from the source to itself is 0
        distance.put(start, 0);
        priorityQueue.offer(new QueueObject(0, start));

        while (!priorityQueue.isEmpty()) {
            QueueObject top = priorityQueue.poll();

            int d = top.distance;
            User u = top.user;

            if (d > distance.get(u)) {
                continue;
            }

            for (User adjacent : graph.getConnections(u)) {
                int weight = graph.getWeight(u, adjacent);

                if (!distance.containsKey(adjacent)) {
                    distance.put(adjacent, Integer.MAX_VALUE);
                }

                if (distance.get(u) + weight < distance.get(adjacent)) {
                    distance.put(adjacent, distance.get(u) + weight);
                    previous.put(adjacent, u);
                    priorityQueue.offer(
                        new QueueObject(distance.get(adjacent), adjacent)
                    );
                }
            }
        }

        if (!previous.containsKey(end) && !end.equals(start)) {
            return new ArrayList<>();
        }

        List<User> path = new ArrayList<>();

        User current = end;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);

        return path;
    }

    public static int pathWeight(Graph graph, List<User> path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException();
        }

        int weight = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            User u1 = path.get(i);
            User u2 = path.get(i + 1);

            if (!graph.hasConnection(u1, u2)) {
                throw new IllegalArgumentException();
            }

            weight += graph.getWeight(u1, u2);
        }

        return weight;
    }
}
