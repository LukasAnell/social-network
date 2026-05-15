import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphStorage {

    /* Sample file format
     * USERS
     * 1,Alice,23
     * 2,Bob,25
     * 3,Carol,22
     * CONNECTIONS
     * 1,2
     * 1,3
     *
     * ---
     *
     * USERS: id,name,age
     * CONNECTIONS: id1,id2
     */

    public static void saveGraph(Graph graph, String filepath)
        throws IOException {
        try (
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))
        ) {
            // USERS header
            writer.write("USERS");
            writer.newLine();

            // write CSV for each unique user in the graph
            List<User> users = graph.getUsers();
            for (User user : users) {
                writer.write(
                    String.format(
                        "%s,%s,%s",
                        user.getId(),
                        user.getName(),
                        user.getAge()
                    )
                );

                writer.newLine();
            }

            // CONNECTIONS header
            writer.write("CONNECTIONS");
            writer.newLine();

            // write CSV for each unique connection pair (id only)
            for (User user : users) {
                List<User> connections = graph.getConnections(user);

                for (User u : connections) {
                    if (user.getId() < u.getId()) {
                        writer.write(
                            String.format("%s,%s", user.getId(), u.getId())
                        );
                        writer.newLine();
                    }
                }
            }
        }
    }

    public static Graph loadGraph(String filePath) throws IOException {
        Graph graph = new Graph();

        try (
            BufferedReader reader = new BufferedReader(new FileReader(filePath))
        ) {
            reader.readLine();
            String line;

            // loop until line is CONNECTIONS, then loop again for the connections
            while (
                (line = reader.readLine()) != null &&
                !line.equals("CONNECTIONS")
            ) {
                String[] sep = line.strip().split(",");
                User currentUser = new User(
                    Integer.parseInt(sep[0]),
                    sep[1],
                    Integer.parseInt(sep[2])
                );

                graph.addUser(currentUser);
            }

            // get list of unique users in graph to check against ids read from line
            List<User> uniqueUsers = graph.getUsers();

            // loop through connections until EOF
            while ((line = reader.readLine()) != null) {
                String[] sep = line.strip().split(",");

                List<User> matchedUsers = new ArrayList<>();

                // loop through unique users and find the ones that have the same id as in sep
                // then add a connection going both ways
                for (User u : uniqueUsers) {
                    if (
                        (u.getId() == Integer.parseInt(sep[0])) ||
                        (u.getId() == Integer.parseInt(sep[1]))
                    ) {
                        matchedUsers.add(u);
                    }
                }

                // add connection
                graph.addConnection(matchedUsers.get(0), matchedUsers.get(1));
            }
        }

        return graph;
    }
}
