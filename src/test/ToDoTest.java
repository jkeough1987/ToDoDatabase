import com.sun.tools.javac.comp.Todo;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by joshuakeough on 9/28/16.
 */
public class ToDoTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        ToDo.createTables(conn);
        return conn;
    }

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertUser(conn, "alice");
        User user = ToDo.selectUser(conn, "alice");
        conn.close();
        assertTrue(user != null);
    }

    @Test
    public void testSelectToDos() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertUser(conn, "alice");
        ToDo.insertToDo(conn,"Hello",1);
        ToDo.insertToDo(conn, "This is a reply",1);
        ToDo.insertToDo(conn,"This is another reply",1);

        ArrayList<ToDoItem> items = ToDo.selectToDos(conn, 1);
        conn.close();

        assertTrue(items.size()== 3);
    }

    @Test
    public void testDeleteToDos() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertUser(conn, "alice");
        ToDo.insertToDo(conn,"Hello",1);
        ToDo.deleteItem(conn,1);
        ArrayList<ToDoItem> items = ToDo.selectToDos(conn, 1);


        conn.close();

        assertTrue(items.size()==0);

    }
    @Test
    public void testToggleItem() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertUser(conn, "alice");
        ToDo.insertToDo(conn,"Hello",1);
        ToDo.toggleToDo(conn, 1);
        ArrayList<ToDoItem> items = ToDo.selectToDos(conn, 1);
        ToDoItem item = items.get(0);


        conn.close();

        assertTrue(item.isDone());


    }

}