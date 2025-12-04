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

    public void updateGymInfo(String name, String phone, String website) {
        if (name != null && !name.trim().isEmpty()) this.name = name;
        if (phone != null && !phone.trim().isEmpty()) this.phone = phone;
        if (website != null && !website.trim().isEmpty()) this.website = website;
    }

    public void updateAddress(Address address) {
        if (address != null) this.address = address;
    }

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

    public void setSchedule(Weekday day, Schedule s) { operatingHours.put(day, s); }

    public void viewGymInfo() {
        System.out.println("--- INFORMAÇÕES DA ACADEMIA ---");
        System.out.println("Nome: " + name);
        System.out.println("Telefone: " + phone);
        System.out.println("Website: " + website);

        if (address != null) {
            System.out.println("\n--- ENDEREÇO ---");
            System.out.println("Rua: " + address.getStreet() + ", " + address.getNumber());
            System.out.println("Bairro: " + address.getNeighborhood());
            System.out.println("Cidade: " + address.getCity() + " - " + address.getState());
            System.out.println("CEP: " + address.getZipCode());
        }

        System.out.println("\n--- HORÁRIOS DE FUNCIONAMENTO ---");
        if (operatingHours.isEmpty()) {
            System.out.println("Nenhum horário cadastrado.");
            System.out.println("Use a opção 2 do menu para cadastrar!");
        } else {
            for (Weekday day : Weekday.values()) {
                Schedule schedule = operatingHours.get(day);
                if (schedule != null) {
                    System.out.printf("%s: %s - %s%n",
                            day,
                            schedule.getOpeningTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                            schedule.getClosingTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
                    );
                }
            }
        }

        System.out.println("\n--- ESTATÍSTICAS ---");
        System.out.println("Total de Instrutores: " + instructors.size());
        System.out.println("Total de Alunos: " + members.size());
        System.out.println("Total de Aparelhos: " + equipments.size());
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
    public void setOperatingHours(Map<Weekday, Schedule> operatingHours) {this.operatingHours = operatingHours;}
    public List<Instructor> getInstructors() { return instructors; }
    public List<Member> getMembers() { return members; }
    public List<Equipment> getEquipments() { return equipments; }
}

