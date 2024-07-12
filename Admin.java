import java.sql.*;

public class Admin {

    // hardcoded user-id, and password
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "password";
    private Connection connection;

    // Method to authenticate admin user
    public boolean authenticate(String userId, String password) {
        return ADMIN_USER.equals(userId) && ADMIN_PASSWORD.equals(password);
    }

//    public void getPatients()
    // Method to delete a patient record
    public boolean deletePatient(String pat_id) {
        ConnectionDB connection = new ConnectionDB();
        connection.connect();
        String sql = "DELETE FROM patient WHERE patientid = ?";
        try {
            connection.insert(sql, pat_id);
            System.out.println("Successfully deleted record.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to delete a doctor record
    public boolean deleteDoctor(String doc_id) {
        ConnectionDB connection = new ConnectionDB();
        connection.connect();
        String sql = "DELETE FROM Doctor WHERE doctorId = ?";
        try {
            connection.insert(sql, doc_id);
            System.out.println("Successfully deleted record.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to approve a user
    public boolean approvePatient(String patientId) {
        ConnectionDB connection = new ConnectionDB();
        connection.connect();
        String sql = "UPDATE patient SET approvalstatus = 1 WHERE patientid = ?";
        try {
            connection.insert(sql, patientId);
            System.out.println("Successfully approved.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to approve a user
    public boolean approveDoctor(String doctorId) {
        ConnectionDB connection = new ConnectionDB();
        connection.connect();
        String sql = "UPDATE Doctor SET approvalstatus = 1 WHERE doctorid = ?";
        try {
            connection.insert(sql, doctorId);
            System.out.println("Successfully approved.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}