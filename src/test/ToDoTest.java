import com.sun.tools.javac.comp.Todo;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

}