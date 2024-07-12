import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionDB {
    String url = "jdbc:oracle:thin:@localhost:1521:orcl"; // Adjust URL based on your setup
    String user = "hospital";
    String password = "123456";

    Connection connection = null;
    Statement statement = null;

    public boolean connect() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
            return true;
        } catch(Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        }
    }

    public boolean close() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            return true;
        } catch(Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        }
    }

    // create table
    public boolean create(String q) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(q)) {
            preparedStatement.executeUpdate();
            System.out.println("Table created successfully!");
            return true;
        } catch(Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        }
    }
    public ResultSet fetch(String sql, Object... params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement, params);
            return preparedStatement.executeQuery();
        } catch (Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return null;
        }
    }
    public boolean insert(String sql, Object... params) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setParameters(preparedStatement, params);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        }
    }

    private void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    // update value
    public boolean update(String q, Object... params) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(q)) {
            setParameters(preparedStatement, params);
            preparedStatement.executeUpdate();
            return true;
        } catch(Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        }
    }
}