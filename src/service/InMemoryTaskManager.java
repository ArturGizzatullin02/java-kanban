package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager historyManager;

    int id = 0;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    protected int generateId() {
        return id++;
    }

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistoryManager();
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
            return Status.NEW;
        } else if (isDone) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        List<Integer> subTasksId = epics.get(subTask.getEpicId()).getSubTasksId();
        subTasksId.add(subTask.getId());
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
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        epic.setStatus(calculateEpicStatus(saved));
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Epic savedEpic = epics.get(subTask.getEpicId());
        savedEpic.setStatus(calculateEpicStatus(savedEpic));
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        if (!historyManager.getHistory().isEmpty()) {
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            for (SubTask subTask : getAllSubTasksByEpic(epics.get(id))) {
                historyManager.remove(subTask.getId());
                subTasks.remove(subTask.getId());
            }
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer subTaskId : epic.getSubTasksId()) {
            subTasks.remove(subTaskId);
            if (historyManager.getHistory().contains(subTaskId)) {
                historyManager.remove(subTaskId);
            }
        }
        epic.getSubTasksId().clear();
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        subTasks.clear();
    }

    @Override
    public void deleteSubtaskById(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        Epic epic = epics.get(removeSubTask.getEpicId());
        epic.getSubTasksId().remove(id);
        calculateEpicStatus(epic);
        historyManager.remove(id);
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
