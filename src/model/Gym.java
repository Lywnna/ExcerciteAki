package model;

import java.util.*;

public class Gym {
    private String name;
    private Address address;
    private String phone;
    private String website;
    private Map<Weekday, Schedule> operatingHours = new HashMap<>();

    private List<Instructor> instructors = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<Equipment> equipments = new ArrayList<>();

    // Métodos para cadastrar
    public boolean registerInstructor(Instructor instructor) {
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor cannot be null");
        }
        if (instructors.contains(instructor)) {
            throw new IllegalStateException("Instructor is already registered in this gym");
        }
        return instructors.add(instructor);
    }

    public boolean registerMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        if (members.contains(member)) {
            throw new IllegalStateException("Member is already registered in this gym");
        }
        return members.add(member);
    }

    public boolean registerEquipment(Equipment equipment) {
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment cannot be null");
        }
        if (equipments.contains(equipment)) {
            throw new IllegalStateException("Equipment is already registered in this gym");
        }
        return equipments.add(equipment);
    }

    // Métodos para remover
    public boolean removeInstructor(Instructor instructor) {
        if (instructor == null) return false;
        return instructors.remove(instructor);
    }

    public boolean removeMember(Member member) {
        if (member == null) return false;
        return members.remove(member);
    }

    public boolean removeEquipment(Equipment equipment) {
        if (equipment == null) return false;
        return equipments.remove(equipment);
    }
    
    // Métodos para atualizar
    public void updateGymInfo(String name, String phone, String website) {
        if (name != null && !name.trim().isEmpty()) this.name = name;
        if (phone != null && !phone.trim().isEmpty()) this.phone = phone;
        if (website != null && !website.trim().isEmpty()) this.website = website;
    }

    public void updateAddress(Address address) {
        if (address != null) this.address = address;
    }

    // Métodos de pesquisa
    public Member findMemberByName(String name) {
        for (Member m : members) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    public Instructor findInstructorByName(String name) {
        for (Instructor i : instructors) {
            if (i.getName().equalsIgnoreCase(name)) return i;
        }
        return null;
    }

    // Define horário
    public void setSchedule(Weekday day, Schedule s) { operatingHours.put(day, s); }
    
    // Visualizar infos
    public void viewGymInfo() {
        System.out.println("Gym: " + name + " | Phone: " + phone + " | Website: " + website + " | Opening hours: " + operatingHours);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public Map<Weekday, Schedule> getOperatingHours() { return operatingHours; }
    public List<Instructor> getInstructors() { return instructors; }
    public List<Member> getMembers() { return members; }
    public List<Equipment> getEquipments() { return equipments; }
}

