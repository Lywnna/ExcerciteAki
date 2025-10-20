package model;

public class Equipment {
    private String name;
    private String description;
    private String function;

    public void updateData(String newDescription, String newFunction) {
        this.description = newDescription;
        this.function = newFunction;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFunction() { return function; }
    public void setFunction(String function) { this.function = function; }
}

