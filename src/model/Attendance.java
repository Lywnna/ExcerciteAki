package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Attendance {
    private LocalDate date;
    private LocalTime entryTime;
    private LocalTime exitTime;

    public void registerEntry() {
        if (entryTime != null) {
            throw new IllegalStateException("Entry already registered for this attendance");
        }
        entryTime = LocalTime.now();
        date = LocalDate.now();
    }

    public void registerExit() {
        if (entryTime == null) {
            throw new IllegalStateException("Cannot register exit before entry");
        }
        if (exitTime != null) {
            throw new IllegalStateException("Exit already registered for this attendance");
        }
        exitTime = LocalTime.now();
    }

    public double calculateHours() {
        if (entryTime == null || exitTime == null) {
            throw new IllegalStateException("Both entry and exit times must be registered");
        }
        return ChronoUnit.MINUTES.between(entryTime, exitTime) / 60.0;
    }
}

