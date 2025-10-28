package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member extends User {

    private LocalDate birthDate;
    private double height;
    private List<Training> trainings = new ArrayList<>();
    private List<Progress> progresses = new ArrayList<>();
    private List<Attendance> attendances = new ArrayList<>();

    public Member(String name, String email, String phone, String password) {
        super(name, email, phone, password);
    }

    public Training viewTraining(Weekday day) {
        for (Training t : trainings) {
            if (t.getWeekDay() == day) return t;
        }
        return null;
    }

    public List<Progress> viewProgress() {
        return progresses;
    }

    // NOVOS MÉTODOS PARA USAR A LISTA DE ATTENDANCES
    public List<Attendance> viewAttendances() {
        return attendances;
    }

    public Attendance getLastAttendance() {
        if (attendances.isEmpty()) {
            return null;
        }
        return attendances.get(attendances.size() - 1);
    }

    public int getTotalAttendances() {
        return attendances.size();
    }

    public double getTotalHoursThisMonth() {
        double total = 0;
        LocalDate now = LocalDate.now();
        for (Attendance a : attendances) {
            if (a.getDate() != null &&
                    a.getDate().getMonth() == now.getMonth() &&
                    a.getDate().getYear() == now.getYear()) {
                try {
                    total += a.calculateHours();
                } catch (Exception e) {
                    // Skip invalid attendances
                }
            }
        }
        return total;
    }

    public void registerEntry(Attendance a) {
        attendances.add(a);
        a.registerEntry();
    }

    public void registerExit(Attendance a) {
        a.registerExit();
    }

    public void addTraining(Training training) {
        trainings.add(training);
    }

    public void addProgress(Progress progress) {
        progresses.add(progress);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
