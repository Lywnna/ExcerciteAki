package model;

public class User {
    private static long nextId = 1;
    private long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private boolean loggedIn = false;

    public User(String name, String email, String phone, String password) {
        this.id = nextId++;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public User(long id, String name, String email, String phone, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;

        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public void login(String enteredPassword) {
        if (this.password.equals(enteredPassword)) {
            loggedIn = true;
            System.out.println("Login successful!");
        } else {
            System.out.println("Incorrect password.");
        }
    }

    public void logout() {
        loggedIn = false;
        System.out.println("Logout successful.");
    }

    public void updateProfile(String newEmail, String newPhone) {
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            this.email = newEmail;
        }
        if (newPhone != null && !newPhone.trim().isEmpty()) {
            this.phone = newPhone;
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
