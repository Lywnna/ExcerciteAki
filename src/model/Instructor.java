package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Instructor extends User {

	private String education;
    private final List<Training> definedTrainings = new ArrayList<>();
    
    public Instructor(String name, String email, String phone, String password, String education) {
		super(name, email, phone, password);
		this.education = education;
	}

    public void defineTraining(Member member, Training training) {
        if (member == null || training == null) {
            throw new IllegalArgumentException("Member and training cannot be null");
        }
        member.addTraining(training);
        definedTrainings.add(training);
    }

    public void registerProgress(Member member, Progress progress) {
        if (member == null || progress == null) {
            throw new IllegalArgumentException("Member and progress cannot be null");
        }
        member.addProgress(progress);
    }

    public String getEducation() { return education; }
    public void setEducation(String education) {
        if (education == null || education.trim().isEmpty()) {
            throw new IllegalArgumentException("Education cannot be null or empty");
        }
        this.education = education;
    }

    public List<Training> getDefinedTrainings() {
        return Collections.unmodifiableList(definedTrainings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instructor)) return false;
        Instructor that = (Instructor) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
