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

        List<User> graphUsers = graph.getUsers();
        graphUsers.remove(user);

        List<User> userFirstDegree = graph.getConnections(user);

        // all users not connected to user
        List<User> notConnectedToUser = new ArrayList<>(graphUsers);
        notConnectedToUser.removeAll(userFirstDegree);

        // check which users in userFirstDegree have a connection that's contained in notConnectedToUser
        List<User> userSecondDegree = new ArrayList<>();
        for (User u : userFirstDegree) {
            for (User v : notConnectedToUser) {
                if (graph.getConnections(u).contains(v)) {
                    userSecondDegree.add(v);
                }
            }
        }

        System.out.println("Before sorting: " + userSecondDegree);

        userSecondDegree.sort((u1, u2) -> {
            int c1 = mutualFriendCount(graph, user, u1);
            int c2 = mutualFriendCount(graph, user, u2);

            return Integer.compare(c1, c2);
        });

        System.out.println("After sorting: " + userSecondDegree);

        return userSecondDegree.reversed();
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
