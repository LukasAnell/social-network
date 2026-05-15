import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {

    private HashMap<User, List<User>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addUser(User user) {
        if (adjacencyList.containsKey(user)) {
            throw new IllegalArgumentException();
        }

        adjacencyList.computeIfAbsent(user, k -> new ArrayList<>());
    }

    public void removeUser(User user) {
        if (!adjacencyList.containsKey(user)) {
            throw new IllegalArgumentException();
        }

        // remove all instances of user in any connection lists
        List<User> uniqueUsers = this.getUsers();

        for (User key : uniqueUsers) {
            adjacencyList.computeIfPresent(key, (k, l) -> {
                l.removeIf(obj -> obj.equals(user));
                return l;
            });
        }

        // remove user from keys
        adjacencyList.remove(user);
    }

    public void addConnection(User a, User b) {
        boolean eitherUserDoesntExist = (!adjacencyList.containsKey(a) ||
            !adjacencyList.containsKey(b));
        if (eitherUserDoesntExist) {
            throw new IllegalArgumentException();
        }

        boolean connectionExists = (adjacencyList.get(a).contains(b) ||
            adjacencyList.get(b).contains(a));
        if (connectionExists) {
            throw new IllegalArgumentException();
        }

        // add user b to user a's connection list
        // then, the other way around
        adjacencyList.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
        adjacencyList.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
    }

    public void removeConnection(User a, User b) {
        boolean eitherUserDoesntExist = (!adjacencyList.containsKey(a) ||
            !adjacencyList.containsKey(b));
        if (eitherUserDoesntExist) {
            throw new IllegalArgumentException();
        }

        boolean connectionExists = (adjacencyList.get(a).contains(b) ||
            adjacencyList.get(b).contains(a));
        if (!connectionExists) {
            throw new IllegalArgumentException();
        }

        adjacencyList.computeIfAbsent(a, k -> new ArrayList<>()).remove(b);
        adjacencyList.computeIfAbsent(b, k -> new ArrayList<>()).remove(a);
    }

    public List<User> getConnections(User user) {
        if (!adjacencyList.containsKey(user)) {
            throw new IllegalArgumentException();
        }

        return adjacencyList.get(user);
    }

    public boolean hasUser(User user) {
        return adjacencyList.containsKey(user);
    }

    public boolean hasConnection(User a, User b) {
        if (!adjacencyList.containsKey(a) || !adjacencyList.containsKey(b)) {
            return false;
        }

        return (
            adjacencyList.get(a).contains(b) || adjacencyList.get(b).contains(a)
        );
    }

    public List<User> getUsers() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    public void printGraph() {
        /* Graph example
         * Alice -> [Bob, Carol]
         * Bob -> [Alice]
         * Carol -> [Alice]
         */

        List<User> uniqueUsers = this.getUsers();

        for (User user : uniqueUsers) {
            List<User> connections = adjacencyList.get(user);

            System.out.println(
                String.format("%s -> %s", user.getName(), connections)
            );
        }
    }
}
