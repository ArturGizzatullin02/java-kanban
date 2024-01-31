package model;

public class SubTask extends Task {
    private Epic epic;
    public SubTask(String name, String description, Status Status, Epic epic) {
        super(name, description, Status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
