package model;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status Status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(Status, task.Status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, Status);
    }

    public Task() {

    }

    public Task(String name, String description, Status Status) {
        this.name = name;
        this.description = description;
        this.Status = Status;
    }

    public TaskType getType() {
        return TaskType.TASK;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return Status;
    }

    public void setStatus(Status Status) {
        this.Status = Status;
    }

    public Integer getEpicId() {
        return null;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", Status=" + Status +
                '}';
    }
}
