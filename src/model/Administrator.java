package model;

public class Administrator extends User {

    public Administrator(String name, String email, String phone, String password) {
        super(name, email, phone, password);
    }

    public Administrator(long id, String name, String email, String phone, String password) {
        super(id, name, email, phone, password);
    }

    public void registerGym(Gym gym, Address address) {
        if (gym == null || address == null) {
            throw new IllegalArgumentException("Gym or Address cannot be null");
        }
        gym.setAddress(address);
    }

    public void registerInstructor(Gym gym, Instructor instructor) {
        if (gym == null || instructor == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (gym.getInstructors().contains(instructor)) {
            throw new IllegalStateException("Instructor is already registered in this gym");
        }
        gym.getInstructors().add(instructor);
    }

    public void registerMember(Gym gym, Member member) {
        if (gym == null || member == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (gym.getMembers().contains(member)) {
            throw new IllegalStateException("Member is already registered in this gym");
        }
        gym.getMembers().add(member);
    }

    public void registerEquipment(Gym gym, Equipment equipment) {
        if (gym == null || equipment == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (gym.getEquipments().contains(equipment)) {
            throw new IllegalStateException("Equipment is already registered in this gym");
        }
        gym.getEquipments().add(equipment);
    }

    public void removeInstructor(Gym gym, Instructor instructor) {
        if (gym == null || instructor == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (!gym.getInstructors().contains(instructor)) {
            throw new IllegalStateException("Instructor is not registered in this gym");
        }
        gym.getInstructors().remove(instructor);
    }

    public void removeMember(Gym gym, Member member) {
        if (gym == null || member == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (!gym.getMembers().contains(member)) {
            throw new IllegalStateException("Member is not registered in this gym");
        }
        gym.getMembers().remove(member);
    }

    public void removeEquipment(Gym gym, Equipment equipment) {
        if (gym == null || equipment == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (!gym.getEquipments().contains(equipment)) {
            throw new IllegalStateException("Equipment is not registered in this gym");
        }
        gym.getEquipments().remove(equipment);
    }

    public void updateGymInfo(Gym gym, String name, String phone, String website) {
        if (gym == null) {
            throw new IllegalArgumentException("Gym cannot be null");
        }
        if (name != null && !name.trim().isEmpty()) {
            gym.setName(name);
        }
        if (phone != null && !phone.trim().isEmpty()) {
            gym.setPhone(phone);
        }
        if (website != null && !website.trim().isEmpty()) {
            gym.setWebsite(website);
        }
    }

    public void updateGymAddress(Gym gym, Address newAddress) {
        if (gym == null || newAddress == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        gym.setAddress(newAddress);
    }

    public void updateGymSchedule(Gym gym, Weekday day, Schedule schedule) {
        if (gym == null || day == null || schedule == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        gym.setSchedule(day, schedule);
    }

    public void updateEquipment(Equipment equipment, String name, String description, String function) {
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment cannot be null");
        }
        if (name != null && !name.trim().isEmpty()) {
            equipment.setName(name);
        }
        if (description != null && !description.trim().isEmpty()) {
            equipment.setDescription(description);
        }
        if (function != null && !function.trim().isEmpty()) {
            equipment.setFunction(function);
        }
    }
}
