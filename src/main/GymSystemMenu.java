package main;

import model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GymSystemMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Gym gym = new Gym();
    private static Administrator admin;
    private static Instructor instructor;
    private static Member member;
    private static final List<Administrator> administrators = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(GymSystemMenu.class.getName());
    private static Attendance currentAttendance = null; // ADICIONADO: Gerenciar attendance ativo

    public static void main(String[] args) {
        try {
            initializeSystem();

            boolean running = true;
            while (running) {
                try {
                    displayMainMenu();
                    int choice = getIntInput();

                    switch (choice) {
                        case 1 -> administratorMenu();
                        case 2 -> instructorMenu();
                        case 3 -> memberMenu();
                        case 4 -> gymMenu();
                        case 5 -> equipmentMenu();
                        case 6 -> attendanceMenu();
                        case 7 -> registerNewAdministrator();
                        case 8 -> listAllAdministrators();
                        case 9 -> runAutomatedTests();
                        case 10 -> switchUserMenu();  // NOVA CHAMADA
                        case 0 -> {
                            running = false;
                            System.out.println("Exiting system. Thank you!");
                        }
                        default -> System.out.println("Invalid option. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("Error in main menu: " + e.getMessage());
                    scanner.nextLine();
                }
            }
        } catch (Exception e) {
            System.out.println("Critical system error: " + e.getMessage());
            logger.log(Level.SEVERE, "Critical system error", e);
        } finally {
            scanner.close();
        }
    }

    private static void initializeSystem() {
        try {
            admin = new Administrator("Admin a", "admina@gym.com", "123", "123");
            administrators.add(admin);

            instructor = new Instructor("Instrutor a", "instrutora@gym.com", "123", "123", "Physical Education");
            member = new Member("Member a", "membera@gym.com", "123", "123");

            gym.setName("Gym A");
            gym.setPhone("1234");
            gym.setWebsite("www.gyma.com");

            Address address = new Address();
            address.setStreet("Rua A");
            address.setNumber("100");
            address.setCity("Caxias do Sul");
            address.setState("RS");
            address.setZipCode("12345");
            gym.setAddress(address);

            gym.registerInstructor(instructor);
            gym.registerMember(member);

            System.out.println("=== Gym Management System Initialized ===");
            System.out.println("Default Admin: " + admin.getName() + " (ID: " + admin.getId() + ")\n");
        } catch (Exception e) {
            System.out.println("Error initializing system: " + e.getMessage());
            throw new RuntimeException("System initialization failed", e);
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n========== GYM MANAGEMENT SYSTEM ==========");
        System.out.println("1. Administrator Operations");
        System.out.println("2. Instructor Operations");
        System.out.println("3. Member Operations");
        System.out.println("4. Gym Operations");
        System.out.println("5. Equipment Operations");
        System.out.println("6. Attendance Operations");
        System.out.println("7. Register New Administrator");
        System.out.println("8. List All Administrators");
        System.out.println("9. Run Automated Tests");
        System.out.println("10. Switch User");  // NOVA OPÇÃO
        System.out.println("0. Exit");
        System.out.print("Select option: ");
    }

    // ===== AUTOMATED TESTS =====
    private static void runAutomatedTests() {
        System.out.println("\n========== RUNNING AUTOMATED TESTS ==========\n");

        try {
            System.out.println("Test 1: Creating new Administrator...");
            Administrator testAdmin = new Administrator("Test Admin", "test@gym.com", "111-222", "pass123");
            administrators.add(testAdmin);
            System.out.println("✓ Admin created: " + testAdmin.getName());

            System.out.println("\nTest 2: Registering Instructor...");
            Instructor testInstructor = new Instructor("Test Instructor", "inst@gym.com", "333-444", "pass123", "Certified Trainer");
            admin.registerInstructor(gym, testInstructor);
            System.out.println("✓ Instructor registered");

            System.out.println("\nTest 3: Registering Member...");
            Member testMember = new Member("Test Member", "member@gym.com", "555-666", "pass123");
            admin.registerMember(gym, testMember);
            System.out.println("✓ Member registered");

            System.out.println("\nTest 4: Registering Equipment...");
            Equipment testEquipment = new Equipment();
            testEquipment.setName("Treadmill");
            testEquipment.setDescription("Cardio equipment");
            testEquipment.setFunction("Running");
            admin.registerEquipment(gym, testEquipment);
            System.out.println("✓ Equipment registered");

            System.out.println("\nTest 5: Updating Gym Info...");
            admin.updateGymInfo(gym, "SuperFit Gym", "999-888", "www.superfit.com");
            System.out.println("✓ Gym info updated");

            System.out.println("\nTest 6: Defining Training...");
            Training training = new Training();
            training.setWeekDay(Weekday.MONDAY);
            testInstructor.defineTraining(testMember, training);
            System.out.println("✓ Training defined for " + Weekday.MONDAY);

            System.out.println("\nTest 7: Registering Progress...");
            Progress progress = new Progress();
            progress.setDate(LocalDate.now());
            progress.setWeight(75.5);
            progress.setMuscleMassPercent(30.0);
            testInstructor.registerProgress(testMember, progress);
            System.out.println("✓ Progress registered");

            System.out.println("\nTest 8: Testing Attendance...");
            Attendance attendance = new Attendance();
            attendance.registerEntry();
            Thread.sleep(1000);
            attendance.registerExit();
            System.out.println("✓ Attendance recorded: " + attendance.calculateHours() + " hours");

            System.out.println("\nTest 9: Testing Search...");
            Member foundMember = gym.findMemberByName("Test Member");
            Instructor foundInstructor = gym.findInstructorByName("Test Instructor");
            System.out.println("✓ Found member: " + (foundMember != null ? foundMember.getName() : "null"));
            System.out.println("✓ Found instructor: " + (foundInstructor != null ? foundInstructor.getName() : "null"));

            System.out.println("\nTest 10: Testing Remove Operations...");
            admin.removeMember(gym, testMember);
            admin.removeInstructor(gym, testInstructor);
            admin.removeEquipment(gym, testEquipment);
            System.out.println("✓ All test entities removed");

            System.out.println("\nTest 11: Testing Login...");
            admin.login("123");
            System.out.println("✓ Login tested");

            System.out.println("\nTest 12: Testing Update Profile...");
            member.updateProfile("newemail@test.com", "777-888");
            System.out.println("✓ Profile updated");

            System.out.println("\nTest 13: Testing Schedule...");
            Schedule schedule = new Schedule();
            schedule.setOpeningTime(LocalTime.of(6, 0));
            schedule.setClosingTime(LocalTime.of(22, 0));
            admin.updateGymSchedule(gym, Weekday.MONDAY, schedule);
            System.out.println("✓ Schedule set for MONDAY: 06:00 - 22:00");

            System.out.println("\n========== ALL TESTS COMPLETED SUCCESSFULLY! ==========\n");

        } catch (InterruptedException e) {
            System.out.println("\n❌ TEST INTERRUPTED: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\n❌ TEST VALIDATION ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ TEST FAILED: " + e.getMessage());
            logger.log(Level.SEVERE, "Automated test failed", e);
        }
    }

    // ===== SWITCH USER MENU =====
    private static void switchUserMenu() {
        try {
            System.out.println("\n--- Switch User ---");
            System.out.println("1. Switch Administrator");
            System.out.println("2. Switch Instructor");
            System.out.println("3. Switch Member");
            System.out.println("4. View Current Users");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> switchAdministrator();
                case 2 -> switchInstructor();
                case 3 -> switchMember();
                case 4 -> viewCurrentUsers();
                case 0 -> {}
                default -> System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.out.println("Error in switch user menu: " + e.getMessage());
        }
    }

    private static void switchAdministrator() {
        try {
            System.out.println("\n--- Switch Administrator ---");
            if (administrators.isEmpty()) {
                System.out.println("No administrators available.");
                return;
            }

            System.out.println("Available Administrators:");
            for (int i = 0; i < administrators.size(); i++) {
                Administrator a = administrators.get(i);
                System.out.println((i + 1) + ". " + a.getName() + " (ID: " + a.getId() + ")" +
                        (a == admin ? " [CURRENT]" : ""));
            }

            System.out.print("\nSelect administrator (1-" + administrators.size() + "): ");
            int choice = getIntInput();

            if (choice > 0 && choice <= administrators.size()) {
                admin = administrators.get(choice - 1);
                System.out.println("✓ Switched to administrator: " + admin.getName());
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Error switching administrator: " + e.getMessage());
        }
    }

    private static void switchInstructor() {
        try {
            System.out.println("\n--- Switch Instructor ---");
            if (gym.getInstructors().isEmpty()) {
                System.out.println("No instructors available.");
                return;
            }

            System.out.println("Available Instructors:");
            List<Instructor> instructors = gym.getInstructors();
            for (int i = 0; i < instructors.size(); i++) {
                Instructor inst = instructors.get(i);
                System.out.println((i + 1) + ". " + inst.getName() + " (ID: " + inst.getId() + ")" +
                        (inst == instructor ? " [CURRENT]" : ""));
            }

            System.out.print("\nSelect instructor (1-" + instructors.size() + "): ");
            int choice = getIntInput();

            if (choice > 0 && choice <= instructors.size()) {
                instructor = instructors.get(choice - 1);
                System.out.println("✓ Switched to instructor: " + instructor.getName());
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Error switching instructor: " + e.getMessage());
        }
    }

    private static void switchMember() {
        try {
            System.out.println("\n--- Switch Member ---");
            if (gym.getMembers().isEmpty()) {
                System.out.println("No members available.");
                return;
            }

            System.out.println("Available Members:");
            List<Member> members = gym.getMembers();
            for (int i = 0; i < members.size(); i++) {
                Member m = members.get(i);
                System.out.println((i + 1) + ". " + m.getName() + " (ID: " + m.getId() + ")" +
                        (m == member ? " [CURRENT]" : ""));
            }

            System.out.print("\nSelect member (1-" + members.size() + "): ");
            int choice = getIntInput();

            if (choice > 0 && choice <= members.size()) {
                member = members.get(choice - 1);
                System.out.println("✓ Switched to member: " + member.getName());
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (Exception e) {
            System.out.println("Error switching member: " + e.getMessage());
        }
    }

    private static void viewCurrentUsers() {
        try {
            System.out.println("\n--- Current Active Users ---");
            System.out.println("Administrator: " + admin.getName() + " (ID: " + admin.getId() + ")");
            System.out.println("Instructor: " + instructor.getName() + " (ID: " + instructor.getId() + ")");
            System.out.println("Member: " + member.getName() + " (ID: " + member.getId() + ")");
        } catch (Exception e) {
            System.out.println("Error viewing current users: " + e.getMessage());
        }
    }

    // ===== ADMINISTRATOR REGISTRATION =====
    private static void registerNewAdministrator() {
        try {
            System.out.println("\n--- Register New Administrator ---");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println("Error: Name cannot be empty.");
                return;
            }

            System.out.print("Email: ");
            String email = scanner.nextLine();
            if (email.trim().isEmpty()) {
                System.out.println("Error: Email cannot be empty.");
                return;
            }

            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            if (password.trim().isEmpty()) {
                System.out.println("Error: Password cannot be empty.");
                return;
            }

            Administrator newAdmin = new Administrator(name, email, phone, password);
            administrators.add(newAdmin);

            System.out.println("\n✓ Administrator registered successfully!");
            System.out.println("ID: " + newAdmin.getId());
            System.out.println("Name: " + newAdmin.getName());
            System.out.println("Email: " + newAdmin.getEmail());

            System.out.print("\nSet as active admin? (y/n): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("y")) {
                admin = newAdmin;
                System.out.println("✓ Active admin changed to: " + admin.getName());
            }
        } catch (Exception e) {
            System.out.println("Error registering administrator: " + e.getMessage());
        }
    }

    private static void listAllAdministrators() {
        try {
            System.out.println("\n--- All Administrators ---");
            if (administrators.isEmpty()) {
                System.out.println("No administrators registered.");
            } else {
                for (Administrator a : administrators) {
                    System.out.println("- ID: " + a.getId() +
                            " | Name: " + a.getName() +
                            " | Email: " + a.getEmail() +
                            (a == admin ? " [ACTIVE]" : ""));
                }
                System.out.println("\nTotal: " + administrators.size() + " administrator(s)");
            }
        } catch (Exception e) {
            System.out.println("Error listing administrators: " + e.getMessage());
        }
    }

    // ===== ADMINISTRATOR MENU =====
    private static void administratorMenu() {
        boolean back = false;
        while (!back) {
            try {
                System.out.println("\n--- Administrator Menu ---");
                System.out.println("Current Admin: " + admin.getName());
                System.out.println("1. Register New Instructor");
                System.out.println("2. Register New Member");
                System.out.println("3. Register Equipment");
                System.out.println("4. Remove Instructor");
                System.out.println("5. Remove Member");
                System.out.println("6. Update Gym Info");
                System.out.println("7. Update Gym Schedule");
                System.out.println("8. Admin Login Test");
                System.out.println("9. Switch Active Admin");
                System.out.println("0. Back to Main Menu");
                System.out.print("Select option: ");

                int choice = getIntInput();

                switch (choice) {
                    case 1 -> registerNewInstructor();
                    case 2 -> registerNewMember();
                    case 3 -> registerEquipment();
                    case 4 -> removeInstructor();
                    case 5 -> removeMember();
                    case 6 -> updateGymInfo();
                    case 7 -> updateGymSchedule();
                    case 8 -> testAdminLogin();
                    case 9 -> switchActiveAdmin();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error in administrator menu: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void switchActiveAdmin() {
        try {
            System.out.println("\n--- Switch Active Administrator ---");
            listAllAdministrators();
            System.out.print("\nEnter Admin ID to set as active: ");
            long id = Long.parseLong(scanner.nextLine());

            for (Administrator a : administrators) {
                if (a.getId() == id) {
                    admin = a;
                    System.out.println("✓ Active admin changed to: " + admin.getName());
                    return;
                }
            }
            System.out.println("Administrator with ID " + id + " not found.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid ID format. Please enter a number.");
        } catch (Exception e) {
            System.out.println("Error switching admin: " + e.getMessage());
        }
    }

    private static void registerNewInstructor() {
        try {
            System.out.println("\n--- Register New Instructor ---");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");

            System.out.print("Email: ");
            String email = scanner.nextLine();
            if (email.trim().isEmpty()) throw new IllegalArgumentException("Email cannot be empty");

            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            if (password.trim().isEmpty()) throw new IllegalArgumentException("Password cannot be empty");

            System.out.print("Education: ");
            String education = scanner.nextLine();
            if (education.trim().isEmpty()) throw new IllegalArgumentException("Education cannot be empty");

            Instructor newInstructor = new Instructor(name, email, phone, password, education);
            admin.registerInstructor(gym, newInstructor);
            System.out.println("✓ Instructor registered successfully!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error registering instructor: " + e.getMessage());
        }
    }

    private static void registerNewMember() {
        try {
            System.out.println("\n--- Register New Member ---");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");

            System.out.print("Email: ");
            String email = scanner.nextLine();
            if (email.trim().isEmpty()) throw new IllegalArgumentException("Email cannot be empty");

            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            if (password.trim().isEmpty()) throw new IllegalArgumentException("Password cannot be empty");

            Member newMember = new Member(name, email, phone, password);
            admin.registerMember(gym, newMember);
            System.out.println("✓ Member registered successfully!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error registering member: " + e.getMessage());
        }
    }

    private static void registerEquipment() {
        try {
            System.out.println("\n--- Register Equipment ---");
            System.out.print("Equipment Name: ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) throw new IllegalArgumentException("Equipment name cannot be empty");

            System.out.print("Description: ");
            String description = scanner.nextLine();
            System.out.print("Function: ");
            String function = scanner.nextLine();

            Equipment equipment = new Equipment();
            equipment.setName(name);
            equipment.setDescription(description);
            equipment.setFunction(function);

            admin.registerEquipment(gym, equipment);
            System.out.println("✓ Equipment registered successfully!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error registering equipment: " + e.getMessage());
        }
    }

    private static void removeInstructor() {
        try {
            System.out.println("\n--- Remove Instructor ---");
            listAllInstructors();
            System.out.print("\nInstructor Name to Remove: ");
            String name = scanner.nextLine();
            Instructor toRemove = gym.findInstructorByName(name);

            if (toRemove != null) {
                admin.removeInstructor(gym, toRemove);
                System.out.println("✓ Instructor removed successfully!");
            } else {
                System.out.println("Instructor not found.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error removing instructor: " + e.getMessage());
        }
    }

    private static void removeMember() {
        try {
            System.out.println("\n--- Remove Member ---");
            listAllMembers();
            System.out.print("\nMember Name to Remove: ");
            String name = scanner.nextLine();
            Member toRemove = gym.findMemberByName(name);

            if (toRemove != null) {
                admin.removeMember(gym, toRemove);
                System.out.println("✓ Member removed successfully!");
            } else {
                System.out.println("Member not found.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error removing member: " + e.getMessage());
        }
    }

    private static void updateGymInfo() {
        try {
            System.out.println("\n--- Update Gym Info ---");
            System.out.println("Current Info:");
            gym.viewGymInfo();
            System.out.println("\nEnter new values (press Enter to skip):");
            System.out.print("New Name: ");
            String name = scanner.nextLine();
            System.out.print("New Phone: ");
            String phone = scanner.nextLine();
            System.out.print("New Website: ");
            String website = scanner.nextLine();

            admin.updateGymInfo(gym, name.isEmpty() ? null : name,
                    phone.isEmpty() ? null : phone,
                    website.isEmpty() ? null : website);
            System.out.println("✓ Gym info updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error updating gym info: " + e.getMessage());
        }
    }

    private static void updateGymSchedule() {
        try {
            System.out.println("\n--- Update Gym Schedule ---");
            System.out.println("Select Day:");
            for (int i = 0; i < Weekday.values().length; i++) {
                System.out.println((i + 1) + ". " + Weekday.values()[i]);
            }
            System.out.print("Choice: ");
            int dayChoice = getIntInput() - 1;

            if (dayChoice >= 0 && dayChoice < Weekday.values().length) {
                System.out.print("Opening Time (HH:MM): ");
                String openTime = scanner.nextLine();
                System.out.print("Closing Time (HH:MM): ");
                String closeTime = scanner.nextLine();

                Schedule schedule = new Schedule();
                schedule.setOpeningTime(LocalTime.parse(openTime));
                schedule.setClosingTime(LocalTime.parse(closeTime));

                admin.updateGymSchedule(gym, Weekday.values()[dayChoice], schedule);
                System.out.println("✓ Schedule updated successfully!");
            } else {
                System.out.println("Invalid day selection.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid time format. Use HH:MM (e.g., 08:00)");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error updating schedule: " + e.getMessage());
        }
    }

    private static void testAdminLogin() {
        try {
            System.out.println("\n--- Test Admin Login ---");
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            admin.login(password);
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }

    // ===== INSTRUCTOR MENU =====
    private static void instructorMenu() {
        boolean back = false;
        while (!back) {
            try {
                System.out.println("\n--- Instructor Menu ---");
                System.out.println("Current Instructor: " + instructor.getName());
                System.out.println("1. Define Training for Member");
                System.out.println("2. Register Member Progress");
                System.out.println("3. View Defined Trainings");
                System.out.println("4. Update Education");
                System.out.println("0. Back to Main Menu");
                System.out.print("Select option: ");

                int choice = getIntInput();

                switch (choice) {
                    case 1 -> defineTraining();
                    case 2 -> registerProgress();
                    case 3 -> viewDefinedTrainings();
                    case 4 -> updateEducation();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error in instructor menu: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void defineTraining() {
        try {
            System.out.println("\n--- Define Training ---");
            System.out.print("Member Name: ");
            String memberName = scanner.nextLine();
            Member targetMember = gym.findMemberByName(memberName);

            if (targetMember == null) {
                System.out.println("Member not found.");
                return;
            }

            System.out.println("Select Weekday:");
            for (int i = 0; i < Weekday.values().length; i++) {
                System.out.println((i + 1) + ". " + Weekday.values()[i]);
            }
            System.out.print("Choice: ");
            int dayChoice = getIntInput() - 1;

            if (dayChoice < 0 || dayChoice >= Weekday.values().length) {
                System.out.println("Invalid day selection.");
                return;
            }

            Training training = new Training();
            training.setWeekDay(Weekday.values()[dayChoice]);

            boolean addingExercises = true;
            int exerciseOrder = 1;

            while (addingExercises) {
                try {
                    System.out.println("\n--- Add Exercise #" + exerciseOrder + " ---");

                    if (gym.getEquipments().isEmpty()) {
                        System.out.println("No equipment available. Please register equipment first.");
                        break;
                    }

                    System.out.println("Available Equipment:");
                    List<Equipment> equipments = new ArrayList<>(gym.getEquipments());
                    for (int i = 0; i < equipments.size(); i++) {
                        System.out.println((i + 1) + ". " + equipments.get(i).getName());
                    }
                    System.out.print("Select equipment (or 0 to finish): ");
                    int equipChoice = getIntInput();

                    if (equipChoice == 0) {
                        addingExercises = false;
                        continue;
                    }

                    if (equipChoice > 0 && equipChoice <= equipments.size()) {
                        Equipment selectedEquipment = equipments.get(equipChoice - 1);

                        System.out.print("Weight (kg): ");
                        double weight = getDoubleInput();

                        System.out.print("Repetitions: ");
                        int reps = getIntInput();

                        Exercise exercise = new Exercise();
                        exercise.setEquipment(selectedEquipment);
                        exercise.setWeight(weight);
                        exercise.setRepetitions(reps);
                        exercise.setOrder(exerciseOrder);

                        training.addExercise(exercise);
                        System.out.println("✓ Exercise added!");

                        exerciseOrder++;

                        System.out.print("\nAdd another exercise? (y/n): ");
                        String choice = scanner.nextLine();
                        if (!choice.equalsIgnoreCase("y")) {
                            addingExercises = false;
                        }
                    } else {
                        System.out.println("Invalid equipment selection.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid input format.");
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Error adding exercise: " + e.getMessage());
                }
            }

            if (training.getExercises().isEmpty()) {
                System.out.println("Training must have at least one exercise.");
            } else {
                instructor.defineTraining(targetMember, training);
                System.out.println("\n✓ Training defined successfully for " + Weekday.values()[dayChoice]);
                System.out.println("Total exercises: " + training.getExercises().size());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error defining training: " + e.getMessage());
        }
    }

    private static void registerProgress() {
        try {
            System.out.println("\n--- Register Progress ---");
            System.out.print("Member Name: ");
            String memberName = scanner.nextLine();
            Member targetMember = gym.findMemberByName(memberName);

            if (targetMember != null) {
                System.out.print("Weight (kg): ");
                double weight = getDoubleInput();
                System.out.print("Muscle Mass Percent: ");
                double muscleMass = getDoubleInput();

                Progress progress = new Progress();
                progress.setDate(LocalDate.now());
                progress.setWeight(weight);
                progress.setMuscleMassPercent(muscleMass);

                instructor.registerProgress(targetMember, progress);
                System.out.println("✓ Progress registered successfully!");
            } else {
                System.out.println("Member not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid number format.");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error registering progress: " + e.getMessage());
        }
    }

    private static void viewDefinedTrainings() {
        try {
            System.out.println("\n--- Defined Trainings ---");
            System.out.println("Total trainings: " + instructor.getDefinedTrainings().size());
            for (Training t : instructor.getDefinedTrainings()) {
                System.out.println("- " + t.getWeekDay() + " (" + t.getExercises().size() + " exercises)");
            }
        } catch (Exception e) {
            System.out.println("Error viewing trainings: " + e.getMessage());
        }
    }

    private static void updateEducation() {
        try {
            System.out.println("\n--- Update Education ---");
            System.out.print("New Education: ");
            String education = scanner.nextLine();
            if (education.trim().isEmpty()) {
                System.out.println("Error: Education cannot be empty.");
                return;
            }
            instructor.setEducation(education);
            System.out.println("✓ Education updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error updating education: " + e.getMessage());
        }
    }

    // ===== MEMBER MENU =====
    private static void memberMenu() {
        boolean back = false;
        while (!back) {
            try {
                System.out.println("\n--- Member Menu ---");
                System.out.println("Current Member: " + member.getName());
                System.out.println("1. View Training for Day");
                System.out.println("2. View Progress History");
                System.out.println("3. Register Entry");
                System.out.println("4. Register Exit");
                System.out.println("5. Update Profile");
                System.out.println("6. View Attendance History");
                System.out.println("0. Back to Main Menu");
                System.out.print("Select option: ");

                int choice = getIntInput();

                switch (choice) {
                    case 1 -> viewTrainingForDay();
                    case 2 -> viewProgressHistory();
                    case 3 -> registerEntry();
                    case 4 -> registerExit();
                    case 5 -> updateMemberProfile();
                    case 6 -> viewAttendanceHistory();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error in member menu: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void viewTrainingForDay() {
        try {
            System.out.println("\n--- View Training ---");
            System.out.println("Select Day:");
            for (int i = 0; i < Weekday.values().length; i++) {
                System.out.println((i + 1) + ". " + Weekday.values()[i]);
            }
            System.out.print("Choice: ");
            int dayChoice = getIntInput() - 1;

            if (dayChoice >= 0 && dayChoice < Weekday.values().length) {
                Training training = member.viewTraining(Weekday.values()[dayChoice]);
                if (training != null) {
                    training.generateReport();
                } else {
                    System.out.println("No training scheduled for this day.");
                }
            } else {
                System.out.println("Invalid day selection.");
            }
        } catch (Exception e) {
            System.out.println("Error viewing training: " + e.getMessage());
        }
    }

    private static void viewAttendanceHistory() {
        try {
            System.out.println("\n--- Attendance History ---");
            List<Attendance> attendances = member.viewAttendances();

            if (attendances.isEmpty()) {
                System.out.println("No attendance records yet.");
            } else {
                System.out.println("Total attendances: " + member.getTotalAttendances());
                System.out.println("Total hours this month: " + String.format("%.2f", member.getTotalHoursThisMonth()) + " hours");
                System.out.println("\nRecent attendances:");

                for (int i = attendances.size() - 1; i >= Math.max(0, attendances.size() - 10); i--) {
                    Attendance a = attendances.get(i);
                    try {
                        System.out.println("- Date: " + a.getDate() + " | Hours: " + String.format("%.2f", a.calculateHours()));
                    } catch (Exception e) {
                        System.out.println("- Date: " + a.getDate() + " | Incomplete record");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error viewing attendance history: " + e.getMessage());
        }
    }

    private static void viewProgressHistory() {
        try {
            System.out.println("\n--- Progress History ---");
            if (member.viewProgress().isEmpty()) {
                System.out.println("No progress records yet.");
            } else {
                for (Progress p : member.viewProgress()) {
                    System.out.println("Date: " + p.getDate() +
                            " | Weight: " + p.getWeight() + "kg" +
                            " | Muscle Mass: " + p.getMuscleMassPercent() + "%");
                }
            }
        } catch (Exception e) {
            System.out.println("Error viewing progress: " + e.getMessage());
        }
    }

    // CORRIGIDO: Gerenciar attendance ativo
    private static void registerEntry() {
        try {
            System.out.println("\n--- Register Entry ---");
            currentAttendance = new Attendance();
            member.registerEntry(currentAttendance);
            System.out.println("✓ Entry registered at " + LocalTime.now());
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error registering entry: " + e.getMessage());
        }
    }

    // CORRIGIDO: Usar attendance ativo
    private static void registerExit() {
        try {
            System.out.println("\n--- Register Exit ---");
            if (currentAttendance == null) {
                System.out.println("Error: No active entry found. Please register entry first.");
                return;
            }
            member.registerExit(currentAttendance);
            System.out.println("✓ Exit registered at " + LocalTime.now());
            System.out.println("✓ Total time: " + String.format("%.2f", currentAttendance.calculateHours()) + " hours");
            currentAttendance = null; // Reset after exit
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error registering exit: " + e.getMessage());
        }
    }

    private static void updateMemberProfile() {
        try {
            System.out.println("\n--- Update Profile ---");
            System.out.print("New Email (or press Enter to skip): ");
            String email = scanner.nextLine();
            System.out.print("New Phone (or press Enter to skip): ");
            String phone = scanner.nextLine();

            member.updateProfile(email.isEmpty() ? null : email,
                    phone.isEmpty() ? null : phone);
            System.out.println("✓ Profile updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
    }

    // ===== GYM MENU =====
    private static void gymMenu() {
        boolean back = false;
        while (!back) {
            try {
                System.out.println("\n--- Gym Operations ---");
                System.out.println("1. View Gym Info");
                System.out.println("2. List All Instructors");
                System.out.println("3. List All Members");
                System.out.println("4. List All Equipment");
                System.out.println("5. Find Member by Name");
                System.out.println("6. Find Instructor by Name");
                System.out.println("0. Back to Main Menu");
                System.out.print("Select option: ");

                int choice = getIntInput();

                switch (choice) {
                    case 1 -> gym.viewGymInfo();
                    case 2 -> listAllInstructors();
                    case 3 -> listAllMembers();
                    case 4 -> listAllEquipment();
                    case 5 -> findMemberByName();
                    case 6 -> findInstructorByName();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error in gym menu: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void listAllInstructors() {
        try {
            System.out.println("\n--- All Instructors ---");
            if (gym.getInstructors().isEmpty()) {
                System.out.println("No instructors registered.");
            } else {
                for (Instructor i : gym.getInstructors()) {
                    System.out.println("- ID: " + i.getId() +
                            " | Name: " + i.getName() +
                            " | Email: " + i.getEmail() +
                            " | Education: " + i.getEducation());
                }
            }
        } catch (Exception e) {
            System.out.println("Error listing instructors: " + e.getMessage());
        }
    }

    private static void listAllMembers() {
        try {
            System.out.println("\n--- All Members ---");
            if (gym.getMembers().isEmpty()) {
                System.out.println("No members registered.");
            } else {
                for (Member m : gym.getMembers()) {
                    System.out.println("- ID: " + m.getId() +
                            " | Name: " + m.getName() +
                            " | Email: " + m.getEmail());
                }
            }
        } catch (Exception e) {
            System.out.println("Error listing members: " + e.getMessage());
        }
    }

    private static void listAllEquipment() {
        try {
            System.out.println("\n--- All Equipment ---");
            if (gym.getEquipments().isEmpty()) {
                System.out.println("No equipment registered.");
            } else {
                for (Equipment e : gym.getEquipments()) {
                    System.out.println("- Name: " + e.getName() +
                            " | Description: " + e.getDescription());
                }
            }
        } catch (Exception e) {
            System.out.println("Error listing equipment: " + e.getMessage());
        }
    }

    private static void findMemberByName() {
        try {
            System.out.print("Enter member name: ");
            String name = scanner.nextLine();
            Member found = gym.findMemberByName(name);
            if (found != null) {
                System.out.println("✓ Found: " + found.getName() + " | " + found.getEmail());
            } else {
                System.out.println("Member not found.");
            }
        } catch (Exception e) {
            System.out.println("Error finding member: " + e.getMessage());
        }
    }

    private static void findInstructorByName() {
        try {
            System.out.print("Enter instructor name: ");
            String name = scanner.nextLine();
            Instructor found = gym.findInstructorByName(name);
            if (found != null) {
                System.out.println("✓ Found: " + found.getName() + " | " + found.getEmail());
            } else {
                System.out.println("Instructor not found.");
            }
        } catch (Exception e) {
            System.out.println("Error finding instructor: " + e.getMessage());
        }
    }

    // ===== EQUIPMENT MENU =====
    private static void equipmentMenu() {
        try {
            System.out.println("\n--- Equipment Operations ---");
            System.out.println("1. Add New Equipment");
            System.out.println("2. Update Equipment Data");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> registerEquipment();
                case 2 -> updateEquipmentData();
            }
        } catch (Exception e) {
            System.out.println("Error in equipment menu: " + e.getMessage());
        }
    }

    private static void updateEquipmentData() {
        try {
            System.out.println("\n--- Update Equipment ---");
            listAllEquipment();
            System.out.print("\nEquipment Name: ");
            String name = scanner.nextLine();

            for (Equipment e : gym.getEquipments()) {
                if (e.getName().equalsIgnoreCase(name)) {
                    System.out.print("New Description: ");
                    String desc = scanner.nextLine();
                    System.out.print("New Function: ");
                    String func = scanner.nextLine();

                    e.updateData(desc, func);
                    System.out.println("✓ Equipment updated successfully!");
                    return;
                }
            }
            System.out.println("Equipment not found.");
        } catch (Exception e) {
            System.out.println("Error updating equipment: " + e.getMessage());
        }
    }

    // ===== ATTENDANCE MENU =====
    private static void attendanceMenu() {
        try {
            System.out.println("\n--- Attendance Test ---");
            System.out.println("1. Test Full Attendance Flow");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select option: ");

            int choice = getIntInput();

            if (choice == 1) {
                testAttendanceFlow();
            }
        } catch (Exception e) {
            System.out.println("Error in attendance menu: " + e.getMessage());
        }
    }

    private static void testAttendanceFlow() {
        try {
            System.out.println("\n--- Testing Attendance Flow ---");
            Attendance att = new Attendance();

            System.out.println("Registering entry...");
            att.registerEntry();

            System.out.println("Waiting 2 seconds...");
            Thread.sleep(2000);

            System.out.println("Registering exit...");
            att.registerExit();

            System.out.println("✓ Calculating hours: " + String.format("%.2f", att.calculateHours()) + " hours");
        } catch (InterruptedException e) {
            System.out.println("Error: Test interrupted.");
            Thread.currentThread().interrupt();
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error in attendance flow: " + e.getMessage());
        }
    }

    // ===== UTILITY METHODS =====
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Enter a number: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    // CORRIGIDO: Aceitar vírgula ou ponto
    private static double getDoubleInput() {
        while (true) {
            try {
                String input = scanner.nextLine().replace(',', '.');
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }
}
