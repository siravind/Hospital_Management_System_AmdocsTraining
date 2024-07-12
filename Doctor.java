import java.security.spec.ECField;
import java.sql.*;
import java.sql.Date;
import java.text.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Doctor {
    public ConnectionDB connection;

    String phoneNumber = "";
    String fullName = "";
    String password = "";
    String dateOfBirth = "";
    java.sql.Date sqlDate = null;
    String aadharNumber = "";
    String gender = "";
    int dept = 1;
    int fees = 0;
    int slotMax= 0;
    String formattedRegistrationTime;
    int approvalStatus;


    // 1. signup function
    //*********************************************************************************************************

    public boolean signup() throws SQLException {
        try {
            Scanner scanner = new Scanner(System.in);
            connection = new ConnectionDB();
            connection.connect();

            boolean validInput = false;

            // taking phone number as input
            while (!validInput) {
                try {
                    System.out.print("Please enter a 10-digit phone number: ");
                    phoneNumber = scanner.nextLine();

                    // Check if the input consists of exactly 10 digits
                    if (phoneNumber.matches("\\d{10}")) {
                        validInput = true;
                    } else {
                        throw new NumberFormatException(); // Throw exception if input doesn't match criteria
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a 10-digit phone number.");
                }
            }

            // taking full name as input

            validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Please enter your full name (only characters and spaces): ");
                    fullName = scanner.nextLine();

                    // Check if the input consists only of letters and spaces
                    if (fullName.matches("[a-zA-Z\\s]+")) {
                        validInput = true;
                    } else {
                        throw new IllegalArgumentException(); // Throw exception if input doesn't match criteria
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input. Please enter your full name using only characters and spaces.");
                }
            }

            // taking password as input

            validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Please enter your password: ");
                    password = scanner.nextLine();

                    // Check if the password meets the criteria
                    if (password.matches("^.{8,}$")) {
                        validInput = true;
                    } else {
                        throw new IllegalArgumentException(); // Throw exception if input doesn't match criteria
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid password format. Please enter a valid password.");
                }
            }

            // take date of birth as input

            validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Please enter your date of birth yyyy-MM-dd: ");
                    dateOfBirth = scanner.nextLine().trim();

                    // Check if the date of birth meets the criteria
                    if (isValidDateFormat(dateOfBirth, "yyyy-MM-dd")) {
                        validInput = true;
                    } else {
                        throw new IllegalArgumentException(); // Throw exception if input doesn't match criteria
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date of birth format. Please enter a valid date yyyy-MM-dd");
                }
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = sdf.parse(dateOfBirth);
                sqlDate = new java.sql.Date(date.getTime());
            } catch (ParseException e) {
                System.out.println("Error parsing date: " + e.getMessage());
            }

            // taking aadhar as input

            validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Please enter your Aadhar number (12 digits): ");
                    aadharNumber = scanner.nextLine();

                    // Check if Aadhar number meets the criteria
                    if (aadharNumber.matches("\\d{12}")) {
                        validInput = true;
                    } else {
                        throw new IllegalArgumentException(); // Throw exception if input doesn't match criteria
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid Aadhar number format. Please enter a valid 12-digit Aadhar number.");
                }
            }

            // taking gender as input

            validInput = false;

            while (!validInput) {
                try {
                    // Display menu for gender selection
                    System.out.println("Select your gender:");
                    System.out.println("M - Male");
                    System.out.println("F - Female");
                    System.out.println("O - Others");
                    System.out.print("Enter your choice: ");

                    // Read user input for gender choice
                    gender = scanner.nextLine().toUpperCase(); // Convert input to uppercase

                    // Check if the gender choice is valid
                    if (gender.equals("M") || gender.equals("F") || gender.equals("O")) {
                        validInput = true;
                    } else {
                        throw new IllegalArgumentException(); // Throw exception if input doesn't match criteria
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid gender selection. Please enter M, F, or O.");
                }
            }

            // taking department id as input for which department he is

            // Display department ID and department name for reference
            System.out.println("Department ID | Department Name");
            System.out.println("-----------------------------");

            // Execute SELECT query to display department ID and name
            ResultSet resultSet = connection.fetch("SELECT deptid, deptname FROM department");

            while (resultSet.next()) {
                int deptId = resultSet.getInt("deptid");
                String deptName = resultSet.getString("deptname");
                System.out.println(deptId + " | " + deptName);
            }

            validInput = false;

            // Loop until valid department name is entered
            while (!validInput) {
                try {
                    // Prompt user to enter department name
                    System.out.print("Enter a valid Department ID: ");
                    dept = scanner.nextInt();

                    // Execute query to check if department exists
                    resultSet = connection.fetch("SELECT COUNT(*) AS dept_count FROM department WHERE deptid = " + dept);
                    if (resultSet.next()) {
                        int deptCount = resultSet.getInt("dept_count");

                        // Check if department exists
                        if (deptCount > 0) {
                            validInput = true; // Exit loop if department exists
                        } else {
                            throw new IllegalArgumentException(); // Throw exception if input doesn't match criteria
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid department ID, department does not exist");
                }
            }

            // taking fees as input

            validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Enter your fees: ");
                    fees = scanner.nextInt();

                    // Check if the input consists of exactly 10 digits
                    if (fees >= 0 && fees <= 50000) {
                        validInput = true;
                    } else {
                        throw new NumberFormatException(); // Throw exception if input doesn't match criteria
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter fees between 0 and 50000");
                }
            }

            // taking max patients per slot(per hour) for booking as input

            validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Enter the number of patients you want to see per slot(per hour): ");
                    slotMax = scanner.nextInt();

                    // Check if the input consists of exactly 10 digits
                    if (slotMax > 0 && slotMax <= 5) {
                        validInput = true;
                    } else {
                        throw new NumberFormatException(); // Throw exception if input doesn't match criteria
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter value between 1 and 5");
                }
            }

            // Create a row in the patient table with the data with approvalStatus as false
            // Get current system time
            LocalDateTime registrationTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedRegistrationTime = registrationTime.format(formatter);

            String insertQuery = "INSERT INTO Doctor (doctorid, fullname, pwd, dob, govtid, gender, approvalStatus, fees, perslot, registrationTime, deptid) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,  TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?)";

            // Assuming approvalStatus is false (0) by default
            approvalStatus = 0;

            connection.insert(insertQuery, phoneNumber, fullName, password, sqlDate, aadharNumber, gender, approvalStatus, fees, slotMax, formattedRegistrationTime, dept);
            System.out.println("Sign Up Successful, Admin will approve soon..!");
            return true;
        } catch(Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        } finally {
            connection.close();
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

    //*********************************************************************************************************

    // 2. Login function
    //*********************************************************************************************************

    public String doctor_login() {

        String docId, password;

        Scanner scanner = new Scanner(System.in);
        connection = new ConnectionDB();
        connection.connect();

        System.out.println("Enter the Doctor ID: ");
        docId = scanner.nextLine();
        System.out.println("Enter the password: ");
        password = scanner.nextLine();

        if (!connection.connect()) {
            return "";
        }

        String sql = "SELECT * FROM Doctor WHERE doctorid = ? AND pwd = ?";

        try {
            ResultSet resultSet = connection.fetch(sql, docId, password);
            if (resultSet.next()) {
                if (resultSet.getInt("approvalStatus") == 1) {
                    System.out.println("Login Successful..!");
                    return docId;
                } else {
                    System.out.println("User Not Approved..! Contact Admin");
                    return "";
                }
            } else {
                System.out.println("Invalid User Details..!");
                return "";
            }
        } catch (Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return "";
        }
    }

    // *******************************************************************************************************

    // 3. Booking slot function
    public boolean slotBooker(String doctorId, String date, int slot ){
        connection = new ConnectionDB();
        connection.connect();

        String sql = "Select * from Slots where dateofSlot = ? and doctorId = ? and slot = ?";
      try {
          ResultSet resultSet = connection.fetch(sql, Date.valueOf(date), doctorId, slot);

          if (resultSet.next()) {
              System.out.println("Is already present");
              return false;
          } else {
              String s = "INSERT INTO Slots (doctorId, dateofSlot, slot) VALUES (?,?,?)";
              connection.insert(s,doctorId, Date.valueOf(date), slot);
              System.out.println("Slot Added");
              return true;
          }
      }catch (Exception e){
          System.out.println("Exception - "+e);
          return false;
      }
    }

    public void slotbookerHelp(String doc_id) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        connection = new ConnectionDB();
        connection.connect();

        boolean isValid = false;

        String date = getDate(); // Get valid date for slot booking

        while (!isValid) {
            displaySlotMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1: case 2: case 3: case 4: case 5: case 6:
                case 7: case 8: case 9: case 10: case 11: case 12:
                    try {
                        // Check if slot is already booked for the given date and doctor
                        if (slotBooker(doc_id, date, choice)) {
                            System.out.println("Slot..Set");
                        } else {
                            System.out.println("Slot is already set. Please select another slot.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 13:
                    isValid = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 13.");
            }
        }
    }
    private String getDate() {
        Scanner scanner = new Scanner(System.in);
        String date = "2024-07-13";
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Please enter date to book slot in format yyyy-MM-dd: ");
                date = scanner.nextLine().trim();

                // Check if the date of birth meets the criteria
                if (isValidDateFormat(date, "yyyy-MM-dd")) {
                    validInput = true;
                } else {
                    throw new IllegalArgumentException(); // Throw exception if input doesn't match criteria
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a valid date in format yyyy-MM-dd");
            }
        }
        return date;
    }

    private void displaySlotMenu() {
        System.out.println("\nAvailable Slots:");
        System.out.println("1. 9:00 am - 10:00 am (Slot A)");
        System.out.println("2. 10:00 am - 11:00 am (Slot B)");
        System.out.println("3. 11:00 am - 12:00 pm (Slot C)");
        System.out.println("4. 12:00 pm - 1:00 pm (Slot D)");
        System.out.println("5. 1:00 pm - 2:00 pm (Slot E)");
        System.out.println("6. 2:00 pm - 3:00 pm (Slot F)");
        System.out.println("7. 3:00 pm - 4:00 pm (Slot G)");
        System.out.println("8. 4:00 pm - 5:00 pm (Slot H)");
        System.out.println("9. 5:00 pm - 6:00 pm (Slot I)");
        System.out.println("10. 6:00 pm - 7:00 pm (Slot J)");
        System.out.println("11. 7:00 pm - 8:00 pm (Slot K)");
        System.out.println("12. 8:00 pm - 9:00 pm (Slot L)");
        System.out.println("13. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character after integer input
        return choice;
    }

//     ***********************************************************************************************************

    // 4. Upload Prescription
    //***********************************************************************************************************

    public boolean uploadPrescription(String doc_Id) {
        Scanner scanner = new Scanner(System.in);

        connection = new ConnectionDB();
        connection.connect();

        System.out.println("All paid prescriptions without any prescription:");

        try {
            String sql = "SELECT * FROM Appointments "
                    + "WHERE prescription IS NULL "
                    + "AND doctorId = " + doc_Id + " "
                    + "ORDER BY doa DESC, SLOT DESC ";
            ResultSet resultSet = connection.fetch(sql);
            System.out.println("Appointment ID         |        Patient ID");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("apptid") + "      |       " + resultSet.getString("patientid"));
            }
            int appid = 0;
            boolean validInput = false;
            while (!validInput) {
                try {
                    System.out.print("Please Enter appointment id to write prescription: ");
                    appid = scanner.nextInt();
                    validInput = true;
                    // Check if the appointment ID exists
//                    ResultSet appResultSet = connection.fetch("SELECT * FROM Appointments WHERE apptid = ?", appid);
//                    if (appResultSet.next()) {
//                        validInput = true;
//                    } else {
//                        throw new IllegalArgumentException("Appointment ID does not exist.");
//                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input. " + e.getMessage());
                }
            }
            String presc;
            System.out.println("Enter prescription");
            presc = scanner.nextLine();
            System.out.println(presc);
            String query =  "UPDATE Appointments SET prescription = ? where apptId = ?";
            connection.insert(query,presc,appid);
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred - " + e + " Try Again");
            return false;
        }
}



//**********************************************************************************************************


//**********************************************************************************************************
}