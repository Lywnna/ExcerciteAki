package Database;

import model.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVPersistence {

    // ===== EQUIPAMENTOS =====
    public static void saveEquipments(List<Equipment> equipments) throws IOException {
        List<String> headers = List.of("id", "name", "description", "function");
        List<List<String>> rows = new ArrayList<>();

        int id = 1;
        for (Equipment e : equipments) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(id++));
            row.add(e.getName() != null ? e.getName() : "");
            row.add(e.getDescription() != null ? e.getDescription() : "");
            row.add(e.getFunction() != null ? e.getFunction() : "");
            rows.add(row);
        }

        Table table = new Table("equipments", headers, rows);
        CSV.WriteToTable(table);
    }

    public static List<Equipment> loadEquipments() throws IOException {
        List<Equipment> equipments = new ArrayList<>();

        try {
            Table table = Database.SelectAll("equipments");

            for (List<String> row : table.getRows()) {
                Equipment e = new Equipment();
                e.setName(row.get(1));
                e.setDescription(row.get(2));
                e.setFunction(row.get(3));
                equipments.add(e);
            }
        } catch (Exception e) {
            System.out.println("Nenhum equipamento encontrado no CSV. Iniciando vazio.");
        }

        return equipments;
    }

    // ===== GYM =====
    public static void saveGym(Gym gym) throws IOException {
        List<String> headers = List.of("id", "name", "phone", "website", "street", "number", "neighborhood", "city", "state", "zipcode");
        List<List<String>> rows = new ArrayList<>();

        Address addr = gym.getAddress();
        List<String> row = new ArrayList<>();
        row.add("1");
        row.add(gym.getName());
        row.add(gym.getPhone());
        row.add(gym.getWebsite());
        row.add(addr.getStreet());
        row.add(addr.getNumber());
        row.add(addr.getNeighborhood());
        row.add(addr.getCity());
        row.add(addr.getState());
        row.add(addr.getZipCode());
        rows.add(row);

        Table table = new Table("gym", headers, rows);
        CSV.WriteToTable(table);
    }

    public static Gym loadGym() throws IOException {
        try {
            Table table = Database.SelectAll("gym");

            if (table.CountRows() > 0) {
                List<String> row = table.getRows().get(0);
                Gym gym = new Gym();
                gym.setName(row.get(1));
                gym.setPhone(row.get(2));
                gym.setWebsite(row.get(3));

                Address address = new Address();
                address.setStreet(row.get(4));
                address.setNumber(row.get(5));
                address.setNeighborhood(row.get(6));
                address.setCity(row.get(7));
                address.setState(row.get(8));
                address.setZipCode(row.get(9));
                gym.setAddress(address);

                return gym;
            }
        } catch (Exception e) {
            System.out.println("Nenhum dado da academia encontrado.");
        }

        return null;
    }

    // ===== HORARIOS =====
    public static void saveSchedules(Map<Weekday, Schedule> schedules) throws IOException {
        List<String> headers = List.of("id", "gym_id", "weekday", "opening_time", "closing_time");
        List<List<String>> rows = new ArrayList<>();

        int id = 1;
        for (Map.Entry<Weekday, Schedule> entry : schedules.entrySet()) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(id++));
            row.add("1");
            row.add(entry.getKey().toString());
            row.add(entry.getValue().getOpeningTime().toString());
            row.add(entry.getValue().getClosingTime().toString());
            rows.add(row);
        }

        Table table = new Table("schedules", headers, rows);
        CSV.WriteToTable(table);
    }

    public static Map<Weekday, Schedule> loadSchedules() throws IOException {
        Map<Weekday, Schedule> schedules = new HashMap<>();

        try {
            Table table = Database.SelectAll("schedules");

            for (List<String> row : table.getRows()) {
                Weekday day = Weekday.valueOf(row.get(2));
                Schedule schedule = new Schedule();
                schedule.setOpeningTime(LocalTime.parse(row.get(3)));
                schedule.setClosingTime(LocalTime.parse(row.get(4)));
                schedules.put(day, schedule);
            }
        } catch (Exception e) {
            System.out.println("Nenhum horario encontrado. Iniciando vazio.");
        }

        return schedules;
    }

    // ===== PROGRESS =====
    public static void saveProgress(List<Member> members) throws IOException {
        List<String> headers = List.of("id", "member_id", "member_name", "date", "weight", "muscle_mass_percent");
        List<List<String>> rows = new ArrayList<>();

        int id = 1;
        for (Member m : members) {
            for (Progress p : m.viewProgress()) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(id++));
                row.add(String.valueOf(m.getId()));
                row.add(m.getName());
                row.add(p.getDate().toString());
                row.add(String.valueOf(p.getWeight()));
                row.add(String.valueOf(p.getMuscleMassPercent()));
                rows.add(row);
            }
        }

        Table table = new Table("progress", headers, rows);
        CSV.WriteToTable(table);
    }

    public static void loadProgress(List<Member> members) throws IOException {
        try {
            Table table = Database.SelectAll("progress");

            for (List<String> row : table.getRows()) {
                long memberId = Long.parseLong(row.get(1));

                for (Member m : members) {
                    if (m.getId() == memberId) {
                        Progress p = new Progress();
                        p.setDate(LocalDate.parse(row.get(3)));
                        p.setWeight(Double.parseDouble(row.get(4)));
                        p.setMuscleMassPercent(Double.parseDouble(row.get(5)));
                        m.addProgress(p);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Nenhum progresso encontrado.");
        }
    }

    // ===== ATTENDANCE =====
    public static void saveAttendance(List<Member> members) throws IOException {
        List<String> headers = List.of("id", "member_id", "member_name", "date", "entry_time", "exit_time");
        List<List<String>> rows = new ArrayList<>();

        int id = 1;
        for (Member m : members) {
            for (Attendance a : m.viewAttendances()) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(id++));
                row.add(String.valueOf(m.getId()));
                row.add(m.getName());
                row.add(a.getDate() != null ? a.getDate().toString() : "");
                row.add(a.getEntryTime() != null ? a.getEntryTime().toString() : "");
                row.add(a.getExitTime() != null ? a.getExitTime().toString() : "");
                rows.add(row);
            }
        }

        Table table = new Table("attendance", headers, rows);
        CSV.WriteToTable(table);
    }

    public static void loadAttendance(List<Member> members) throws IOException {
        try {
            Table table = Database.SelectAll("attendance");

            for (List<String> row : table.getRows()) {
                long memberId = Long.parseLong(row.get(1));

                for (Member m : members) {
                    if (m.getId() == memberId) {
                        Attendance a = new Attendance();
                        if (!row.get(3).isEmpty()) a.setDate(LocalDate.parse(row.get(3)));
                        if (!row.get(4).isEmpty()) a.setEntryTime(LocalTime.parse(row.get(4)));
                        if (!row.get(5).isEmpty()) a.setExitTime(LocalTime.parse(row.get(5)));
                        m.addAttendance(a);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Nenhuma frequencia encontrada.");
        }
    }

    public static void saveTrainings(List<Member> members, List<Equipment> equipments, List<Instructor> instructors) throws IOException {
        List<String> trainingHeaders = List.of("id", "member_id", "member_name", "weekday", "instructor_id", "instructor_name");
        List<List<String>> trainingRows = new ArrayList<>();

        List<String> exerciseHeaders = List.of("id", "training_id", "equipment_name", "weight", "repetitions", "order");
        List<List<String>> exerciseRows = new ArrayList<>();

        int trainingId = 1;
        int exerciseId = 1;

        for (Member m : members) {
            for (Weekday day : Weekday.values()) {
                Training t = m.viewTraining(day);
                if (t != null && !t.getExercises().isEmpty()) {
                    Instructor trainingInstructor = null;
                    for (Instructor inst : instructors) {
                        for (Training definedTraining : inst.getDefinedTrainings()) {
                            if (definedTraining == t) {
                                trainingInstructor = inst;
                                break;
                            }
                        }
                        if (trainingInstructor != null) break;
                    }

                    List<String> tRow = new ArrayList<>();
                    tRow.add(String.valueOf(trainingId));
                    tRow.add(String.valueOf(m.getId()));
                    tRow.add(m.getName());
                    tRow.add(day.toString());

                    if (trainingInstructor != null) {
                        tRow.add(String.valueOf(trainingInstructor.getId()));
                        tRow.add(trainingInstructor.getName());
                    } else {
                        tRow.add("0");
                        tRow.add("Sistema");
                    }

                    trainingRows.add(tRow);

                    for (Exercise ex : t.getExercises()) {
                        List<String> eRow = new ArrayList<>();
                        eRow.add(String.valueOf(exerciseId++));
                        eRow.add(String.valueOf(trainingId));
                        eRow.add(ex.getEquipment().getName());
                        eRow.add(String.valueOf(ex.getWeight()));
                        eRow.add(String.valueOf(ex.getRepetitions()));
                        eRow.add(String.valueOf(ex.getOrder()));
                        exerciseRows.add(eRow);
                    }

                    trainingId++;
                }
            }
        }

        Table trainingTable = new Table("trainings", trainingHeaders, trainingRows);
        CSV.WriteToTable(trainingTable);

        Table exerciseTable = new Table("exercises", exerciseHeaders, exerciseRows);
        CSV.WriteToTable(exerciseTable);
    }

    public static void loadInstructorTrainings(List<Instructor> instructors, List<Member> members, List<Equipment> equipments) throws IOException {
        try {
            Table trainingsTable = Database.SelectAll("trainings");
            if (trainingsTable.CountRows() == 0) {
                return;
            }

            Table exercisesTable = Database.SelectAll("exercises");

            for (List<String> trainingRow : trainingsTable.getRows()) {
                long memberId = Long.parseLong(trainingRow.get(1));
                String weekdayStr = trainingRow.get(3);
                long instructorId = Long.parseLong(trainingRow.get(4));

                // Encontra o membro
                Member member = null;
                for (Member m : members) {
                    if (m.getId() == memberId) {
                        member = m;
                        break;
                    }
                }

                // Encontra o instrutor
                Instructor instructor = null;
                for (Instructor i : instructors) {
                    if (i.getId() == instructorId) {
                        instructor = i;
                        break;
                    }
                }

                if (member != null) {
                    Weekday weekday = Weekday.valueOf(weekdayStr);

                    // Verifica se o membro já tem treino para esse dia
                    Training training = member.viewTraining(weekday);
                    if (training == null) {
                        training = new Training();
                        training.setWeekDay(weekday);
                    }

                    // Carrega exercícios deste treino
                    long trainingId = Long.parseLong(trainingRow.get(0));
                    for (List<String> exerciseRow : exercisesTable.getRows()) {
                        long exTrainingId = Long.parseLong(exerciseRow.get(1));

                        if (exTrainingId == trainingId) {
                            String equipmentName = exerciseRow.get(2);
                            double weight = Double.parseDouble(exerciseRow.get(3));
                            int reps = Integer.parseInt(exerciseRow.get(4));
                            int order = Integer.parseInt(exerciseRow.get(5));

                            // Encontra o equipamento
                            Equipment equipment = null;
                            for (Equipment e : equipments) {
                                if (e.getName().equalsIgnoreCase(equipmentName)) {
                                    equipment = e;
                                    break;
                                }
                            }

                            if (equipment != null) {
                                Exercise exercise = new Exercise();
                                exercise.setEquipment(equipment);
                                exercise.setWeight(weight);
                                exercise.setRepetitions(reps);
                                exercise.setOrder(order);
                                training.addExercise(exercise);
                            }
                        }
                    }

                    member.addTraining(weekday, training);

                    if (instructor != null && !instructor.getDefinedTrainings().contains(training)) {
                        instructor.getDefinedTrainings().add(training);
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("Error loading instructor trainings: " + e.getMessage(), e);
        }
    }

    public static void loadTrainings(List<Member> members, List<Equipment> equipments) throws IOException {
        try {
            Table trainingTable = Database.SelectAll("trainings");
            Table exerciseTable = Database.SelectAll("exercises");

            for (List<String> tRow : trainingTable.getRows()) {
                long memberId = Long.parseLong(tRow.get(1));
                int trainingId = Integer.parseInt(tRow.get(0));
                Weekday day = Weekday.valueOf(tRow.get(3));

                for (Member m : members) {
                    if (m.getId() == memberId) {
                        Training training = new Training();
                        training.setWeekDay(day);

                        for (List<String> eRow : exerciseTable.getRows()) {
                            if (Integer.parseInt(eRow.get(1)) == trainingId) {
                                String equipName = eRow.get(2);
                                Equipment equipment = null;

                                for (Equipment eq : equipments) {
                                    if (eq.getName().equals(equipName)) {
                                        equipment = eq;
                                        break;
                                    }
                                }

                                if (equipment != null) {
                                    Exercise exercise = new Exercise();
                                    exercise.setEquipment(equipment);
                                    exercise.setWeight(Double.parseDouble(eRow.get(3)));
                                    exercise.setRepetitions(Integer.parseInt(eRow.get(4)));
                                    exercise.setOrder(Integer.parseInt(eRow.get(5)));
                                    training.addExercise(exercise);
                                }
                            }
                        }

                        m.addTraining(day, training);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Nenhum treino encontrado.");
        }
    }
}
