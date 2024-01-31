package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int generateId() {
        return id++;
    }

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    private Status calculateEpicStatus(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;
        boolean isInProgress = true;
        for (SubTask subTask : epic.getSubTasks()) {
            if (subTask.getStatus() == Status.IN_PROGRESS || subTask.getStatus() == Status.DONE) {
                isNew = false;
            }
            if (subTask.getStatus() == Status.IN_PROGRESS || subTask.getStatus() == Status.NEW) {
                isDone = false;
            }
        }
        if (isNew) {
            return Status.NEW; // Если список пустой, то все булевы значения true и вернется первый вариант из if-else
            // цикла - NEW, как и должно быть по заданию
        } else if (isDone) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTask.getEpic().getSubTasks().add(subTask);
        subTasks.put(subTask.getId(), subTask);
        tasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        Status epicStatus = calculateEpicStatus(epic);
        epics.put(epic.getId(), epic);
        tasks.put(epic.getId(), epic);
        return epic;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getAllSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Task getById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }
    public Task update(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        epic.setStatus(calculateEpicStatus(saved));
    }

    public void updateSubTak (SubTask subTask) {
        Epic epic = subTask.getEpic();
        Epic savedEpic = epics.get(epic.getId());
        savedEpic.setStatus(calculateEpicStatus(savedEpic));
    }

    public void deleteById(int id) {
        tasks.remove(id);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (SubTask subTask : epic.getSubTasks()) {
            subTasks.remove(subTask.getId());
            tasks.remove(subTask.getId());
        }
        epic.getSubTasks().clear();
        epics.remove(id);
        tasks.remove(id);
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    public void deleteSubtaskById(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        Epic epic = removeSubTask.getEpic();
        epic.getSubTasks().remove(id);
        calculateEpicStatus(epic);
        tasks.remove(id);
    }
}
