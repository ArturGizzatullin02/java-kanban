package model;

import java.util.ArrayList;
import java.util.List;

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
    public String toString() {
        String output = super.toString();
        return output + "Epic{" +
                "subTasksId=" + subTasksId +
                '}';
    }
}
