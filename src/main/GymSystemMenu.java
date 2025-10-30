package main;

import model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GymSystemMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Gym gym = new Gym();
    private static Administrator admin;
    private static Instructor currentInstructor;
    private static Member currentMember;
    private static String currentUserType = "";

    public static void main(String[] args) {
        System.out.println("===================================================");
        System.out.println("     SISTEMA DE GERENCIAMENTO - EXERCITEAKI       ");
        System.out.println("===================================================\n");

        initializeSystem();

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
                    if (!loginSystem()) {
                        running = false;
                    }
                }
                case 0 -> {
                    running = false;
                    System.out.println("\nSistema encerrado!");
                }
                default -> System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    private static void initializeSystem() {
        admin = new Administrator("Admin", "admin@exerciteaki.com", "(54) 3220-0001", "admin123");
        System.out.println("Sistema ExerciteAki inicializado com sucesso!\n");
    }

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
                    System.out.println("\nLogin realizado como Administrador!\n");
                    return true;
                } else {
                    System.out.println("\nSenha incorreta!\n");
                    return loginSystem();
                }
            }
            case 2 -> {
                if (gym.getInstructors().isEmpty()) {
                    System.out.println("\nNenhum instrutor cadastrado!");
                    System.out.println("Acesse a conta admin para cadastrar um instrutor!\n");
                    return loginSystem();
                }

                System.out.print("\nEmail do Instrutor: ");
                String email = scanner.nextLine();
                System.out.print("Senha: ");
                String password = scanner.nextLine();

                for (Instructor inst : gym.getInstructors()) {
                    if (inst.getEmail().equalsIgnoreCase(email) && inst.getPassword().equals(password)) {
                        currentInstructor = inst;
                        currentUserType = "instrutor";
                        System.out.println("\nLogin realizado: " + inst.getName() + "\n");
                        return true;
                    }
                }
                System.out.println("\nCredenciais inválidas!\n");
                return loginSystem();
            }
            case 3 -> {
                if (gym.getMembers().isEmpty()) {
                    System.out.println("\nNenhum aluno cadastrado!");
                    System.out.println("Crie uma conta (opção 4).\n");
                    return loginSystem();
                }

                System.out.print("\nEmail do Aluno: ");
                String email = scanner.nextLine();
                System.out.print("Senha: ");
                String password = scanner.nextLine();

                for (Member mem : gym.getMembers()) {
                    if (mem.getEmail().equalsIgnoreCase(email) && mem.getPassword().equals(password)) {
                        currentMember = mem;
                        currentUserType = "member";
                        System.out.println("\nLogin realizado: " + mem.getName() + "\n");
                        return true;
                    }
                }
                System.out.println("\nCredenciais inválidas!\n");
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
                System.out.println("\nOpção inválida!\n");
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
            System.out.println("\nNome não pode ser vazio!\n");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("\nEmail não pode ser vazio!\n");
            return;
        }

        for (Member m : gym.getMembers()) {
            if (m.getEmail().equalsIgnoreCase(email)) {
                System.out.println("\nEmail já cadastrado!\n");
                return;
            }
        }

        System.out.print("Telefone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("\nSenha não pode ser vazia!\n");
            return;
        }

        System.out.print("Confirme a Senha: ");
        String confirmPassword = scanner.nextLine();
        if (!password.equals(confirmPassword)) {
            System.out.println("\nAs senhas não coincidem!\n");
            return;
        }

        System.out.print("Data de nascimento (dd/mm/aaaa): ");
        String birthDateStr = scanner.nextLine().trim();
        if (birthDateStr.isEmpty()) {
            System.out.println("\nData inválida!\n");
            return;
        }

        System.out.print("Altura (em metros): ");
        double height = getDoubleInput();
        if (height <= 0 || height > 3.0) {
            System.out.println("\nAltura inválida!\n");
            return;
        }

        try {
            Member member = new Member(name, email, phone, password);
            member.setBirthDate(LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            member.setHeight(height);
            gym.getMembers().add(member);

            System.out.println("\nConta criada com sucesso! ID: " + member.getId());
            System.out.println("Você já pode fazer login.\n");

        } catch (DateTimeParseException e) {
            System.out.println("\nData inválida! Use dd/mm/aaaa\n");
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
        return currentUserType.equals("admin");
    }

    private static boolean isInstructor() {
        return currentUserType.equals("instrutor");
    }

    private static boolean isMember() {
        return currentUserType.equals("member");
    }

    private static int getIntInput() {
        while (true) {
            try {
                if (scanner.hasNextInt()) {
                    int input = scanner.nextInt();
                    scanner.nextLine();
                    return input;
                } else {
                    System.out.print("Entrada inválida. Digite um número: ");
                    scanner.nextLine();
                }
            } catch (Exception e) {
                System.out.print("Erro. Tente novamente: ");
                scanner.nextLine();
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                String input = scanner.nextLine().replace(',', '.').trim();
                if (input.isEmpty()) {
                    System.out.print("Entrada vazia. Digite um número: ");
                    continue;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Digite um número decimal: ");
            }
        }
    }

    // ===== MENU ADMINISTRADOR =====
    private static void administratorMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===================================================");
            System.out.println("         MENU ADMINISTRADOR - EXERCITEAKI          ");
            System.out.println("===================================================");
            System.out.println("1. Cadastrar Dados da Academia");
            System.out.println("2. Atualizar Horários de Funcionamento");
            System.out.println("3. Cadastrar Instrutor");
            System.out.println("4. Alterar Dados de Instrutor");
            System.out.println("5. Excluir Instrutor");
            System.out.println("6. Consultar Instrutor (por código ou nome)");
            System.out.println("7. Cadastrar Aluno");
            System.out.println("8. Alterar Dados de Aluno");
            System.out.println("9. Excluir Aluno");
            System.out.println("10. Consultar Aluno (por código ou nome)");
            System.out.println("11. Cadastrar Aparelho");
            System.out.println("12. Alterar Dados de Aparelho");
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
                case 7 -> registerMember();
                case 8 -> alterMember();
                case 9 -> removeMember();
                case 10 -> consultMember();
                case 11 -> registerEquipment();
                case 12 -> alterEquipment();
                case 13 -> removeEquipment();
                case 14 -> consultEquipment();
                case 15 -> viewGymInfo();
                case 0 -> back = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void updateGymData() {
        System.out.println("\n--- CADASTRAR/ATUALIZAR DADOS DA ACADEMIA ---");
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\nNome não pode ser vazio!\n");
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
        String zipCode = scanner.nextLine().trim();

        gym.setName(name);
        gym.setPhone(phone);
        gym.setWebsite(website);

        Address address = new Address();
        address.setStreet(street);
        address.setNumber(number);
        address.setNeighborhood(neighborhood);
        address.setCity(city);
        address.setState(state);
        address.setZipCode(zipCode);
        gym.setAddress(address);

        System.out.println("\nDados atualizados com sucesso!\n");
    }

    private static void updateGymSchedule() {
        System.out.println("\n--- ATUALIZAR HORÁRIOS DE FUNCIONAMENTO ---");
        Weekday[] days = Weekday.values();
        for (int i = 0; i < days.length; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }
        System.out.print("Escolha o dia (1-7): ");
        int dayChoice = getIntInput() - 1;

        if (dayChoice >= 0 && dayChoice < days.length) {
            try {
                System.out.print("Horário de Abertura (HH:MM): ");
                String openTime = scanner.nextLine();
                System.out.print("Horário de Fechamento (HH:MM): ");
                String closeTime = scanner.nextLine();

                Schedule schedule = new Schedule();
                schedule.setOpeningTime(LocalTime.parse(openTime));
                schedule.setClosingTime(LocalTime.parse(closeTime));

                admin.updateGymSchedule(gym, days[dayChoice], schedule);
                System.out.println("\nHorário atualizado para " + days[dayChoice] + "!\n");
            } catch (DateTimeParseException e) {
                System.out.println("\nFormato inválido! Use HH:MM\n");
            }
        } else {
            System.out.println("\nDia inválido!\n");
        }
    }

    private static void registerInstructor() {
        System.out.println("\n--- CADASTRAR INSTRUTOR ---");
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\nNome não pode ser vazio!\n");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("\nEmail não pode ser vazio!\n");
            return;
        }

        for (Instructor i : gym.getInstructors()) {
            if (i.getEmail().equalsIgnoreCase(email)) {
                System.out.println("\nEmail já cadastrado!\n");
                return;
            }
        }

        System.out.print("Telefone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("\nSenha não pode ser vazia!\n");
            return;
        }

        System.out.print("Formação Acadêmica: ");
        String education = scanner.nextLine().trim();
        if (education.isEmpty()) {
            System.out.println("\nFormação não pode ser vazia!\n");
            return;
        }

        Instructor instructor = new Instructor(name, email, phone, password, education);
        admin.registerInstructor(gym, instructor);
        System.out.println("\nInstrutor cadastrado! ID: " + instructor.getId() + "\n");
    }

    private static void alterInstructor() {
        System.out.println("\n--- ALTERAR DADOS DE INSTRUTOR ---");
        Instructor inst = findInstructor();
        if (inst != null) {
            System.out.println("Dados atuais: " + inst.getName() + " | " + inst.getEmail());
            System.out.print("Novo Email (Enter para manter): ");
            String email = scanner.nextLine().trim();
            System.out.print("Novo Telefone (Enter para manter): ");
            String phone = scanner.nextLine().trim();
            System.out.print("Nova Formação (Enter para manter): ");
            String education = scanner.nextLine().trim();

            if (!email.isEmpty()) inst.setEmail(email);
            if (!phone.isEmpty()) inst.setPhone(phone);
            if (!education.isEmpty()) inst.setEducation(education);

            System.out.println("\nDados alterados!\n");
        }
    }

    private static void removeInstructor() {
        System.out.println("\n--- EXCLUIR INSTRUTOR ---");
        Instructor inst = findInstructor();
        if (inst != null) {
            admin.removeInstructor(gym, inst);
            System.out.println("\nInstrutor removido!\n");
        }
    }

    private static void consultInstructor() {
        System.out.println("\n--- CONSULTAR INSTRUTOR ---");
        Instructor inst = findInstructor();
        if (inst != null) {
            System.out.println("\n=== DADOS DO INSTRUTOR ===");
            System.out.println("ID: " + inst.getId());
            System.out.println("Nome: " + inst.getName());
            System.out.println("Email: " + inst.getEmail());
            System.out.println("Telefone: " + inst.getPhone());
            System.out.println("Formação: " + inst.getEducation());
            System.out.println("Treinos Definidos: " + inst.getDefinedTrainings().size());
        }
    }

    private static Instructor findInstructor() {
        System.out.println("1. Buscar por Código (ID)");
        System.out.println("2. Buscar por Nome");
        System.out.print("Escolha: ");
        int choice = getIntInput();

        if (choice == 1) {
            System.out.print("ID: ");
            long id = Long.parseLong(scanner.nextLine());
            for (Instructor i : gym.getInstructors()) {
                if (i.getId() == id) return i;
            }
            System.out.println("\nInstrutor com ID " + id + " não encontrado.\n");
        } else if (choice == 2) {
            System.out.print("Nome: ");
            String name = scanner.nextLine();
            Instructor found = gym.findInstructorByName(name);
            if (found != null) return found;
            System.out.println("\nInstrutor '" + name + "' não encontrado.\n");
        }
        return null;
    }

    private static void registerMember() {
        System.out.println("\n--- CADASTRAR ALUNO ---");
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\nNome não pode ser vazio!\n");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("\nEmail não pode ser vazio!\n");
            return;
        }

        for (Member m : gym.getMembers()) {
            if (m.getEmail().equalsIgnoreCase(email)) {
                System.out.println("\nEmail já cadastrado!\n");
                return;
            }
        }

        System.out.print("Telefone: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("\nSenha não pode ser vazia!\n");
            return;
        }

        System.out.print("Data de Nascimento (dd/mm/aaaa): ");
        String birthDateStr = scanner.nextLine().trim();
        if (birthDateStr.isEmpty()) {
            System.out.println("\nData inválida!\n");
            return;
        }

        System.out.print("Altura (em metros): ");
        double height = getDoubleInput();
        if (height <= 0 || height > 3.0) {
            System.out.println("\nAltura inválida!\n");
            return;
        }

        try {
            Member member = new Member(name, email, phone, password);
            member.setBirthDate(LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            member.setHeight(height);
            admin.registerMember(gym, member);
            System.out.println("\nAluno cadastrado! ID: " + member.getId());
        } catch (DateTimeParseException e) {
            System.out.println("\nData inválida! Use dd/mm/aaaa\n");
        }
    }

    private static void alterMember() {
        System.out.println("\n--- ALTERAR DADOS DE ALUNO ---");
        Member mem = findMember();
        if (mem != null) {
            System.out.println("Dados atuais: " + mem.getName() + " | " + mem.getEmail());
            System.out.print("Novo Email (Enter para manter): ");
            String email = scanner.nextLine().trim();
            System.out.print("Novo Telefone (Enter para manter): ");
            String phone = scanner.nextLine().trim();

            mem.updateProfile(email.isEmpty() ? null : email, phone.isEmpty() ? null : phone);
            System.out.println("\nDados alterados!\n");
        }
    }

    private static void removeMember() {
        System.out.println("\n--- EXCLUIR ALUNO ---");
        Member mem = findMember();
        if (mem != null) {
            admin.removeMember(gym, mem);
            System.out.println("\nAluno removido!\n");
        }
    }

    private static void consultMember() {
        System.out.println("\n--- CONSULTAR ALUNO ---");
        Member mem = findMember();
        if (mem != null) {
            System.out.println("\n=== DADOS DO ALUNO ===");
            System.out.println("ID: " + mem.getId());
            System.out.println("Nome: " + mem.getName());
            System.out.println("Email: " + mem.getEmail());
            System.out.println("Telefone: " + mem.getPhone());
            if (mem.getBirthDate() != null) {
                System.out.println("Data Nascimento: " + mem.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                System.out.println("Idade: " + mem.getAge() + " anos");
            }
            System.out.println("Altura: " + mem.getHeight() + "m");
            System.out.println("Total de Presenças: " + mem.getTotalAttendances());
        }
    }

    private static Member findMember() {
        System.out.println("1. Buscar por Código (ID)");
        System.out.println("2. Buscar por Nome");
        System.out.print("Escolha: ");
        int choice = getIntInput();

        if (choice == 1) {
            System.out.print("ID: ");
            long id = Long.parseLong(scanner.nextLine());
            for (Member m : gym.getMembers()) {
                if (m.getId() == id) return m;
            }
            System.out.println("\nAluno com ID " + id + " não encontrado.\n");
        } else if (choice == 2) {
            System.out.print("Nome: ");
            String name = scanner.nextLine();
            Member found = gym.findMemberByName(name);
            if (found != null) return found;
            System.out.println("\nAluno '" + name + "' não encontrado.\n");
        }
        return null;
    }

    private static void registerEquipment() {
        System.out.println("\n--- CADASTRAR APARELHO ---");
        System.out.print("Nome: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("\nNome não pode ser vazio!\n");
            return;
        }

        System.out.print("Descrição: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            System.out.println("\nDescrição não pode ser vazia!\n");
            return;
        }

        System.out.print("Função: ");
        String function = scanner.nextLine().trim();
        if (function.isEmpty()) {
            System.out.println("\nFunção não pode ser vazia!\n");
            return;
        }

        Equipment equipment = new Equipment();
        equipment.setName(name);
        equipment.setDescription(description);
        equipment.setFunction(function);

        admin.registerEquipment(gym, equipment);
        System.out.println("\nAparelho cadastrado!\n");
    }

    private static void alterEquipment() {
        System.out.println("\n--- ALTERAR DADOS DE APARELHO ---");
        Equipment eq = findEquipment();
        if (eq != null) {
            System.out.println("Dados atuais: " + eq.getName());
            System.out.print("Nova Descrição (Enter para manter): ");
            String desc = scanner.nextLine().trim();
            System.out.print("Nova Função (Enter para manter): ");
            String func = scanner.nextLine().trim();

            if (!desc.isEmpty() || !func.isEmpty()) {
                eq.updateData(desc.isEmpty() ? null : desc, func.isEmpty() ? null : func);
                System.out.println("\nDados alterados!\n");
            }
        }
    }

    private static void removeEquipment() {
        System.out.println("\n--- EXCLUIR APARELHO ---");
        Equipment eq = findEquipment();
        if (eq != null) {
            admin.removeEquipment(gym, eq);
            System.out.println("\nAparelho removido!\n");
        }
    }

    private static void consultEquipment() {
        System.out.println("\n--- CONSULTAR APARELHO ---");
        Equipment eq = findEquipment();
        if (eq != null) {
            System.out.println("\n=== DADOS DO APARELHO ===");
            System.out.println("Nome: " + eq.getName());
            System.out.println("Descrição: " + eq.getDescription());
            System.out.println("Função: " + eq.getFunction());
        }
    }

    private static Equipment findEquipment() {
        System.out.print("Nome do Aparelho: ");
        String name = scanner.nextLine().trim();
        for (Equipment e : gym.getEquipments()) {
            if (e.getName().equalsIgnoreCase(name)) return e;
        }
        System.out.println("\nAparelho '" + name + "' não encontrado.\n");
        return null;
    }

    private static void viewGymInfo() {
        gym.viewGymInfo();
    }

    // ===== MENU INSTRUTOR =====
    private static void instructorMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===================================================");
            System.out.println("--- MENU INSTRUTOR - EXERCITEAKI ---");
            System.out.println("===================================================");
            System.out.println("Instrutor: " + currentInstructor.getName());
            System.out.println("---------------------------------------------------");
            System.out.println("1. Definir Treino para Aluno");
            System.out.println("2. Registrar Evolução de Aluno");
            System.out.println("3. Cadastrar Aluno");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("\nSelecione uma opção: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> defineTraining();
                case 2 -> registerProgressByInstructor();
                case 3 -> registerMember();
                case 0 -> back = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void defineTraining() {
        System.out.println("\n--- DEFINIR TREINO PARA ALUNO ---");
        System.out.print("Nome do Aluno: ");
        String memberName = scanner.nextLine();
        Member member = gym.findMemberByName(memberName);

        if (member == null) {
            System.out.println("\nAluno não encontrado.\n");
            return;
        }

        System.out.println("\nSelecione o Dia da Semana:");
        Weekday[] days = Weekday.values();
        for (int i = 0; i < days.length; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }
        System.out.print("Escolha (1-7): ");
        int dayChoice = getIntInput() - 1;

        if (dayChoice < 0 || dayChoice >= days.length) {
            System.out.println("\nDia inválido.\n");
            return;
        }

        Training training = new Training();
        training.setWeekDay(days[dayChoice]);

        boolean addingExercises = true;
        int exerciseOrder = 1;

        while (addingExercises) {
            System.out.println("\n--- Adicionar Exercício #" + exerciseOrder + " ---");

            if (gym.getEquipments().isEmpty()) {
                System.out.println("Nenhum aparelho cadastrado!");
                break;
            }

            System.out.println("Aparelhos Disponíveis:");
            List<Equipment> equipments = new ArrayList<>(gym.getEquipments());
            for (int i = 0; i < equipments.size(); i++) {
                System.out.println((i + 1) + ". " + equipments.get(i).getName());
            }
            System.out.print("Selecione (ou 0 para finalizar): ");
            int equipChoice = getIntInput();

            if (equipChoice == 0) break;

            if (equipChoice > 0 && equipChoice <= equipments.size()) {
                Equipment selectedEquipment = equipments.get(equipChoice - 1);

                System.out.print("Carga (kg): ");
                double weight = getDoubleInput();

                System.out.print("Repetições: ");
                int reps = getIntInput();

                Exercise exercise = new Exercise();
                exercise.setEquipment(selectedEquipment);
                exercise.setWeight(weight);
                exercise.setRepetitions(reps);
                exercise.setOrder(exerciseOrder);

                training.addExercise(exercise);
                System.out.println("Exercício adicionado!");

                exerciseOrder++;

                System.out.print("\nAdicionar outro? (s/n): ");
                String choice = scanner.nextLine();
                if (!choice.equalsIgnoreCase("s")) {
                    addingExercises = false;
                }
            } else {
                System.out.println("Seleção inválida.");
            }
        }

        if (training.getExercises().isEmpty()) {
            System.out.println("\nTreino precisa ter pelo menos um exercício.\n");
        } else {
            currentInstructor.defineTraining(member, training);
            System.out.println("\nTreino definido para " + days[dayChoice] + "!");
            System.out.println("Total: " + training.getExercises().size() + " exercício(s)\n");
        }
    }

    private static void registerProgressByInstructor() {
        System.out.println("\n--- REGISTRAR EVOLUÇÃO DO ALUNO ---");
        System.out.print("Nome do Aluno: ");
        String memberName = scanner.nextLine();
        Member member = gym.findMemberByName(memberName);

        if (member == null) {
            System.out.println("\nAluno não encontrado.\n");
            return;
        }

        System.out.print("Peso (kg): ");
        double weight = getDoubleInput();

        System.out.print("Percentual de Massa Muscular: ");
        double muscleMass = getDoubleInput();

        Progress progress = new Progress();
        progress.setDate(LocalDate.now());
        progress.setWeight(weight);
        progress.setMuscleMassPercent(muscleMass);

        currentInstructor.registerProgress(member, progress);
        System.out.println("\nEvolução registrada com sucesso!\n");
    }

    // MENU ALUNO
    private static void memberMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===================================================");
            System.out.println("--- MENU ALUNO - EXERCITEAKI ---");
            System.out.println("===================================================");
            System.out.println("Aluno: " + currentMember.getName());
            System.out.println("---------------------------------------------------");
            System.out.println("1. Consultar Meu Treino do Dia");
            System.out.println("2. Ver Minha Evolução");
            System.out.println("3. Registrar Entrada (Check-in)");
            System.out.println("4. Registrar Saída (Check-out)");
            System.out.println("5. Ver Meu Relatório de Frequência");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("\nSelecione uma opção: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 -> viewMyTraining();
                case 2 -> viewMyProgress();
                case 3 -> registerEntry();
                case 4 -> registerExit();
                case 5 -> viewMyAttendanceReport();
                case 0 -> back = true;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void viewMyTraining() {
        System.out.println("\n===================================================");
        System.out.println("\n--- CONSULTAR MEU TREINO ---");
        System.out.println("\n===================================================");
        System.out.println("Selecione o Dia:");
        Weekday[] days = Weekday.values();
        for (int i = 0; i < days.length; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }
        System.out.print("Escolha (1-7): ");
        int dayChoice = getIntInput() - 1;

        if (dayChoice >= 0 && dayChoice < days.length) {
            Training training = currentMember.viewTraining(days[dayChoice]);
            if (training != null) {
                System.out.println("--- TREINO DE " + days[dayChoice].toString().toUpperCase() + "  ---");
                System.out.println("Aluno: " + currentMember.getName());
                System.out.println("Total de Exercícios: " + training.getExercises().size());
                System.out.println("\n----------------------------------------------------------");

                for (Exercise ex : training.getExercises()) {
                    System.out.println("\n" + ex.getOrder() + ". " + ex.getEquipment().getName());
                    System.out.println("   Carga: " + ex.getWeight() + " kg");
                    System.out.println("   Repetições: " + ex.getRepetitions());
                    System.out.println("   Função: " + ex.getEquipment().getFunction());
                }
                System.out.println("\n----------------------------------------------------------");
            } else {
                System.out.println("\nNenhum treino agendado para este dia.\n");
            }
        } else {
            System.out.println("\nDia inválido.\n");
        }
    }

    private static void viewMyProgress() {
        System.out.println("\n===================================================");
        System.out.println("--- MINHA EVOLUÇÃO - EXERCITEAKI ---");
        System.out.println("===================================================");
        System.out.println("\nAluno: " + currentMember.getName());

        List<Progress> progressList = currentMember.viewProgress();

        if (progressList.isEmpty()) {
            System.out.println("\nNenhum registro de evolução ainda.");
            System.out.println("Dica: Peça ao seu instrutor para registrar sua evolução.\n");
        } else {
            System.out.println("\nTotal de Registros: " + progressList.size());
            System.out.println("\n----------------------------------------------------------");
            System.out.println("Data       | Peso (kg) | Massa Muscular (%)");
            System.out.println("----------------------------------------------------------");

            for (Progress p : progressList) {
                System.out.printf("%10s | %9.1f | %18.1f%n",
                        p.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        p.getWeight(),
                        p.getMuscleMassPercent()
                );
            }

            System.out.println("----------------------------------------------------------");
        }
    }

    private static void registerEntry() {
        System.out.println("\n===================================================");
        System.out.println("--- REGISTRAR ENTRADA (CHECK-IN) ---");
        System.out.println("===================================================");

        Attendance attendance = new Attendance();
        currentMember.registerEntry(attendance);

        System.out.println("\nEntrada registrada com sucesso!");
        System.out.println("Horário: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("\nBom treino, " + currentMember.getName() + "!");
    }

    private static void registerExit() {
        System.out.println("\n--- REGISTRAR SAÍDA (CHECK-OUT) ---");

        List<Attendance> attendances = currentMember.viewAttendances();
        Attendance lastAttendance = null;

        // Procura última entrada sem saída
        for (int i = attendances.size() - 1; i >= 0; i--) {
            Attendance a = attendances.get(i);
            if (a.getExitTime() == null) {
                lastAttendance = a;
                break;
            }
        }

        if (lastAttendance == null) {
            System.out.println("\nNenhuma entrada ativa encontrada!");
            System.out.println("Registre a entrada antes de registrar a saída.\n");
            return;
        }

        currentMember.registerExit(lastAttendance);

        System.out.println("\nSaída registrada com sucesso!");
        System.out.println("Horário: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        try {
            double hours = lastAttendance.calculateHours();
            System.out.println("Tempo na academia: " + String.format("%.2f", hours) + " horas");
        } catch (Exception e) {
            System.out.println("Erro ao calcular tempo.");
        }

        System.out.println("\nAté a próxima, " + currentMember.getName() + "!");
    }

    private static void viewMyAttendanceReport() {
        System.out.println("\n--- RELATÓRIO DE FREQUÊNCIA ---");
        System.out.println("1. Ver Últimas 10 Visitas");
        System.out.println("2. Ver Período Específico");
        System.out.print("Escolha: ");

        int choice = getIntInput();

        if (choice == 1) {
            generateLast10AttendancesReport();
        } else if (choice == 2) {
            generatePeriodAttendanceReport();
        } else {
            System.out.println("\nOpção inválida.\n");
        }
    }

    private static void generateLast10AttendancesReport() {
        System.out.println("\n===================================================");
        System.out.println("--- ÚLTIMAS VISITAS - EXERCITEAKI ---");
        System.out.println("===================================================");
        System.out.println("Aluno: " + currentMember.getName());

        List<Attendance> attendances = currentMember.viewAttendances();

        if (attendances.isEmpty()) {
            System.out.println("\nNenhum registro de frequência ainda.\n");
            return;
        }

        System.out.println("\n----------------------------------------------------------");
        System.out.println("Data       | Entrada  | Saída    | Horas");
        System.out.println("----------------------------------------------------------");

        int count = 0;
        for (int i = attendances.size() - 1; i >= 0 && count < 10; i--, count++) {
            Attendance a = attendances.get(i);

            String dateStr = a.getDate() != null ?
                    a.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";
            String entryStr = a.getEntryTime() != null ?
                    a.getEntryTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A";
            String exitStr = a.getExitTime() != null ?
                    a.getExitTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "---";

            String hoursStr;
            try {
                if (a.getExitTime() != null) {
                    hoursStr = String.format("%.2f", a.calculateHours());
                } else {
                    hoursStr = "Em andamento";
                }
            } catch (Exception e) {
                hoursStr = "Erro";
            }

            System.out.printf("%10s | %8s | %8s | %s%n", dateStr, entryStr, exitStr, hoursStr);
        }

        System.out.println("----------------------------------------------------------");
    }

    private static void generatePeriodAttendanceReport() {
        System.out.println("\n===================================================");
        System.out.println("\n--- RELATÓRIO POR PERÍODO ---");
        System.out.println("\n===================================================");
        System.out.print("Data Inicial (dd/mm/aaaa): ");
        String startDateStr = scanner.nextLine().trim();

        System.out.print("Data Final (dd/mm/aaaa): ");
        String endDateStr = scanner.nextLine().trim();

        try {
            LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (startDate.isAfter(endDate)) {
                System.out.println("\nData inicial não pode ser maior que data final!\n");
                return;
            }

            List<Attendance> allAttendances = currentMember.viewAttendances();
            List<Attendance> filteredAttendances = new ArrayList<>();
            double totalHours = 0;

            for (Attendance a : allAttendances) {
                if (a.getDate() != null &&
                        !a.getDate().isBefore(startDate) &&
                        !a.getDate().isAfter(endDate)) {
                    filteredAttendances.add(a);
                    try {
                        if (a.getExitTime() != null) {
                            totalHours += a.calculateHours();
                        }
                    } catch (Exception e) {
                        // vai ignora registros incompletos
                    }
                }
            }
            System.out.println("\n===================================================");
            System.out.println("--- RELATÓRIO DE FREQUÊNCIA - EXERCITEAKI ---");
            System.out.println("\n===================================================");
            System.out.println("Aluno: " + currentMember.getName());
            System.out.println("Período: " + startDateStr + " a " + endDateStr);
            System.out.println("\n----------------------------------------------------------");
            System.out.println("TOTAL DE COMPARECIMENTOS: " + filteredAttendances.size());
            System.out.println("TOTAL DE HORAS NO PERÍODO: " + String.format("%.2f", totalHours) + "h");

            if (!filteredAttendances.isEmpty()) {
                System.out.println("\n----------------------------------------------------------");
                System.out.println("Data       | Entrada  | Saída    | Horas");
                System.out.println("----------------------------------------------------------");

                for (Attendance a : filteredAttendances) {
                    String dateStr = a.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String entryStr = a.getEntryTime() != null ?
                            a.getEntryTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A";
                    String exitStr = a.getExitTime() != null ?
                            a.getExitTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "---";

                    String hoursStr;
                    try {
                        if (a.getExitTime() != null) {
                            hoursStr = String.format("%.2f", a.calculateHours());
                        } else {
                            hoursStr = "Incompleto";
                        }
                    } catch (Exception e) {
                        hoursStr = "Erro";
                    }

                    System.out.printf("%10s | %8s | %8s | %s%n", dateStr, entryStr, exitStr, hoursStr);
                }
            }

            System.out.println("----------------------------------------------------------");

        } catch (DateTimeParseException e) {
            System.out.println("\nFormato de data inválido! Use dd/mm/aaaa\n");
        }
    }
}
