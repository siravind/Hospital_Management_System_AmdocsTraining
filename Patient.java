import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Patient {
    String patientId;
    String fullName;
    String password;
    String DOB;
    String govtId;
    String gender;
    int approvalStatus = 0;
    Date registrationTime;
    boolean loggedIn = false;

    public java.sql.Date convertDate(String s){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            java.util.Date dobUtil = sdf.parse(s);
            java.sql.Date dob = new java.sql.Date(dobUtil.getTime());
            return dob;
        }catch(Exception e){
            System.out.println("Exception"+e);
            return null;
        }
    }

    // Method to validate date format
    private static boolean isValidDateFormat(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public boolean signup(String patientId, String fullName, String password, String dob, String govtId, String gender) throws SQLException {

        ConnectionDB obj = new ConnectionDB();
        if (!obj.connect()) {
            return false;
        }

        try {
            // Check if the patient already exists
            String checkQuery = "SELECT * FROM Patient WHERE patientId = ?";
            ResultSet resultSet = obj.fetch(checkQuery, patientId);

            if (resultSet != null && resultSet.next()) {
                System.out.println("Patient is already present..!");
                return false;
            } else {
                try {
                    // Create a row in the patient table with the data with approvalStatus as false
                    // Get current system time
                    LocalDateTime registrationTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedRegistrationTime = registrationTime.format(formatter);

                    String insertQuery = "INSERT INTO Patient (patientId, fullname, pwd, dob, govtid, gender, approvalStatus, registrationTime) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'))";

                    // Assuming approvalStatus is false (0) by default
                    int approvalStatus = 0;

                    obj.insert(insertQuery, patientId, fullName, password, Date.valueOf(dob), govtId, gender, approvalStatus, formattedRegistrationTime);
                    System.out.println("Sign Up Successful, Admin will approve soon..!");
                    return true;
                } catch (Exception e) {
                    System.out.println("Exception occurred - " + e + " Try Again");
                    return false;
                } finally {
                    obj.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        } finally {
            obj.close();
        }
    }


    // login
    public boolean login(String patientId, String password) {
        ConnectionDB obj = new ConnectionDB();
        if (!obj.connect()) {
            return false;
        }

        String sql = "SELECT * FROM Patient WHERE patientId = ? AND pwd = ?";

        try {
            ResultSet resultSet = obj.fetch(sql, patientId, password);
            if (resultSet != null && resultSet.next()) {
                if (resultSet.getInt("approvalStatus") == 1) {
                    System.out.println("Login Successful..!");
                    this.loggedIn = true;
                    return true;
                } else {
                    System.out.println("User Not Approved..! Contact Admin");
                    return false;
                }
            } else {
                System.out.println("Invalid User Details..!");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        } finally {
            obj.close();
        }
    }


    // book an Appointment

    public boolean makeAppointment(String patientId, String doctorId, int slot, String date){
        ConnectionDB obj = new ConnectionDB();
        if (!obj.connect()) {
            return false;
        }

        String s1 = "Select * from Slots where slot = ? and dateofSlot = ? and doctorId = ?";

        try {
            ResultSet rs = obj.fetch(s1, slot, Date.valueOf(date), doctorId);
            if (rs.next()) {
                System.out.println("Slot Already Booked");
                return false;
            }
        }catch (Exception e){
            System.out.println("Exception - "+e);
        }

        String sql = "INSERT INTO Appointments (patientId, doctorId, doa, slot, payment) VALUES (?, ?, ?, ?, ?)";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(date);
            Date sqlDate = new Date(parsedDate.getTime());

            obj.insert(sql, patientId, doctorId, sqlDate, slot, 1);
            System.out.println("Appointment Booked..!");
            return true;
        } catch (ParseException e) {
            System.out.println("Exception occurred while parsing date - " + e + " Try Again");
            return false;
        } catch (Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        } finally {
            obj.close();
        }
    };

    public boolean getLatestPrescription(String patientId) {
        ConnectionDB obj = new ConnectionDB();
        obj.connect();
        String sql = "Select * from Appointments where patientId = ?";

        try {
            ResultSet resultSet = obj.fetch(sql, patientId);
            while(resultSet.next()){
                System.out.println(resultSet.getString("prescription"));
            }
            return true;
        }catch(Exception e){
            System.out.println("Exception - "+e);
            return false;
        }
    };

    public boolean getDoctors(){
        ConnectionDB obj = new ConnectionDB();
        obj.connect();
        String sql = "Select fullname, doctorId, deptId, deptname from Doctor";

        try {
            ResultSet resultSet = obj.fetch(sql);
            System.out.println("Doctors Info");
            while(resultSet.next()){
                System.out.println(resultSet.getString("fullname")+" "+resultSet.getString("doctorId")+" "+resultSet.getInt("deptId")+" "+resultSet.getString("deptname"));
            }
            return true;
        }catch(Exception e){
            System.out.println("Exception - "+e);
            return false;
        }
    }

    public boolean getPatients(){
        ConnectionDB obj = new ConnectionDB();
        obj.connect();
        String sql = "Select fullname, patientId, approvalstatus from Patient";

        try {
            ResultSet resultSet = obj.fetch(sql);
            System.out.println("Patients Info");
            while(resultSet.next()){
                System.out.println(resultSet.getString("fullname")+" "+resultSet.getString("patientId")+" "+resultSet.getInt("approvalstatus"));
            }
            return true;
        }catch(Exception e){
            System.out.println("Exception - "+e);
            return false;
        }
    }

    public boolean getNotApprovedPatients() {
        ConnectionDB obj = new ConnectionDB();
        obj.connect();
        String sql = "Select fullname, patientId, approvalstatus from Patient WHERE approvalstatus = 0";

        try {
            ResultSet resultSet = obj.fetch(sql);
            System.out.println("Patients Info");
            while(resultSet.next()){
                System.out.println(resultSet.getString("fullname")+" "+resultSet.getString("patientId")+" "+resultSet.getInt("approvalstatus"));
            }
            return true;
        }catch(Exception e){
            System.out.println("Exception - "+e);
            return false;
        }
    }
    public boolean getNotApprovedDoctors(){
        ConnectionDB obj = new ConnectionDB();
        obj.connect();
        String sql = "Select fullname, doctorId, deptId, deptname, approvalstatus from Doctor WHERE approvalstatus = 0";

        try {
            ResultSet resultSet = obj.fetch(sql);
            System.out.println("Doctors Info");
            while(resultSet.next()){
                System.out.println(resultSet.getString("fullname")+" "+resultSet.getString("doctorId")+" "+resultSet.getInt("deptId")+" "+resultSet.getString("deptname"));
            }
            return true;
        }catch(Exception e){
            System.out.println("Exception - " + e);
            return false;
        }
    }


}
