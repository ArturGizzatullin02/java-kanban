package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager;

    int id = 0;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    private int generateId() {
        return id++;
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private Status calculateEpicStatus(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;
        List<SubTask> subTasksOfEpic = new ArrayList<>();
        for (Integer id : epic.getSubTasksId()) {
            subTasksOfEpic.add(subTasks.get(id));
        }
        for (SubTask subTask : subTasksOfEpic) {
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

    @Override
    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        epics.get(subTask.getEpicId()).getSubTasksId().add(subTask.getId());
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epic.setStatus(Status.NEW);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getById(int id) {
        Task task = tasks.get(id);
        historyManager.addToHistory(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.addToHistory(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.addToHistory(subTask);
        return subTask;
    }
    @Override
    public Task update(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        epic.setStatus(calculateEpicStatus(saved));
    }

    @Override
    public void updateSubTak(SubTask subTask) {
        Epic savedEpic = epics.get(subTask.getEpicId());
        savedEpic.setStatus(calculateEpicStatus(savedEpic));
    }

    @Override
    public void deleteById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer subTaskId : epic.getSubTasksId()) {
            subTasks.remove(subTaskId);
        }
        epic.getSubTasksId().clear();
        epics.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    @Override
    public void deleteSubtaskById(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        Epic epic = epics.get(removeSubTask.getEpicId());
        epic.getSubTasksId().remove(id);
        calculateEpicStatus(epic);
    }

    @Override
    public List<SubTask> getAllSubTasksByEpic(Epic epic) {
        ArrayList<SubTask> subTasksOfEpic = new ArrayList<>();
        for (Integer id : epic.getSubTasksId()) {
            subTasksOfEpic.add(subTasks.get(id));
        }
        return subTasksOfEpic;
    }


}
