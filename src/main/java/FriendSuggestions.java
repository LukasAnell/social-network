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

        return userSecondDegree;
    }

    public static int mutualFriendCount(Graph graph, User a, User b) {
        //

        return 0;
    }
}
