package model;

import java.time.LocalTime;

public class Schedule {
    private LocalTime openingTime;
    private LocalTime closingTime;

    public LocalTime getOpeningTime() { return openingTime; }
    public void setOpeningTime(LocalTime openingTime) { this.openingTime = openingTime; }

    public LocalTime getClosingTime() { return closingTime; }
    public void setClosingTime(LocalTime closingTime) { this.closingTime = closingTime; }

    public boolean isOpen(LocalTime currentTime) {
        return currentTime.isAfter(openingTime) && currentTime.isBefore(closingTime);
    }
}

