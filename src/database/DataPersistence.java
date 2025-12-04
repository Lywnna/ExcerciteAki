package database;

import model.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataPersistence {

    private static final String DATA_DIR = "src" + File.separator + "Tables" + File.separator;

    private static final String GYM_FILE = DATA_DIR + "gym.csv";
    private static final String SCHEDULES_FILE = DATA_DIR + "schedules.csv";
    private static final String INSTRUCTORS_FILE = DATA_DIR + "instructors.csv";
    private static final String MEMBERS_FILE = DATA_DIR + "members.csv";
    private static final String EQUIPMENTS_FILE = DATA_DIR + "equipments.csv";
    private static final String EXERCISES_FILE = DATA_DIR + "exercises.csv";
    private static final String PROGRESS_FILE = DATA_DIR + "progress.csv";
    private static final String ATTENDANCE_FILE = DATA_DIR + "attendance.csv";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public DataPersistence() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("Pasta 'data' criada em: " + dir.getAbsolutePath());
            } else {
                System.err.println("ERRO: não foi possível criar a pasta 'data'!");
            }
        }
    }

    // ========= Frequência =========

    public void saveAttendances(List<Member> members) {
        try {
            File file = new File(ATTENDANCE_FILE);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("memberId;date;entryTime;exitTime");

            for (Member member : members) {
                List<Attendance> list = member.viewAttendances();
                if (list == null) continue;

                for (Attendance a : list) {
                    String dateStr = a.getDate() != null
                            ? a.getDate().format(DATE_FORMATTER)
                            : "";
                    String entryStr = a.getEntryTime() != null
                            ? a.getEntryTime().format(TIME_FORMATTER)
                            : "";
                    String exitStr = a.getExitTime() != null
                            ? a.getExitTime().format(TIME_FORMATTER)
                            : "";

                    writer.printf("%d;%s;%s;%s%n",
                            member.getId(),
                            dateStr,
                            entryStr,
                            exitStr
                    );
                }
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar frequências: " + e.getMessage());
        }
    }

    public void loadAttendances(List<Member> members) {
        File file = new File(ATTENDANCE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = parseCSVLine(line);
                if (p.length < 4) continue;

                long memberId = Long.parseLong(p[0]);
                String dateStr = p[1];
                String entryStr = p[2];
                String exitStr = p[3];

                // localizar aluno
                Member member = null;
                for (Member m : members) {
                    if (m.getId() == memberId) {
                        member = m;
                        break;
                    }
                }
                if (member == null) continue;

                Attendance a = new Attendance();
                if (!dateStr.isEmpty()) {
                    a.setDate(LocalDate.parse(dateStr, DATE_FORMATTER));
                }
                if (!entryStr.isEmpty()) {
                    a.setEntryTime(LocalTime.parse(entryStr, TIME_FORMATTER));
                }
                if (!exitStr.isEmpty()) {
                    a.setExitTime(LocalTime.parse(exitStr, TIME_FORMATTER));
                }

                member.viewAttendances().add(a);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar frequências: " + e.getMessage());
        }
    }

    // ========= Progresso =========

    public void saveProgress(List<Member> members) {
        try {
            File file = new File(PROGRESS_FILE);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("memberId;date;weight;muscleMassPercent");

            for (Member member : members) {
                List<Progress> list = member.viewProgress();
                if (list == null) continue;

                for (Progress p : list) {
                    writer.printf(java.util.Locale.US, "%d;%s;%.2f;%.2f%n",
                            member.getId(),
                            p.getDate().format(DATE_FORMATTER),
                            p.getWeight(),
                            p.getMuscleMassPercent()
                    );
                }
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar evolução: " + e.getMessage());
        }
    }

    public void loadProgress(List<Member> members) {
        File file = new File(PROGRESS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = parseCSVLine(line);
                if (p.length < 4) continue;

                long memberId = Long.parseLong(p[0]);
                LocalDate date = LocalDate.parse(p[1], DATE_FORMATTER);
                double weight = Double.parseDouble(p[2]);
                double muscle = Double.parseDouble(p[3]);

                // localizar aluno
                Member member = null;
                for (Member m : members) {
                    if (m.getId() == memberId) {
                        member = m;
                        break;
                    }
                }
                if (member == null) continue;

                Progress prog = new Progress();
                prog.setDate(date);
                prog.setWeight(weight);
                prog.setMuscleMassPercent(muscle);

                member.viewProgress().add(prog);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar evolução: " + e.getMessage());
        }
    }

    // ========= GYM =========

    public void saveGym(Gym gym) {
        try {
            File file = new File(GYM_FILE);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("name;phone;website;street;number;neighborhood;city;state;zipCode");

            Address addr = gym.getAddress();
            String street = addr != null ? addr.getStreet() : "";
            String number = addr != null ? addr.getNumber() : "";
            String neighborhood = addr != null ? addr.getNeighborhood() : "";
            String city = addr != null ? addr.getCity() : "";
            String state = addr != null ? addr.getState() : "";
            String zip = addr != null ? addr.getZipCode() : "";

            writer.printf(java.util.Locale.US, "%s;%s;%s;%s;%s;%s;%s;%s;%s%n",
                    escapeCSV(gym.getName()),
                    escapeCSV(gym.getPhone()),
                    escapeCSV(gym.getWebsite()),
                    escapeCSV(street),
                    escapeCSV(number),
                    escapeCSV(neighborhood),
                    escapeCSV(city),
                    escapeCSV(state),
                    escapeCSV(zip)
            );

            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar academia: " + e.getMessage());
        }
    }

    public void loadGym(Gym gym) {
        File file = new File(GYM_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line = reader.readLine();
            if (line != null) {
                String[] p = parseCSVLine(line);
                if (p.length >= 9) {
                    gym.setName(p[0]);
                    gym.setPhone(p[1]);
                    gym.setWebsite(p[2]);

                    Address addr = new Address();
                    addr.setStreet(p[3]);
                    addr.setNumber(p[4]);
                    addr.setNeighborhood(p[5]);
                    addr.setCity(p[6]);
                    addr.setState(p[7]);
                    addr.setZipCode(p[8]);
                    gym.setAddress(addr);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar academia: " + e.getMessage());
        }
    }

    // ========= SCHEDULES =========

    public void saveSchedules(Map<Weekday, Schedule> schedules) {
        try {
            File file = new File(SCHEDULES_FILE);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("weekday;openingTime;closingTime");

            for (Map.Entry<Weekday, Schedule> entry : schedules.entrySet()) {
                Schedule s = entry.getValue();
                writer.printf("%s;%s;%s%n",
                        entry.getKey().name(),
                        s.getOpeningTime().format(TIME_FORMATTER),
                        s.getClosingTime().format(TIME_FORMATTER)
                );
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar horários: " + e.getMessage());
        }
    }

    public Map<Weekday, Schedule> loadSchedules() {
        Map<Weekday, Schedule> map = new HashMap<>();
        File file = new File(SCHEDULES_FILE);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = parseCSVLine(line);
                if (p.length >= 3) {
                    Weekday day = Weekday.valueOf(p[0]);
                    Schedule s = new Schedule();
                    s.setOpeningTime(LocalTime.parse(p[1], TIME_FORMATTER));
                    s.setClosingTime(LocalTime.parse(p[2], TIME_FORMATTER));
                    map.put(day, s);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar horários: " + e.getMessage());
        }
        return map;
    }

    // ========= INSTRUCTORS =========

    public void saveInstructors(List<Instructor> list) {
        try {
            File file = new File(INSTRUCTORS_FILE);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("id;name;email;phone;password;education");

            for (Instructor i : list) {
                writer.printf(java.util.Locale.US, "%d;%s;%s;%s;%s;%s%n",
                        i.getId(),
                        escapeCSV(i.getName()),
                        escapeCSV(i.getEmail()),
                        escapeCSV(i.getPhone()),
                        escapeCSV(i.getPassword()),
                        escapeCSV(i.getEducation())
                );
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar instrutores: " + e.getMessage());
        }
    }

    public List<Instructor> loadInstructors() {
        List<Instructor> list = new ArrayList<>();
        File file = new File(INSTRUCTORS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = parseCSVLine(line);
                if (p.length >= 6) {
                    Instructor i = new Instructor(
                            p[1], // name
                            p[2], // email
                            p[3], // phone
                            p[4], // password
                            p[5]  // education
                    );
                    list.add(i);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar instrutores: " + e.getMessage());
        }
        return list;
    }

    // ========= MEMBERS =========

    public void saveMembers(List<Member> list) {
        try {
            File file = new File(MEMBERS_FILE);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("id;name;email;phone;password;birthDate;height");

            for (Member m : list) {
                String birth = m.getBirthDate() != null ? m.getBirthDate().format(DATE_FORMATTER) : "";
                writer.printf(java.util.Locale.US,"%d;%s;%s;%s;%s;%s;%.2f%n",
                        m.getId(),
                        escapeCSV(m.getName()),
                        escapeCSV(m.getEmail()),
                        escapeCSV(m.getPhone()),
                        escapeCSV(m.getPassword()),
                        birth,
                        m.getHeight()
                );
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar alunos: " + e.getMessage());
        }
    }

    public List<Member> loadMembers() {
        List<Member> list = new ArrayList<>();
        File file = new File(MEMBERS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = parseCSVLine(line);
                if (p.length >= 7) {
                    Member m = new Member(
                            p[1], // name
                            p[2], // email
                            p[3], // phone
                            p[4]  // password
                    );
                    if (!p[5].isEmpty()) {
                        m.setBirthDate(LocalDate.parse(p[5], DATE_FORMATTER));
                    }
                    if (!p[6].isEmpty()) {
                        m.setHeight(Double.parseDouble(p[6]));
                    }
                    list.add(m);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar alunos: " + e.getMessage());
        }
        return list;
    }

    // ========= EQUIPMENTS =========

    public void saveEquipments(List<Equipment> list) {
        try {
            File file = new File(EQUIPMENTS_FILE);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("name;description;function");

            for (Equipment e : list) {
                writer.printf(java.util.Locale.US, "%s;%s;%s%n",
                        escapeCSV(e.getName()),
                        escapeCSV(e.getDescription()),
                        escapeCSV(e.getFunction())
                );
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar aparelhos: " + e.getMessage());
        }
    }

    public List<Equipment> loadEquipments() {
        List<Equipment> list = new ArrayList<>();
        File file = new File(EQUIPMENTS_FILE);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = parseCSVLine(line);
                if (p.length >= 3) {
                    Equipment e = new Equipment();
                    e.setName(p[0]);
                    e.setDescription(p[1]);
                    e.setFunction(p[2]);
                    list.add(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar aparelhos: " + e.getMessage());
        }
        return list;
    }

    // ========= Exercises =========

    public void saveExercises(List<Member> members) {
        try {
            File file = new File(EXERCISES_FILE);
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.printf(java.util.Locale.US, "memberId;weekday;order;equipmentName;weight;repetitions%n");

            for (Member member : members) {
                Map<Weekday, Training> trainings = member.getTrainings();
                if (trainings == null) continue;

                for (Map.Entry<Weekday, Training> entry : trainings.entrySet()) {
                    Weekday day = entry.getKey();
                    Training training = entry.getValue();
                    if (training == null || training.getExercises() == null) continue;

                    for (Exercise ex : training.getExercises()) {
                        writer.printf(java.util.Locale.US,"%d;%s;%d;%s;%.2f;%d%n",
                                member.getId(),
                                day.name(),
                                ex.getOrder(),
                                escapeCSV(ex.getEquipment().getName()),
                                ex.getWeight(),
                                ex.getRepetitions()
                        );
                    }
                }
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Erro ao salvar exercícios: " + e.getMessage());
        }
    }

    public void loadExercises(List<Member> members, List<Equipment> equipments) {
        File file = new File(EXERCISES_FILE);
        if (!file.exists()) return;

        Map<String, Equipment> eqMap = new HashMap<>();
        for (Equipment e : equipments) {
            eqMap.put(e.getName(), e);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = parseCSVLine(line);
                if (p.length < 6) continue;

                long memberId = Long.parseLong(p[0]);
                Weekday day = Weekday.valueOf(p[1]);
                int order = Integer.parseInt(p[2]);
                String equipmentName = p[3];
                double weight = Double.parseDouble(p[4]);
                int reps = Integer.parseInt(p[5]);

                // localizar aluno
                Member member = null;
                for (Member m : members) {
                    if (m.getId() == memberId) {
                        member = m;
                        break;
                    }
                }
                if (member == null) continue;

                // localizar equipamento
                Equipment eq = eqMap.get(equipmentName);
                if (eq == null) continue;

                // pegar ou criar Training
                Map<Weekday, Training> trainings = member.getTrainings();
                if (trainings == null) continue;

                Training training = trainings.get(day);
                if (training == null) {
                    training = new Training();
                    training.setWeekDay(day);
                    trainings.put(day, training);
                }

                // criar exercício
                Exercise ex = new Exercise();
                ex.setOrder(order);
                ex.setEquipment(eq);
                ex.setWeight(weight);
                ex.setRepetitions(reps);

                training.getExercises().add(ex);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar exercícios: " + e.getMessage());
        }
    }

    // ========= AUXILIARES =========

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(";") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ';' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());

        return result.toArray(new String[0]);
    }
}
