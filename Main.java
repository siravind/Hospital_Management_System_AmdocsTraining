import java.sql.*;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean exit = false;

    public static void main(String[] args) {
        while (!exit) {
            displayMainMenu();
            int choice = getUserChoice();
            processChoice(choice);
        }
        System.out.println("Exiting... Goodbye!");
    }

    private static void displayMainMenu() {
        System.out.println("\nWelcome to Hospital Management System");
        System.out.println("Who are you?");
        System.out.println("1. Patient");
        System.out.println("2. Doctor");
        System.out.println("3. Admin");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character after integer input
        return choice;
    }

    private static void processChoice(int choice) {
        switch (choice) {
            case 1:
                patientMenu();
                break;
            case 2:
                doctorMenu();
                break;
            case 3:
                adminMenu();
                break;
            case 4:
                exit = true;
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
        }
    }

    private static void patientMenu() {
        System.out.println("\nPatient Menu");
        System.out.println("1. Sign Up");
        System.out.println("2. Login");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = getUserChoice();

        Patient patient = new Patient();
        switch (choice) {
            case 1:
                // Implement sign up logic here
                System.out.print("Enter patient ID: ");
                String patientId = scanner.nextLine();
                System.out.print("Enter full name: ");
                String fullName = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                System.out.print("Enter DOB (YYYY-MM-DD): ");
                String dob = scanner.nextLine();
                System.out.print("Enter government ID: ");
                String govtId = scanner.nextLine();
                System.out.print("Enter gender: ");
                String gender = scanner.nextLine();
                try {
                    if (patient.signup(patientId, fullName, password, dob, govtId, gender)) {
                        System.out.println("Sign up successful! Awaiting admin approval.");
                    } else {
                        System.out.println("Sign up failed.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                // Implement login logic here
                System.out.print("Enter patient ID: ");
                patientId = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();
                if (patient.login(patientId, password)) {
                    System.out.println("Login successful!");
                    patientLoggedInMenu(patientId);
                } else {
                    System.out.println("Login failed.");
                }
                break;
            case 3:
                // Back to main menu
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
        }
    }

    private static void patientLoggedInMenu(String patientId) {
        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            System.out.println("\nPatient Logged In Menu");
            System.out.println("1. Book Slot");
            System.out.println("2. Get Prescription");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = getUserChoice();

            Patient patient = new Patient();
            switch (choice) {
                case 1:
                    // Implement book slot logic here
                    patient.getDoctors();
                    System.out.print("Enter doctor ID: ");
                    String doctorId = scanner.nextLine();
                    System.out.print("Enter slot date (YYYY-MM-DD): ");
                    String slotDate = scanner.nextLine();
                    System.out.print("Enter your choice: ");
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
                    System.out.print("Enter slot number (1-12): ");
                    int slotNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    System.out.print("Enter payment status (0 for not paid, 1 for paid): ");
                    int paymentStatus = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    if (patient.makeAppointment(patientId, doctorId, slotNumber, slotDate)) {
                        System.out.println("Slot booked successfully!");
                    } else {
                        System.out.println("Slot booking failed.");
                    }
                    break;
                case 2:
                    // Implement get prescription logic here
                    patient.getDoctors();
                    System.out.print("Enter doctor ID: ");
                    doctorId = scanner.nextLine();
                    if (patient.getLatestPrescription(patientId)) {
                        System.out.println("Prescription retrieved successfully!");
                    } else {
                        System.out.println("Failed to retrieve prescription.");
                    }
                    break;
                case 3:
                    // Logout
                    backToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }

    private static void doctorMenu() {
        System.out.println("\nDoctor Menu");
        System.out.println("1. Sign Up");
        System.out.println("2. Login");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = getUserChoice();

        Doctor doctor = new Doctor();
        switch (choice) {
            case 1:
                // Implement sign up logic here
                try {
                    if (doctor.signup()) {
                        System.out.println("Sign up successful! Awaiting admin approval.");
                    } else {
                        System.out.println("Sign up failed.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                // Implement login logic here
                String doctorId = doctor.doctor_login();
                if (doctorId != "") {
                    doctorLoggedInMenu(doctorId);
                } else {
                    System.out.println("Login failed.");
                }
                break;
            case 3:
                // Back to main menu
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
        }
    }

    private static void doctorLoggedInMenu(String doctorId) {
        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            System.out.println("\nDoctor Logged In Menu");
            System.out.println("1. Fill Slots");
            System.out.println("2. Update Prescription");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = getUserChoice();

            Doctor doctor = new Doctor();
            switch (choice) {
                case 1:
                    // Implement fill slots logic here
                    try {
                        doctor.slotbookerHelp(doctorId);
                        System.out.println("Slot filled successfully!");
                    } catch (Exception e) {
                        System.out.println("Slot booking error");
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    // Implement update prescription logic here
                    try {
                        doctor.uploadPrescription(doctorId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Prescription error");
                    }
                    break;
                case 3:
                    // Logout
                    backToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }

    }

    private static void adminMenu() {
        System.out.println("\nAdmin Menu");
        System.out.println("1. Login");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = getUserChoice();

        Admin admin = new Admin();
        switch (choice) {
            case 1:
                // Implement login logic here
                System.out.print("Enter Admin ID: ");
                String patientId = scanner.nextLine();
                System.out.print("Enter Admin password: ");
                String password = scanner.nextLine();
                if (admin.authenticate(patientId, password)) {
                    System.out.println("Login successful!");
                    adminLoggedInMenu();
                } else {
                    System.out.println("Login failed.");
                }
                break;
            case 3:
                // Back to main menu
                break;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
        }
    }

    private static void adminLoggedInMenu() {

        Scanner scanner = new Scanner(System.in);

        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            System.out.println("\nAdmin Logged In Menu");
            System.out.println("1. Delete doctor");
            System.out.println("2. Delete patient");
            System.out.println("3. Approve doctor");
            System.out.println("4. Approve patient");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = getUserChoice();

            Admin admin = new Admin();
            Patient p1 = new Patient();
            switch (choice) {
                case 1:
                    // delete doctor id
                    p1.getDoctors();
                    System.out.print("Enter doctor ID: ");
                    String doctorId = scanner.nextLine();
                    admin.deleteDoctor(doctorId);

                    break;
                case 2:
                    // Implement get prescription logic here
                    p1.getPatients();
                    System.out.print("Enter patient ID: ");
                    String patientId = scanner.nextLine();
                    admin.deletePatient(patientId);
                    break;
                case 3:
                    // approve the doctors
                    p1.getNotApprovedDoctors();
                    System.out.print("Enter Doctor ID: ");
                    String docid = scanner.nextLine();
                    admin.approveDoctor(docid);
                    break;
                case 4:
                    // approve the patients
                    p1.getNotApprovedPatients();
                    System.out.print("Enter patient ID: ");
                    patientId = scanner.nextLine();
                    admin.approvePatient(patientId);
                    break;
                case 5:
                    // Logout
                    backToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }
}