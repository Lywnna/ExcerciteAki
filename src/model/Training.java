package model;

import java.util.ArrayList;
import java.util.List;

public class Training {
    private Weekday weekDay;
    private List<Exercise> exercises = new ArrayList<>();

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void generateReport() {
        System.out.println("Training for " + weekDay + ":");
        for (Exercise e : exercises) {
            System.out.println("- " + e.getEquipment().getName() + " | Weight: " + e.getWeight() + " | Repetitions: " + e.getRepetitions());
        }
    }

    public Weekday getWeekDay() { return weekDay; }
    public void setWeekDay(Weekday weekDay) { this.weekDay = weekDay; }
    public List<Exercise> getExercises() { return exercises; }
}

