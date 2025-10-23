package model;

import java.time.LocalDate;

public class Progress {
    private LocalDate date;
    private double weight;
    private double muscleMassPercent;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public double getMuscleMassPercent() { return muscleMassPercent; }
    public void setMuscleMassPercent(double muscleMassPercent) {
        this.muscleMassPercent = muscleMassPercent;
    }
}

