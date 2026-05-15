import java.util.Objects;

public class User {

    private int id;
    private String name;
    private int age;

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format(
            "User{id=%s, name='%s', age=%s}",
            this.id,
            this.name,
            this.age
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }

        // check if the id is the same for each object
        User objUser = (User) obj;
        return this.id == objUser.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
