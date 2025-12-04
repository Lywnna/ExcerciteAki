package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Member extends User {
    private LocalDate birthDate;
    private double height;
    private List<Progress> progressList;
    private List<Attendance> attendances;
    private Map<Weekday, Training> trainings;

    public Member(String name, String email, String phone, String password) {
        super(name, email, phone, password);
        this.progressList = new ArrayList<>();
        this.attendances = new ArrayList<>();
        this.trainings = new HashMap<>();
    }

    public Member(long id, String name, String email, String phone, String password) {
        super(id, name, email, phone, password);
        this.progressList = new ArrayList<>();
        this.attendances = new ArrayList<>();
        this.trainings = new HashMap<>();
    }

    public void addTraining(Weekday day, Training training) {
        if (day == null || training == null) {
            throw new IllegalArgumentException("Day and training cannot be null");
        }
        getTrainings().put(day, training);
    }

    public Training viewTraining(Weekday day) {
        return getTrainings().get(day);
    }

    public void addProgress(Progress progress) {
        if (progress == null) {
            throw new IllegalArgumentException("Progress cannot be null");
        }
        progressList.add(progress);
    }

    public List<Progress> viewProgress() {
        return progressList;
    }

    public void addAttendance(Attendance attendance) {
        if (attendance == null) {
            throw new IllegalArgumentException("Attendance cannot be null");
        }
        attendances.add(attendance);
    }

    public void registerEntry(Attendance attendance) {
        if (attendance == null) {
            throw new IllegalArgumentException("Attendance cannot be null");
        }
        attendance.recordEntry();
        attendances.add(attendance);
    }

    public void registerExit(Attendance attendance) {
        if (attendance == null) {
            throw new IllegalArgumentException("Attendance cannot be null");
        }
        if (!attendances.contains(attendance)) {
            throw new IllegalStateException("Attendance record not found for this member");
        }
        attendance.recordExit();
    }

    public List<Attendance> viewAttendances() {
        return attendances;
    }

    public int getTotalAttendances() {
        return attendances.size();
    }

    public double getTotalHoursThisMonth() {
        LocalDate now = LocalDate.now();
        double totalHours = 0;

        for (Attendance a : attendances) {
            if (a.getDate() != null &&
                    a.getDate().getMonth() == now.getMonth() &&
                    a.getDate().getYear() == now.getYear()) {
                try {
                    totalHours += a.calculateHours();
                } catch (Exception e) {
                    // ignora
                }
            }
        }

        return totalHours;
    }

    public long getAge() {
        if (birthDate == null) {
            return 0;
        }
        return ChronoUnit.YEARS.between(birthDate, LocalDate.now());
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
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
        this.height = height;
    }

    public List<Progress> getProgressList() {
        return progressList;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public Map<Weekday, Training> getTrainings() {
        if (trainings == null) {
            trainings = new HashMap<>();
        }
        return trainings;
    }

    public void setTrainings(Map<Weekday, Training> trainings) {
        this.trainings = (trainings != null) ? trainings : new HashMap<>();
    }
}