package model;

import java.util.ArrayList;
import java.util.List;

public class Instructor extends User {
    private String education;
    private List<Training> definedTrainings;

    public Instructor(String name, String email, String phone, String password, String education) {
        super(name, email, phone, password);
        this.education = education;
        this.definedTrainings = new ArrayList<>();
    }

    public Instructor(long id, String name, String email, String phone, String password, String education) {
        super(id, name, email, phone, password);
        this.education = education;
        this.definedTrainings = new ArrayList<>();
    }

    public void defineTraining(Member member, Training training) {
        member.addTraining(training.getWeekDay(), training);
        definedTrainings.add(training);
    }

    public void registerProgress(Member member, Progress progress) {
        member.addProgress(progress);
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Training> getDefinedTrainings() {
        return definedTrainings;
    }
}
