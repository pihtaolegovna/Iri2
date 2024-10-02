package sakura.iri2.model;

import java.util.UUID;

public class StudentModel {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private boolean isDeleted;

    public StudentModel(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.isDeleted = false;
    }

    // Геттеры и сеттеры
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}