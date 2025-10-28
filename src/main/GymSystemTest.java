package main;

import model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class GymSystemTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     GYM MANAGEMENT SYSTEM - AUTOMATED TESTING          ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        // Initialize test data
        Gym gym = new Gym();
        Administrator admin = new Administrator("Admin Test", "admin@test.com", "123", "pass123");

        // Run all tests
        testAddressClass();
        testUserClass();
        testAdministratorClass(gym, admin);
        testInstructorClass(gym);
        testMemberClass(gym);
        testEquipmentClass(gym, admin);
        testAttendanceClass();
        testTrainingClass(gym);
        testProgressClass();
        testScheduleClass();
        testGymOperations(gym, admin);

        // Print summary
        printTestSummary();
    }

    // ===== TEST: Address Class =====
    private static void testAddressClass() {
        System.out.println("\n[TEST 1] Testing Address Class");
        System.out.println("─────────────────────────────────────────");

        try {
            Address address = new Address();
            address.setStreet("Main Street");
            address.setNumber("100");
            address.setCity("New York");
            address.setState("NY");
            address.setZipCode("10001");
            address.setComplement("Apt 5B");
            address.setNeighborhood("Downtown");

            assert address.getStreet().equals("Main Street") : "Street mismatch";
            assert address.getNumber().equals("100") : "Number mismatch";
            assert address.getCity().equals("New York") : "City mismatch";

            pass("Address creation and getters/setters");
        } catch (Exception e) {
            fail("Address class", e);
        }
    }

    // ===== TEST: User Class =====
    private static void testUserClass() {
        System.out.println("\n[TEST 2] Testing User Class");
        System.out.println("─────────────────────────────────────────");

        try {
            User user = new User("Test User", "test@email.com", "555-1234", "password123");

            assert user.getName().equals("Test User") : "Name mismatch";
            assert user.getEmail().equals("test@email.com") : "Email mismatch";
            assert user.getId() > 0 : "ID not generated";

            pass("User creation");

            // Test login
            user.login("password123");
            assert user.isLoggedIn() : "Login failed";
            pass("Login with correct password");

            user.logout();
            assert !user.isLoggedIn() : "Logout failed";
            pass("Logout");

            // Test wrong password
            user.login("wrongpassword");
            assert !user.isLoggedIn() : "Login succeeded with wrong password";
            pass("Login rejection with wrong password");

            // Test profile update
            user.updateProfile("newemail@test.com", "999-8888");
            assert user.getEmail().equals("newemail@test.com") : "Email update failed";
            assert user.getPhone().equals("999-8888") : "Phone update failed";
            pass("Profile update");

        } catch (Exception e) {
            fail("User class", e);
        }
    }

    // ===== TEST: Administrator Class =====
    private static void testAdministratorClass(Gym gym, Administrator admin) {
        System.out.println("\n[TEST 3] Testing Administrator Class");
        System.out.println("─────────────────────────────────────────");

        try {
            // Test admin creation
            assert admin.getName().equals("Admin Test") : "Admin name mismatch";
            pass("Administrator creation");

            // Test instructor registration
            Instructor instructor = new Instructor("Instructor Test", "inst@test.com", "111", "pass", "Certified");
            admin.registerInstructor(gym, instructor);
            assert gym.getInstructors().size() == 1 : "Instructor not registered";
            pass("Register instructor via admin");

            // Test member registration
            Member member = new Member("Member Test", "member@test.com", "222", "pass");
            admin.registerMember(gym, member);
            assert gym.getMembers().size() == 1 : "Member not registered";
            pass("Register member via admin");

            // Test equipment registration
            Equipment equipment = new Equipment();
            equipment.setName("Treadmill");
            equipment.setDescription("Cardio");
            equipment.setFunction("Running");
            admin.registerEquipment(gym, equipment);
            assert gym.getEquipments().size() == 1 : "Equipment not registered";
            pass("Register equipment via admin");

            // Test gym info update
            admin.updateGymInfo(gym, "Updated Gym", "999-999", "www.updated.com");
            assert gym.getName().equals("Updated Gym") : "Gym name not updated";
            pass("Update gym info");

            // Test schedule update
            Schedule schedule = new Schedule();
            schedule.setOpeningTime(LocalTime.of(6, 0));
            schedule.setClosingTime(LocalTime.of(22, 0));
            admin.updateGymSchedule(gym, Weekday.MONDAY, schedule);
            assert gym.getOperatingHours().containsKey(Weekday.MONDAY) : "Schedule not updated";
            pass("Update gym schedule");

        } catch (Exception e) {
            fail("Administrator class", e);
        }
    }

    // ===== TEST: Instructor Class =====
    private static void testInstructorClass(Gym gym) {
        System.out.println("\n[TEST 4] Testing Instructor Class");
        System.out.println("─────────────────────────────────────────");

        try {
            Instructor instructor = new Instructor("Instructor 2", "inst2@test.com", "333", "pass", "Degree");
            Member member = new Member("Member 2", "mem2@test.com", "444", "pass");

            // Test training definition
            Training training = new Training();
            training.setWeekDay(Weekday.TUESDAY);
            instructor.defineTraining(member, training);

            assert instructor.getDefinedTrainings().size() == 1 : "Training not defined";
            pass("Define training for member");

            // Test progress registration
            Progress progress = new Progress();
            progress.setDate(LocalDate.now());
            progress.setWeight(70.5);
            progress.setMuscleMassPercent(25.0);
            instructor.registerProgress(member, progress);

            assert member.viewProgress().size() == 1 : "Progress not registered";
            pass("Register member progress");

            // Test education update
            instructor.setEducation("Master's Degree");
            assert instructor.getEducation().equals("Master's Degree") : "Education not updated";
            pass("Update education");

        } catch (Exception e) {
            fail("Instructor class", e);
        }
    }

    // ===== TEST: Member Class =====
    private static void testMemberClass(Gym gym) {
        System.out.println("\n[TEST 5] Testing Member Class");
        System.out.println("─────────────────────────────────────────");

        try {
            Member member = new Member("Member 3", "mem3@test.com", "555", "pass");

            // Test training assignment
            Training training = new Training();
            training.setWeekDay(Weekday.WEDNESDAY);
            member.addTraining(training);

            Training retrieved = member.viewTraining(Weekday.WEDNESDAY);
            assert retrieved != null : "Training not found";
            pass("View training for specific day");

            // Test progress tracking
            Progress progress = new Progress();
            progress.setDate(LocalDate.now());
            progress.setWeight(65.0);
            progress.setMuscleMassPercent(28.0);
            member.addProgress(progress);

            assert member.viewProgress().size() == 1 : "Progress not tracked";
            pass("Track member progress");

            // Test attendance
            Attendance attendance = new Attendance();
            member.registerEntry(attendance);

            assert member.viewAttendances().size() == 1 : "Attendance not recorded";
            pass("Register attendance entry");

            // Test total attendances
            assert member.getTotalAttendances() == 1 : "Total attendances incorrect";
            pass("Get total attendances count");

        } catch (Exception e) {
            fail("Member class", e);
        }
    }

    // ===== TEST: Equipment Class =====
    private static void testEquipmentClass(Gym gym, Administrator admin) {
        System.out.println("\n[TEST 6] Testing Equipment Class");
        System.out.println("─────────────────────────────────────────");

        try {
            Equipment equipment = new Equipment();
            equipment.setName("Bench Press");
            equipment.setDescription("Upper body");
            equipment.setFunction("Chest strength");

            assert equipment.getName().equals("Bench Press") : "Name not set";
            pass("Equipment creation and setters");

            // Test update
            equipment.updateData("Updated description", "Updated function");
            assert equipment.getDescription().equals("Updated description") : "Description not updated";
            assert equipment.getFunction().equals("Updated function") : "Function not updated";
            pass("Update equipment data");

        } catch (Exception e) {
            fail("Equipment class", e);
        }
    }

    // ===== TEST: Attendance Class =====
    private static void testAttendanceClass() {
        System.out.println("\n[TEST 7] Testing Attendance Class");
        System.out.println("─────────────────────────────────────────");

        try {
            Attendance attendance = new Attendance();

            // Test entry registration
            attendance.registerEntry();
            pass("Register entry time");

            // Wait 1 second
            Thread.sleep(1000);

            // Test exit registration
            attendance.registerExit();
            pass("Register exit time");

            // Test hours calculation
            double hours = attendance.calculateHours();
            assert hours > 0 : "Hours not calculated correctly";
            assert hours < 1 : "Hours calculation seems wrong";
            pass("Calculate attendance hours");

            // Test date tracking
            assert attendance.getDate() != null : "Date not recorded";
            pass("Track attendance date");

        } catch (Exception e) {
            fail("Attendance class", e);
        }
    }

    // ===== TEST: Training Class =====
    private static void testTrainingClass(Gym gym) {
        System.out.println("\n[TEST 8] Testing Training Class");
        System.out.println("─────────────────────────────────────────");

        try {
            Training training = new Training();
            training.setWeekDay(Weekday.THURSDAY);

            assert training.getWeekDay() == Weekday.THURSDAY : "Weekday not set";
            pass("Set training weekday");

            // Add exercises
            Equipment equipment = new Equipment();
            equipment.setName("Squat Rack");

            Exercise exercise1 = new Exercise();
            exercise1.setEquipment(equipment);
            exercise1.setWeight(100.0);
            exercise1.setRepetitions(10);
            exercise1.setOrder(1);

            training.addExercise(exercise1);
            assert training.getExercises().size() == 1 : "Exercise not added";
            pass("Add exercise to training");

            Exercise exercise2 = new Exercise();
            exercise2.setEquipment(equipment);
            exercise2.setWeight(80.0);
            exercise2.setRepetitions(12);
            exercise2.setOrder(2);

            training.addExercise(exercise2);
            assert training.getExercises().size() == 2 : "Second exercise not added";
            pass("Add multiple exercises");

            // Test report generation (just verify it doesn't crash)
            training.generateReport();
            pass("Generate training report");

        } catch (Exception e) {
            fail("Training class", e);
        }
    }

    // ===== TEST: Progress Class =====
    private static void testProgressClass() {
        System.out.println("\n[TEST 9] Testing Progress Class");
        System.out.println("─────────────────────────────────────────");

        try {
            Progress progress = new Progress();
            progress.setDate(LocalDate.now());
            progress.setWeight(75.5);
            progress.setMuscleMassPercent(30.0);

            assert progress.getDate() != null : "Date not set";
            assert progress.getWeight() == 75.5 : "Weight not set";
            assert progress.getMuscleMassPercent() == 30.0 : "Muscle mass not set";
            pass("Progress tracking all fields");

        } catch (Exception e) {
            fail("Progress class", e);
        }
    }

    // ===== TEST: Schedule Class =====
    private static void testScheduleClass() {
        System.out.println("\n[TEST 10] Testing Schedule Class");
        System.out.println("─────────────────────────────────────────");

        try {
            Schedule schedule = new Schedule();
            schedule.setOpeningTime(LocalTime.of(8, 0));
            schedule.setClosingTime(LocalTime.of(20, 0));

            assert schedule.getOpeningTime().equals(LocalTime.of(8, 0)) : "Opening time not set";
            assert schedule.getClosingTime().equals(LocalTime.of(20, 0)) : "Closing time not set";
            pass("Schedule time setting");

            // Test isOpen
            assert schedule.isOpen(LocalTime.of(12, 0)) : "Should be open at noon";
            assert !schedule.isOpen(LocalTime.of(6, 0)) : "Should be closed at 6 AM";
            assert !schedule.isOpen(LocalTime.of(22, 0)) : "Should be closed at 10 PM";
            pass("Schedule open/closed validation");

        } catch (Exception e) {
            fail("Schedule class", e);
        }
    }

    // ===== TEST: Gym Operations =====
    private static void testGymOperations(Gym gym, Administrator admin) {
        System.out.println("\n[TEST 11] Testing Gym Operations");
        System.out.println("─────────────────────────────────────────");

        try {
            // Test find member by name
            Member testMember = new Member("John Doe", "john@test.com", "777", "pass");
            admin.registerMember(gym, testMember);

            Member found = gym.findMemberByName("John Doe");
            assert found != null : "Member not found by name";
            assert found.getName().equals("John Doe") : "Wrong member found";
            pass("Find member by name");

            // Test find instructor by name
            Instructor testInstructor = new Instructor("Jane Trainer", "jane@test.com", "888", "pass", "Cert");
            admin.registerInstructor(gym, testInstructor);

            Instructor foundInst = gym.findInstructorByName("Jane Trainer");
            assert foundInst != null : "Instructor not found by name";
            assert foundInst.getName().equals("Jane Trainer") : "Wrong instructor found";
            pass("Find instructor by name");

            // Test removal
            admin.removeMember(gym, testMember);
            assert gym.findMemberByName("John Doe") == null : "Member not removed";
            pass("Remove member");

            admin.removeInstructor(gym, testInstructor);
            assert gym.findInstructorByName("Jane Trainer") == null : "Instructor not removed";
            pass("Remove instructor");

            // Test gym info view (just verify no crash)
            gym.viewGymInfo();
            pass("View gym information");

        } catch (Exception e) {
            fail("Gym operations", e);
        }
    }

    // ===== HELPER METHODS =====
    private static void pass(String testName) {
        testsPassed++;
        System.out.println("✓ PASS: " + testName);
    }

    private static void fail(String testName, Exception e) {
        testsFailed++;
        System.out.println("✗ FAIL: " + testName);
        System.out.println("  Error: " + e.getMessage());
    }

    private static void printTestSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                    TEST SUMMARY                        ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("║  Total Tests Passed: " + String.format("%-33d", testsPassed) + "║");
        System.out.println("║  Total Tests Failed: " + String.format("%-33d", testsFailed) + "║");
        System.out.println("╠════════════════════════════════════════════════════════╣");

        if (testsFailed == 0) {
            System.out.println("║  Result: ALL TESTS PASSED ✓                           ║");
        } else {
            System.out.println("║  Result: SOME TESTS FAILED ✗                          ║");
        }

        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}
