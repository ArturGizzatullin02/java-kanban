package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Integer> subTasksId = new ArrayList<>();

    public Epic() {
        super();
    }

    public Epic(String name, String description, Status Status) {
        super(name, description, Status);
        setDuration(Duration.ofMinutes(0));
        setStartTime(LocalDateTime.now());
        setEndTime(getStartTime().plus(getDuration()));
    }


    public TaskType getType() {
        return TaskType.EPIC;
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(List<Integer> subTasksId) {
        this.subTasksId = subTasksId;
    }

    public void setSubTask(SubTask subTask) {
        subTasksId.add(subTask.getId());
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
