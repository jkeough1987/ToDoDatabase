import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;
import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by joshuakeough on 9/6/16.
 */
public class ToDo {
    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) throws SQLException {

        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        System.out.println("Please enter your name");
        String userName = scanner.nextLine();
        insertUser(conn, userName);
        User user = selectUser(conn, userName);
        ArrayList<ToDoItem> items = selectToDos(conn, user.getUserId());


        while (true) {
            System.out.println("1. Create to-do item");
            System.out.println("2. Toggle to-do item");
            System.out.println("3. List to-do items");
            System.out.println("4. Delete item");

            String option = scanner.nextLine();

            if (option.equals("1")) {
                System.out.println("Enter your to-do item:");
                String text = scanner.nextLine();

                ToDoItem item = new ToDoItem(text, false);
                insertToDo(conn, text, user.getUserId());
            } else if (option.equals("2")) {
                System.out.println("Enter the number of the item you want to toggle:");
                int itemNum = Integer.valueOf(scanner.nextLine());
                toggleToDo(conn, itemNum);
            } else if (option.equals("3")) {

                ArrayList<ToDoItem> todos = selectToDos(conn, user.getUserId());
                for (ToDoItem todo : todos) {
                    String checkbox = "[ ] ";
                    if (todo.isDone()) {
                        checkbox = "[x] ";
                    }
                    System.out.println(checkbox + todo.getId() + ". " + todo.getText());

                }
            } else if (option.equals("4")) {
                System.out.println("Please enter the number to delete");
                int deleteNum = Integer.valueOf(scanner.nextLine());
                deleteItem(conn, deleteNum);
            }else{
                System.out.println("Invalid option");
            }

        }
    }

    public static void insertUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL , ?)");
        stmt.setString(1, name);
        stmt.execute();
    }

    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            int id = results.getInt("id");
            return new User(id, name);
        }
        return null;
    }

    public static void insertToDo(Connection conn, String text, int userId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO todos VALUES (NULL , ?, false, ?)");
        stmt.setString(1, text);
        stmt.setInt(2, userId);
        stmt.execute();
    }

    public static ToDoItem selectToDo(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos INNER JOIN users on todos.user_id = users.id WHERE todos.id = ?");
        ResultSet results = stmt.executeQuery();
        stmt.setInt(1, id);
        if (results.next()) {
            int itemId = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean(("is_done"));
            int userId = results.getInt("user_id");
            return new ToDoItem(itemId, text, isDone, userId);
        }
        return null;
    }

    public static ArrayList<ToDoItem> selectToDos(Connection conn, int currentId) throws SQLException {
        ArrayList<ToDoItem> items1 = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT *FROM todos INNER JOIN users ON todos.user_id = users.id WHERE users.id = ?");
        stmt.setInt(1, currentId);
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean isDone = results.getBoolean(("is_done"));
            int userId = results.getInt("user_id");
            items1.add(new ToDoItem(id, text, isDone, userId));
        }
        return items1;
    }

    public static void toggleToDo(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE todos SET is_done = NOT is_done WHERE id= ?");
        System.out.println("toggleToDo parm id = " + id);
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY , text VARCHAR, is_done BOOLEAN, user_id INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY , name VARCHAR)");
    }

    public static void deleteItem(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos WHERE id= ?");
        stmt.setInt(1, id);
        stmt.execute();
    }
}
