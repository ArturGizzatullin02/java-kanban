package model;

import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask() {
    }

    public SubTask(String name, String description, Status Status, int epicId) {
        super(name, description, Status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpic(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubTask task = (SubTask) o;

        return Objects.equals(getName(), task.getName()) && Objects.equals(getDescription(), task.getDescription())
                && Objects.equals(getStatus(), task.getStatus()) && Objects.equals(getEpicId(), task.getEpicId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getStatus(), getEpicId());
    }
}
