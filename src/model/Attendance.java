package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class Attendance {
    private LocalDate date;
    private LocalTime entryTime;
    private LocalTime exitTime;

    public Attendance() {

    }

    public void recordEntry() {
        this.date = LocalDate.now();
        this.entryTime = LocalTime.now();
    }

    public void recordExit() {
        this.exitTime = LocalTime.now();
    }

    public double calculateHours() {
        if (entryTime == null || exitTime == null) {
            throw new IllegalStateException("Both entry and exit times must be recorded");
        }

        Duration duration = Duration.between(entryTime, exitTime);
        return duration.toMinutes() / 60.0;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalTime exitTime) {
        this.exitTime = exitTime;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "date=" + date +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                '}';
    }
}
