/**
 * Created by joshuakeough on 9/28/16.
 */
public class User {
    private int userId;
    private String name;

    public User(int id, String name) {
        this.userId = id;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
