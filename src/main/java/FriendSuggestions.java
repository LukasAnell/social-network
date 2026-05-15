import java.util.ArrayList;
import java.util.List;

public class FriendSuggestions {

    public static List<User> suggest(Graph graph, User user) {
        if (!graph.hasUser(user)) {
            throw new IllegalArgumentException();
        }

        // get list of users connected to user
        // then, use that to find users in graph that are not connected to user
        // get list of users connected to each user adjacent to user
        // find which users in the graph (which are not connected to user)
        // ARE connected to one of the users connected to each user adjancent to user
        // this makes no sense when typed out

        List<User> userFirstDegree = graph.getConnections(user);
        List<User> candidates = new ArrayList<>();

        for (User candidate : graph.getUsers()) {
            if (candidate.equals(user) || userFirstDegree.contains(candidate)) {
                continue;
            }

            if (mutualFriendCount(graph, user, candidate) > 0) {
                candidates.add(candidate);
            }
        }

        candidates.sort(
            (a, b) ->
                mutualFriendCount(graph, user, b) -
                mutualFriendCount(graph, user, a)
        );

        return candidates;
    }

    public static int mutualFriendCount(Graph graph, User a, User b) {
        if (!graph.hasUser(a) || !graph.hasUser(b)) {
            throw new IllegalArgumentException();
        }

        List<User> aConnections = graph.getConnections(a);
        List<User> bConnections = graph.getConnections(b);

        List<User> intersection = new ArrayList<>(aConnections);
        intersection.retainAll(bConnections);

        return intersection.size();
    }
}
