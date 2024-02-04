package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksId;

    public Epic() {
    }

    public Epic(String name, String description, Status Status) {
        super(name, description, Status);
        subTasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void setSubTasksId(ArrayList<Integer> subTasksId) {
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
