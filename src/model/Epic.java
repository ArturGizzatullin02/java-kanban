package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTasksId;

    public Epic() {
    }

    public Epic(String name, String description, Status Status) {
        super(name, description, Status);
        subTasksId = new ArrayList<>();
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(List<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Epic task = (Epic) o;

        return Objects.equals(getName(), task.getName()) && Objects.equals(getDescription(), task.getDescription())
                && Objects.equals(getStatus(), task.getStatus()) && Objects.equals(getSubTasksId(), task.getSubTasksId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getStatus(), getSubTasksId());
    }

    @Override
    public String toString() {
        String output = super.toString();
        return output + "Epic{" +
                "subTasksId=" + subTasksId +
                '}';
    }
}
