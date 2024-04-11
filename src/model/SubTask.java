package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Status Status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, Status, duration, startTime);
        this.epicId = epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public Integer getEpicId() {
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
