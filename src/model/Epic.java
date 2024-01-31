package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;
    public Epic(String name, String description, Status Status) {
        super(name, description, Status);
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void getAllSubtasksByEpic () {
        for (SubTask subTask : subTasks) {
            System.out.println(subTask);
        }
    }
}
