package sakura.iri2.model;

import java.util.UUID;

public class CourseModel {

    private UUID id;
    private String name;
    private String description;
    private boolean isDeleted;

    public CourseModel(String name, String description) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}