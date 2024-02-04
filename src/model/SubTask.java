package model;

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
}
