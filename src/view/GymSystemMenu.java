package view;

import database.DataPersistence;
import model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static database.DataPersistence.TIME_FORMATTER;

public class GymSystemMenu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Gym gym = new Gym();
    private static final DataPersistence persistence = new DataPersistence();

    private static Administrator admin;
    private static Instructor currentInstructor;
    private static Member currentMember;
    private static String currentUserType = "";

    public static void main(String[] args) {
        System.out.println("===================================================");
        System.out.println("     SISTEMA DE GERENCIAMENTO - EXERCITEAKI       ");
        System.out.println("===================================================\n");

        initializeSystem();
        loadAllData();

        if (!loginSystem()) {
            System.out.println("Sistema encerrado.");
            scanner.close();
            return;
        }

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1 -> {
                    if (isAdmin()) administratorMenu();
                    else System.out.println("Acesso negado. Apenas administradores.");
                }
                case 2 -> {
                    if (isInstructor()) instructorMenu();
                    else System.out.println("Acesso negado. Apenas instrutores.");
                }
                case 3 -> {
                    if (isMember()) memberMenu();
                    else System.out.println("Acesso negado. Apenas alunos.");
                }
                case 8 -> {
                    System.out.println("\nTrocando usuário...\n");
                    saveAllData();
                    if (!loginSystem()) {
                        running = false;
                    }
                }
                case 0 -> {
                    running = false;
                    saveAllData();
                    System.out.println("\nDados salvos com sucesso.");
                    System.out.println("Sistema encerrado.");
                }
                default -> System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    // ===== INICIALIZAÇÃO E PERSISTÊNCIA =====

    private static void initializeSystem() {
        admin = new Administrator("Admin", "admin@exerciteaki.com", "(54) 3220-0001", "admin123");
    }

    private static void loadAllData() {

        persistence.loadGym(gym);
        gym.setOperatingHours(persistence.loadSchedules());

        gym.getEquipments().clear();
        gym.getEquipments().addAll(persistence.loadEquipments());

        gym.getInstructors().clear();
        gym.getInstructors().addAll(persistence.loadInstructors());

        gym.getMembers().clear();
        gym.getMembers().addAll(persistence.loadMembers());

        persistence.loadExercises(gym.getMembers(), gym.getEquipments());
        persistence.loadProgress(gym.getMembers());
        persistence.loadAttendances(gym.getMembers());

    }

    private static void saveAllData() {
        persistence.saveGym(gym);
        persistence.saveSchedules(gym.getOperatingHours());
        persistence.saveEquipments(gym.getEquipments());
        persistence.saveInstructors(gym.getInstructors());
        persistence.saveMembers(gym.getMembers());
        persistence.saveExercises(gym.getMembers());
        persistence.saveProgress(gym.getMembers());
        persistence.saveAttendances(gym.getMembers());
    }

    // ===== LOGIN E MENU PRINCIPAL =====

    private static boolean loginSystem() {
        System.out.println("===================================================");
        System.out.println("                  LOGIN NO SISTEMA                 ");
        System.out.println("===================================================");
        System.out.println("1 - Administrador");
        System.out.println("2 - Instrutor");
        System.out.println("3 - Aluno");
        System.out.println("4 - Criar nova conta (Aluno)");
        System.out.println("0 - Sair");
        System.out.print("Selecione uma opção: ");

        int choice = getIntInput();

        switch (choice) {
            case 1 -> {
                System.out.print("\nSenha do Administrador: ");
                String password = scanner.nextLine();
                if (admin.getPassword().equals(password)) {
                    currentUserType = "admin";
                    currentInstructor = null;
                    currentMember = null;
                    System.out.println("\nLogin realizado como Administrador.\n");
                    return true;
                } else {
                    System.out.println("\nSenha incorreta.\n");
                    return loginSystem();
                }
            }
            case 2 -> {
                if (gym.getInstructors().isEmpty()) {
                    System.out.println("\nNenhum instrutor cadastrado.");
                    System.out.println("Acesse a conta admin para cadastrar um instrutor.\n");
                    return loginSystem();
                }

                System.out.print("\nEmail do Instrutor: ");
                String email = scanner.nextLine();
                System.out.print("Senha: ");
                String password = scanner.nextLine();

                for (Instructor inst : gym.getInstructors()) {
                    if (inst.getEmail().equalsIgnoreCase(email) && inst.getPassword().equals(password)) {
                        currentInstructor = inst;
                        currentMember = null;
                        currentUserType = "instrutor";
                        System.out.println("\nLogin realizado: " + inst.getName() + "\n");
                        return true;
                    }
                }
                System.out.println("\nCredenciais inválidas.\n");
                return loginSystem();
            }
            case 3 -> {
                if (gym.getMembers().isEmpty()) {
                    System.out.println("\nNenhum aluno cadastrado.");
                    System.out.println("Crie uma conta (opção 4) ou peça para o admin/instrutor cadastrar.\n");
                    return loginSystem();
                }

                System.out.print("\nEmail do Aluno: ");
                String email = scanner.nextLine();
                System.out.print("Senha: ");
                String password = scanner.nextLine();

                for (Member mem : gym.getMembers()) {
                    if (mem.getEmail().equalsIgnoreCase(email) && mem.getPassword().equals(password)) {
                        currentMember = mem;
                        currentInstructor = null;
                        currentUserType = "member";
                        System.out.println("\nLogin realizado: " + mem.getName() + "\n");
                        return true;
                    }
                }
                System.out.println("\nCredenciais inválidas.\n");
                return loginSystem();
            }
            case 4 -> {
                createAccount();
                return loginSystem();
            }
            case 0 -> {
                return false;
            }
            default -> {
                System.out.println("\nOpção inválida.\n");
                return loginSystem();
            }
        }
    }

    private static void createAccount() {
        System.out.println("\n===================================================");
        System.out.println("              CRIAR NOVA CONTA - ALUNO             ");
        System.out.println("===================================================");

        System.out.print("Nome Completo: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\nNome não pode ser vazio.\n");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("\nEmail não pode ser vazio.\n");
            return;
        }
        for (Member m : gym.getMembers()) {
            if (m.getEmail().equalsIgnoreCase(email)) {
                System.out.println("\nEmail já cadastrado.\n");
                return;
            }
        }

        System.out.print("Telefone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("\nSenha não pode ser vazia.\n");
            return;
        }

        System.out.print("Confirme a Senha: ");
        String confirm = scanner.nextLine();
        if (!password.equals(confirm)) {
            System.out.println("\nAs senhas não coincidem.\n");
            return;
        }

        System.out.print("Data de nascimento (dd/mm/aaaa): ");
        String birthStr = scanner.nextLine().trim();

        System.out.print("Altura (em metros): ");
        double height = getDoubleInput();

        try {
            Member m = new Member(name, email, phone, password);
            if (!birthStr.isEmpty()) {
                m.setBirthDate(LocalDate.parse(birthStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            m.setHeight(height);
            gym.getMembers().add(m);
            persistence.saveMembers(gym.getMembers());
            System.out.println("\nConta criada com sucesso. ID: " + m.getId() + "\n");
        } catch (DateTimeParseException e) {
            System.out.println("\nData inválida. Use dd/mm/aaaa.\n");
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n===================================================");
        System.out.println("           MENU PRINCIPAL - EXERCITEAKI            ");
        System.out.println("===================================================");

        if (isAdmin()) {
            System.out.println("Usuário: Administrador");
            System.out.println("1. Menu de Administrador");
        } else if (isInstructor()) {
            System.out.println("Usuário: " + currentInstructor.getName() + " (Instrutor)");
            System.out.println("2. Menu de Instrutor");
        } else if (isMember()) {
            System.out.println("Usuário: " + currentMember.getName() + " (Aluno)");
            System.out.println("3. Menu de Aluno");
        }

        System.out.println("8. Trocar Usuário");
        System.out.println("0. Sair do Sistema");
        System.out.print("\nSelecione uma opção: ");
    }

    private static boolean isAdmin() {
        return "admin".equals(currentUserType);
    }

    private static boolean isInstructor() {
        return "instrutor".equals(currentUserType);
    }

    private static boolean isMember() {
        return "member".equals(currentUserType);
    }

    // ===== MENU ADMINISTRADOR =====

    private static void administratorMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===================================================");
            System.out.println("         MENU ADMINISTRADOR - EXERCITEAKI          ");
            System.out.println("===================================================");
            System.out.println("1. Cadastrar/Atualizar Dados da Academia");
            System.out.println("2. Atualizar Horários de Funcionamento");
            System.out.println("3. Cadastrar Instrutor");
            System.out.println("4. Alterar Instrutor");
            System.out.println("5. Excluir Instrutor");
            System.out.println("6. Consultar Instrutor");
            System.out.println("7. Cadastrar Aluno");
            System.out.println("8. Alterar Aluno");
            System.out.println("9. Excluir Aluno");
            System.out.println("10. Consultar Aluno");
            System.out.println("11. Cadastrar Aparelho");
            System.out.println("12. Alterar Aparelho");
            System.out.println("13. Excluir Aparelho");
            System.out.println("14. Consultar Aparelho");
            System.out.println("15. Visualizar Dados da Academia");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("\nSelecione uma opção: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> updateGymData();
                case 2 -> updateGymSchedule();
                case 3 -> registerInstructor();
                case 4 -> alterInstructor();
                case 5 -> removeInstructor();
                case 6 -> consultInstructor();
                case 7 -> registerMemberAdmin();
                case 8 -> alterMember();
                case 9 -> removeMember();
                case 10 -> consultMember();
                case 11 -> registerEquipment();
                case 12 -> alterEquipment();
                case 13 -> removeEquipment();
                case 14 -> consultEquipment();
                case 15 -> gym.viewGymInfo();
                case 0 -> {
                    back = true;
                    saveAllData();
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void updateGymData() {
        System.out.println("\n--- CADASTRAR/ATUALIZAR DADOS DA ACADEMIA ---");
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\nNome não pode ser vazio.\n");
            return;
        }

        System.out.print("Telefone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Website: ");
        String website = scanner.nextLine().trim();

        System.out.println("\n--- ENDEREÇO ---");
        System.out.print("Rua: ");
        String street = scanner.nextLine().trim();
        System.out.print("Número: ");
        String number = scanner.nextLine().trim();
        System.out.print("Bairro: ");
        String neighborhood = scanner.nextLine().trim();
        System.out.print("Cidade: ");
        String city = scanner.nextLine().trim();
        System.out.print("Estado (sigla): ");
        String state = scanner.nextLine().trim().toUpperCase();
        System.out.print("CEP: ");
        String zip = scanner.nextLine().trim();

        gym.setName(name);
        gym.setPhone(phone);
        gym.setWebsite(website);

        Address addr = new Address();
        addr.setStreet(street);
        addr.setNumber(number);
        addr.setNeighborhood(neighborhood);
        addr.setCity(city);
        addr.setState(state);
        addr.setZipCode(zip);
        gym.setAddress(addr);

        persistence.saveGym(gym);
        System.out.println("\nDados da academia salvos.\n");
    }

    private static void updateGymSchedule() {
        System.out.println("\n--- ATUALIZAR HORÁRIOS DE FUNCIONAMENTO ---");
        Weekday[] days = Weekday.values();
        for (int i = 0; i < days.length; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }
        System.out.print("Escolha o dia (1-7): ");
        int dayIndex = getIntInput() - 1;

        if (dayIndex < 0 || dayIndex >= days.length) {
            System.out.println("\nDia inválido.\n");
            return;
        }
        Weekday day = days[dayIndex];

        try {
            System.out.print("Horário de abertura (HH:MM): ");
            String openStr = scanner.nextLine();
            System.out.print("Horário de fechamento (HH:MM): ");
            String closeStr = scanner.nextLine();

            Schedule s = new Schedule();
            s.setOpeningTime(LocalTime.parse(openStr + ":00", TIME_FORMATTER));
            s.setClosingTime(LocalTime.parse(closeStr + ":00", TIME_FORMATTER));

            gym.getOperatingHours().put(day, s);
            persistence.saveSchedules(gym.getOperatingHours());

            System.out.println("\nHorário atualizado para " + day + ".\n");
        } catch (Exception e) {
            System.out.println("\nFormato inválido. Use HH:MM.\n");
        }
    }

    // ===== CRUD INSTRUTOR =====

    private static void registerInstructor() {
        System.out.println("\n--- CADASTRAR INSTRUTOR ---");
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\nNome não pode ser vazio.\n");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("\nEmail não pode ser vazio.\n");
            return;
        }
        for (Instructor i : gym.getInstructors()) {
            if (i.getEmail().equalsIgnoreCase(email)) {
                System.out.println("\nEmail já cadastrado.\n");
                return;
            }
        }

        System.out.print("Telefone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("\nSenha não pode ser vazia.\n");
            return;
        }

        System.out.print("Formação (Ed. Física, Fisioterapeuta, Atleta): ");
        String education = scanner.nextLine().trim();
        if (education.isEmpty()) {
            System.out.println("\nFormação não pode ser vazia.\n");
            return;
        }

        Instructor inst = new Instructor(name, email, phone, password, education);
        gym.getInstructors().add(inst);
        persistence.saveInstructors(gym.getInstructors());

        System.out.println("\nInstrutor cadastrado. ID: " + inst.getId() + "\n");
    }

    private static Instructor selectInstructorFromList() {
        List<Instructor> list = gym.getInstructors();
        if (list.isEmpty()) {
            System.out.println("\nNenhum instrutor cadastrado.\n");
            return null;
        }

        System.out.println("\n--- LISTA DE INSTRUTORES ---");
        for (int i = 0; i < list.size(); i++) {
            Instructor inst = list.get(i);
            System.out.printf("%d. %s (ID %d) - %s%n", i + 1, inst.getName(), inst.getId(), inst.getEmail());
        }
        System.out.print("\nSelecione o número (0 para cancelar): ");
        int op = getIntInput();
        if (op == 0) return null;
        if (op < 1 || op > list.size()) {
            System.out.println("\nSeleção inválida.\n");
            return null;
        }
        return list.get(op - 1);
    }

    private static void alterInstructor() {
        System.out.println("\n--- ALTERAR INSTRUTOR ---");
        Instructor inst = selectInstructorFromList();
        if (inst == null) return;

        System.out.println("\nDados atuais: " + inst.getName() + " | " + inst.getEmail());
        System.out.print("Novo email (Enter para manter): ");
        String email = scanner.nextLine().trim();
        System.out.print("Novo telefone (Enter para manter): ");
        String phone = scanner.nextLine().trim();
        System.out.print("Nova formação (Enter para manter): ");
        String education = scanner.nextLine().trim();

        if (!email.isEmpty()) inst.setEmail(email);
        if (!phone.isEmpty()) inst.setPhone(phone);
        if (!education.isEmpty()) inst.setEducation(education);

        persistence.saveInstructors(gym.getInstructors());
        System.out.println("\nInstrutor atualizado.\n");
    }

    private static void removeInstructor() {
        System.out.println("\n--- EXCLUIR INSTRUTOR ---");
        Instructor inst = selectInstructorFromList();
        if (inst == null) return;

        System.out.print("\nConfirmar exclusão de " + inst.getName() + "? (s/n): ");
        String c = scanner.nextLine();
        if (!c.equalsIgnoreCase("s")) {
            System.out.println("\nOperação cancelada.\n");
            return;
        }

        gym.getInstructors().remove(inst);
        persistence.saveInstructors(gym.getInstructors());
        System.out.println("\nInstrutor removido.\n");
    }

    private static void consultInstructor() {
        System.out.println("\n--- CONSULTAR INSTRUTOR ---");
        Instructor inst = selectInstructorFromList();
        if (inst == null) return;

        System.out.println("\n=== DADOS DO INSTRUTOR ===");
        System.out.println("ID: " + inst.getId());
        System.out.println("Nome: " + inst.getName());
        System.out.println("Email: " + inst.getEmail());
        System.out.println("Telefone: " + inst.getPhone());
        System.out.println("Formação: " + inst.getEducation());
    }

    // ===== CRUD ALUNO =====

    private static void registerMemberAdmin() {
        System.out.println("\n--- CADASTRAR ALUNO ---");
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\nNome não pode ser vazio.\n");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("\nEmail não pode ser vazio.\n");
            return;
        }
        for (Member m : gym.getMembers()) {
            if (m.getEmail().equalsIgnoreCase(email)) {
                System.out.println("\nEmail já cadastrado.\n");
                return;
            }
        }

        System.out.print("Telefone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("\nSenha não pode ser vazia.\n");
            return;
        }

        System.out.print("Data de nascimento (dd/mm/aaaa): ");
        String birthStr = scanner.nextLine().trim();

        System.out.print("Altura (em metros): ");
        double height = getDoubleInput();

        try {
            Member m = new Member(name, email, phone, password);
            if (!birthStr.isEmpty()) {
                m.setBirthDate(LocalDate.parse(birthStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            m.setHeight(height);
            gym.getMembers().add(m);
            persistence.saveMembers(gym.getMembers());
            System.out.println("\nAluno cadastrado. ID: " + m.getId() + "\n");
        } catch (DateTimeParseException e) {
            System.out.println("\nData inválida.\n");
        }
    }

    private static Member selectMemberFromList() {
        List<Member> list = gym.getMembers();
        if (list.isEmpty()) {
            System.out.println("\nNenhum aluno cadastrado.\n");
            return null;
        }

        System.out.println("\n--- LISTA DE ALUNOS ---");
        for (int i = 0; i < list.size(); i++) {
            Member m = list.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, m.getName(), m.getEmail());
        }
        System.out.print("\nSelecione o número (0 para cancelar): ");
        int op = getIntInput();
        if (op == 0) return null;
        if (op < 1 || op > list.size()) {
            System.out.println("\nSeleção inválida.\n");
            return null;
        }
        return list.get(op - 1);
    }

    private static void alterMember() {
        System.out.println("\n--- ALTERAR ALUNO ---");
        Member m = selectMemberFromList();
        if (m == null) return;

        System.out.println("\nDados atuais: " + m.getName() + " | " + m.getEmail());
        System.out.print("Novo email (Enter para manter): ");
        String email = scanner.nextLine().trim();
        System.out.print("Novo telefone (Enter para manter): ");
        String phone = scanner.nextLine().trim();

        if (!email.isEmpty()) m.setEmail(email);
        if (!phone.isEmpty()) m.setPhone(phone);

        persistence.saveMembers(gym.getMembers());
        System.out.println("\nAluno atualizado.\n");
    }

    private static void removeMember() {
        System.out.println("\n--- EXCLUIR ALUNO ---");
        Member m = selectMemberFromList();
        if (m == null) return;

        System.out.print("\nConfirmar exclusão de " + m.getName() + "? (s/n): ");
        String c = scanner.nextLine();
        if (!c.equalsIgnoreCase("s")) {
            System.out.println("\nOperação cancelada.\n");
            return;
        }

        gym.getMembers().remove(m);
        persistence.saveMembers(gym.getMembers());
        System.out.println("\nAluno removido.\n");
    }

    private static void consultMember() {
        System.out.println("\n--- CONSULTAR ALUNO ---");
        Member m = selectMemberFromList();
        if (m == null) return;

        System.out.println("\n=== DADOS DO ALUNO ===");
        System.out.println("ID: " + m.getId());
        System.out.println("Nome: " + m.getName());
        System.out.println("Email: " + m.getEmail());
        System.out.println("Telefone: " + m.getPhone());
        if (m.getBirthDate() != null) {
            System.out.println("Data de nascimento: " + m.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Idade: " + m.getAge() + " anos");
        }
        System.out.println("Altura: " + m.getHeight() + " m");
    }

    // ===== CRUD APARELHO =====

    private static void registerEquipment() {
        System.out.println("\n--- CADASTRAR APARELHO ---");
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\nNome não pode ser vazio.\n");
            return;
        }
        for (Equipment e : gym.getEquipments()) {
            if (e.getName().equalsIgnoreCase(name)) {
                System.out.println("\nJá existe aparelho com esse nome.\n");
                return;
            }
        }

        System.out.print("Descrição: ");
        String desc = scanner.nextLine().trim();
        System.out.print("Função: ");
        String func = scanner.nextLine().trim();

        Equipment e = new Equipment();
        e.setName(name);
        e.setDescription(desc);
        e.setFunction(func);

        gym.getEquipments().add(e);
        persistence.saveEquipments(gym.getEquipments());
        System.out.println("\nAparelho cadastrado.\n");
    }

    private static Equipment selectEquipmentFromList() {
        List<Equipment> list = gym.getEquipments();
        if (list.isEmpty()) {
            System.out.println("\nNenhum aparelho cadastrado.\n");
            return null;
        }

        System.out.println("\n--- LISTA DE APARELHOS ---");
        for (int i = 0; i < list.size(); i++) {
            Equipment e = list.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, e.getName(), e.getDescription());
        }
        System.out.print("\nSelecione o número (0 para cancelar): ");
        int op = getIntInput();
        if (op == 0) return null;
        if (op < 1 || op > list.size()) {
            System.out.println("\nSeleção inválida.\n");
            return null;
        }
        return list.get(op - 1);
    }

    private static void alterEquipment() {
        System.out.println("\n--- ALTERAR APARELHO ---");
        Equipment e = selectEquipmentFromList();
        if (e == null) return;

        System.out.println("\nDados atuais: " + e.getName());
        System.out.println("Descrição: " + e.getDescription());
        System.out.println("Função: " + e.getFunction());

        System.out.print("Nova descrição (Enter para manter): ");
        String desc = scanner.nextLine().trim();
        System.out.print("Nova função (Enter para manter): ");
        String func = scanner.nextLine().trim();

        if (!desc.isEmpty()) e.setDescription(desc);
        if (!func.isEmpty()) e.setFunction(func);

        persistence.saveEquipments(gym.getEquipments());
        System.out.println("\nAparelho atualizado.\n");
    }

    private static void removeEquipment() {
        System.out.println("\n--- EXCLUIR APARELHO ---");
        Equipment e = selectEquipmentFromList();
        if (e == null) return;

        System.out.print("\nConfirmar exclusão de " + e.getName() + "? (s/n): ");
        String c = scanner.nextLine();
        if (!c.equalsIgnoreCase("s")) {
            System.out.println("\nOperação cancelada.\n");
            return;
        }

        gym.getEquipments().remove(e);
        persistence.saveEquipments(gym.getEquipments());
        System.out.println("\nAparelho removido.\n");
    }

    private static void consultEquipment() {
        System.out.println("\n--- CONSULTAR APARELHO ---");
        Equipment e = selectEquipmentFromList();
        if (e == null) return;

        System.out.println("\n=== DADOS DO APARELHO ===");
        System.out.println("Nome: " + e.getName());
        System.out.println("Descrição: " + e.getDescription());
        System.out.println("Função: " + e.getFunction());
    }

    // ===== MENU INSTRUTOR =====

    private static void instructorMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===================================================");
            System.out.println("          MENU INSTRUTOR - EXERCITEAKI             ");
            System.out.println("===================================================");
            System.out.println("Instrutor: " + currentInstructor.getName());
            System.out.println("---------------------------------------------------");
            System.out.println("1. Definir/Alterar Treino para Aluno");
            System.out.println("2. Registrar Evolução de Aluno");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("\nSelecione uma opção: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> defineTraining();
                case 2 -> registerProgressByInstructor();
                case 0 -> {
                    back = true;
                    saveAllData();
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void registerProgressByInstructor() {
        System.out.println("\n--- REGISTRAR EVOLUÇÃO DO ALUNO ---");

        Member member = selectMemberFromList();
        if (member == null) return;

        System.out.print("Peso (kg): ");
        double weight = getDoubleInput();

        System.out.print("Percentual de massa muscular (%): ");
        double muscle = getDoubleInput();

        Progress p = new Progress();
        p.setDate(LocalDate.now());
        p.setWeight(weight);
        p.setMuscleMassPercent(muscle);

        member.viewProgress().add(p);

        persistence.saveProgress(gym.getMembers());

        System.out.println("\nEvolução registrada e salva para " +
                member.getName() + " na data " +
                p.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".\n");
    }

    private static void defineTraining() {
        System.out.println("\n--- DEFINIR/ALTERAR TREINO PARA ALUNO ---");

        Member member = selectMemberFromList();
        if (member == null) return;

        // escolher dia da semana
        System.out.println("\nSelecione o dia da semana:");
        Weekday[] days = Weekday.values();
        for (int i = 0; i < days.length; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }
        System.out.print("Escolha (1-7): ");
        int dayIndex = getIntInput() - 1;
        if (dayIndex < 0 || dayIndex >= days.length) {
            System.out.println("\nDia inválido.\n");
            return;
        }
        Weekday day = days[dayIndex];

        Training training = new Training();
        training.setWeekDay(day);
        training.getExercises().clear();

        boolean adding = true;
        int order = 1;

        while (adding) {
            System.out.println("\n--- Exercício #" + order + " ---");

            Equipment eq = selectEquipmentFromList();
            if (eq == null) {
                System.out.println("Nenhum aparelho selecionado. Encerrando definição.");
                break;
            }

            System.out.print("Carga (kg): ");
            double weight = getDoubleInput();

            System.out.print("Repetições: ");
            int reps = getIntInput();

            Exercise ex = new Exercise();
            ex.setOrder(order);
            ex.setEquipment(eq);
            ex.setWeight(weight);
            ex.setRepetitions(reps);
            training.getExercises().add(ex);

            System.out.print("\nAdicionar outro exercício? (s/n): ");
            String c = scanner.nextLine();
            if (!c.equalsIgnoreCase("s")) {
                adding = false;
            } else {
                order++;
            }
        }

        if (training.getExercises().isEmpty()) {
            System.out.println("\nTreino vazio. Nada foi salvo.\n");
            return;
        }

        member.getTrainings().put(day, training);

        persistence.saveExercises(gym.getMembers());

        System.out.println("\nTreino salvo para " + day + " com " +
                training.getExercises().size() + " exercício(s).\n");
    }

    // ===== MENU ALUNO =====

    private static void memberMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===================================================");
            System.out.println("            MENU ALUNO - EXERCITEAKI               ");
            System.out.println("===================================================");
            System.out.println("Aluno: " + currentMember.getName());
            System.out.println("---------------------------------------------------");
            System.out.println("1. Consultar Meu Treino do Dia");
            System.out.println("2. Ver Minha Evolução");
            System.out.println("3. Registrar Entrada (Check-in)");
            System.out.println("4. Registrar Saída (Check-out)");
            System.out.println("5. Ver Relatório de Frequência");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("\nSelecione uma opção: ");

            int choice = getIntInput();
            switch (choice) {
                case 1 -> viewMyTraining();
                case 2 -> viewMyProgress();
                case 3 -> registerEntry();
                case 4 -> registerExit();
                case 5 -> viewMyAttendanceReport();
                case 0 -> {
                    back = true;
                    saveAllData();
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void registerEntry() {
        System.out.println("\n--- REGISTRAR ENTRADA (CHECK-IN) ---");

        Attendance a = new Attendance();
        a.setDate(LocalDate.now());
        a.setEntryTime(LocalTime.now());
        a.setExitTime(null);

        currentMember.viewAttendances().add(a);
        persistence.saveAttendances(gym.getMembers());

        System.out.println("\nEntrada registrada.");
        System.out.println("Data: " + a.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Horário: " + a.getEntryTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n");
    }

    private static void registerExit() {
        System.out.println("\n--- REGISTRAR SAÍDA (CHECK-OUT) ---");

        List<Attendance> list = currentMember.viewAttendances();
        if (list.isEmpty()) {
            System.out.println("\nNenhuma entrada encontrada.\n");
            return;
        }

        Attendance open = null;
        for (int i = list.size() - 1; i >= 0; i--) {
            Attendance a = list.get(i);
            if (a.getExitTime() == null) {
                open = a;
                break;
            }
        }

        if (open == null) {
            System.out.println("\nNenhuma entrada pendente para este aluno.\n");
            return;
        }

        open.setExitTime(LocalTime.now());
        persistence.saveAttendances(gym.getMembers());

        System.out.println("\nSaída registrada.");
        System.out.println("Data: " + open.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Horário de entrada: " + open.getEntryTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("Horário de saída: " + open.getExitTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        try {
            double hours = open.calculateHours();
            System.out.println("Tempo total: " + String.format("%.2f", hours) + " horas\n");
        } catch (Exception e) {
            System.out.println("Não foi possível calcular o tempo.\n");
        }
    }

    private static void viewMyAttendanceReport() {
        System.out.println("\n--- RELATÓRIO DE FREQUÊNCIA ---");
        System.out.println("1. Ver Últimas 10 Visitas");
        System.out.println("2. Ver por Período");
        System.out.print("Escolha: ");

        int choice = getIntInput();
        switch (choice) {
            case 1 -> reportLast10Attendances();
            case 2 -> reportAttendanceByPeriod();
            default -> System.out.println("\nOpção inválida.\n");
        }
    }

    private static void reportLast10Attendances() {
        List<Attendance> list = currentMember.viewAttendances();
        System.out.println("\n--- ÚLTIMAS 10 VISITAS ---");

        if (list.isEmpty()) {
            System.out.println("\nNenhuma frequência registrada.\n");
            return;
        }

        System.out.println("Aluno: " + currentMember.getName());
        System.out.println("Data       | Entrada  | Saída    | Horas");
        System.out.println("-------------------------------------------");

        int count = 0;
        for (int i = list.size() - 1; i >= 0 && count < 10; i--, count++) {
            Attendance a = list.get(i);
            String dateStr = a.getDate() != null
                    ? a.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "";
            String entryStr = a.getEntryTime() != null
                    ? a.getEntryTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                    : "";
            String exitStr = a.getExitTime() != null
                    ? a.getExitTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                    : "";

            String hoursStr = "";
            try {
                if (a.getExitTime() != null) {
                    hoursStr = String.format("%.2f", a.calculateHours());
                } else {
                    hoursStr = "Em andamento";
                }
            } catch (Exception e) {
                hoursStr = "Erro";
            }

            System.out.printf("%10s | %8s | %8s | %s%n",
                    dateStr, entryStr, exitStr, hoursStr);
        }
        System.out.println();
    }

    private static void reportAttendanceByPeriod() {
        System.out.println("\n--- RELATÓRIO POR PERÍODO ---");
        System.out.print("Data inicial (dd/mm/aaaa): ");
        String startStr = scanner.nextLine().trim();
        System.out.print("Data final (dd/mm/aaaa): ");
        String endStr = scanner.nextLine().trim();

        try {
            LocalDate start = LocalDate.parse(startStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate end = LocalDate.parse(endStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (start.isAfter(end)) {
                System.out.println("\nData inicial não pode ser maior que data final.\n");
                return;
            }

            List<Attendance> list = currentMember.viewAttendances();
            List<Attendance> filtered = new ArrayList<>();
            double totalHours = 0;

            for (Attendance a : list) {
                if (a.getDate() == null) continue;
                if (!a.getDate().isBefore(start) && !a.getDate().isAfter(end)) {
                    filtered.add(a);
                    try {
                        if (a.getExitTime() != null) {
                            totalHours += a.calculateHours();
                        }
                    } catch (Exception ignore) {
                    }
                }
            }

            System.out.println("\nAluno: " + currentMember.getName());
            System.out.println("Período: " + startStr + " a " + endStr);
            System.out.println("Total de comparecimentos: " + filtered.size());
            System.out.println("Total de horas no período: " + String.format("%.2f", totalHours) + " h\n");

            if (filtered.isEmpty()) return;

            System.out.println("Data       | Entrada  | Saída    | Horas");
            System.out.println("-------------------------------------------");
            for (Attendance a : filtered) {
                String dateStr = a.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String entryStr = a.getEntryTime() != null
                        ? a.getEntryTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        : "";
                String exitStr = a.getExitTime() != null
                        ? a.getExitTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                        : "";

                String hoursStr = "";
                try {
                    if (a.getExitTime() != null) {
                        hoursStr = String.format("%.2f", a.calculateHours());
                    } else {
                        hoursStr = "Incompleto";
                    }
                } catch (Exception e) {
                    hoursStr = "Erro";
                }

                System.out.printf("%10s | %8s | %8s | %s%n",
                        dateStr, entryStr, exitStr, hoursStr);
            }
            System.out.println();
        } catch (DateTimeParseException e) {
            System.out.println("\nFormato de data inválido. Use dd/mm/aaaa.\n");
        }
    }

    private static void viewMyProgress() {
        System.out.println("\n--- MINHA EVOLUÇÃO ---");
        List<Progress> list = currentMember.viewProgress();

        if (list.isEmpty()) {
            System.out.println("\nNenhum registro de evolução encontrado.\n");
            return;
        }

        System.out.println("Aluno: " + currentMember.getName());
        System.out.println("Total de registros: " + list.size());
        System.out.println("\nData       | Peso (kg) | Massa Muscular (%)");
        System.out.println("--------------------------------------------");

        for (Progress p : list) {
            String d = p.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.printf("%10s | %8.2f | %18.2f%n", d, p.getWeight(), p.getMuscleMassPercent());
        }
        System.out.println();
    }

    private static void viewMyTraining() {
        System.out.println("\n--- CONSULTAR MEU TREINO ---");
        System.out.println("Selecione o dia:");

        Weekday[] days = Weekday.values();
        for (int i = 0; i < days.length; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }
        System.out.print("Escolha (1-7): ");
        int dayIndex = getIntInput() - 1;
        if (dayIndex < 0 || dayIndex >= days.length) {
            System.out.println("\nDia inválido.\n");
            return;
        }
        Weekday day = days[dayIndex];

        Training training = currentMember.getTrainings().get(day);
        if (training == null || training.getExercises().isEmpty()) {
            System.out.println("\nNenhum treino cadastrado para " + day + ".\n");
            return;
        }

        System.out.println("\nTreino de " + day + " para " + currentMember.getName());
        System.out.println("Total de exercícios: " + training.getExercises().size());

        for (Exercise ex : training.getExercises()) {
            System.out.println("\n" + ex.getOrder() + ". " + ex.getEquipment().getName());
            System.out.println("   Carga: " + ex.getWeight() + " kg");
            System.out.println("   Repetições: " + ex.getRepetitions());
            System.out.println("   Função: " + ex.getEquipment().getFunction());
        }
        System.out.println();
    }

    // ===== AUXILIARES GERAIS =====

    private static int getIntInput() {
        while (true) {
            try {
                if (scanner.hasNextInt()) {
                    int v = scanner.nextInt();
                    scanner.nextLine();
                    return v;
                } else {
                    System.out.print("Entrada inválida. Digite um número: ");
                    scanner.nextLine();
                }
            } catch (Exception e) {
                System.out.print("Erro. Digite novamente: ");
                scanner.nextLine();
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                String s = scanner.nextLine().replace(',', '.').trim();
                if (s.isEmpty()) {
                    System.out.print("Entrada vazia. Digite um número: ");
                    continue;
                }
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número decimal: ");
            }
        }
    }
}
