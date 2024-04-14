package service;

import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, SubTask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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
        List<SubTask> subTasksOfEpic = epic.getSubTasksId().stream()
                .map(subTasks::get)
                .toList();
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

    public void calculateEpicTime(Epic epic) {
        LocalDateTime endTime = epic.getEndTime();
        LocalDateTime startTime = LocalDateTime.MAX;
        epic.getSubTasksId().forEach((id) -> {
            LocalDateTime subTaskStartTime = getSubTaskById(id).getStartTime();
            LocalDateTime subTaskEndTime = getSubTaskById(id).getEndTime();
            if (subTaskStartTime.isBefore(startTime)) {
                epic.setStartTime(subTaskStartTime);
            }
            if (subTaskEndTime.isAfter(endTime)) {
                epic.setEndTime(subTaskEndTime);
            }
        });
        epic.setDuration(Duration.between(epic.getStartTime(), endTime));
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        getPrioritizedTasks().stream()
                .filter(taskForValidate -> taskForValidate.getId() != task.getId())
                .forEach(taskForValidate -> isTasksCrossing(task, taskForValidate));
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        Epic epic = epics.get(subTask.getEpicId());
        epic.setSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
        calculateEpicTime(epic);
        epic.setStatus(calculateEpicStatus(epic));
        getPrioritizedTasks().forEach((taskForValidate) -> {
            if (subTask.getId() != taskForValidate.getId()) {
                isTasksCrossing(subTask, taskForValidate);
            }
        });
        prioritizedTasks.add(subTask);
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
        if (task == null) {
            throw new NotFoundException("Задача с id: " + id + " не найдена");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Задача с id: " + id + " не найдена");
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            throw new NotFoundException("Задача с id: " + id + " не найдена");
        }
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Task updateTask(Task task) {
//        if (tasks.containsV) {
//            throw new NotFoundException("Задача не существует");
//        }
        prioritizedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            throw new NotFoundException("Задача не существует");
        }
        Epic saved = epics.get(epic.getId());
        epic.setStatus(calculateEpicStatus(saved));
        calculateEpicTime(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Epic savedEpic = epics.get(subTask.getEpicId());
        savedEpic.setStatus(calculateEpicStatus(savedEpic));
        calculateEpicTime(epics.get(subTask.getEpicId()));
    }

    @Override
    public void deleteTaskById(int id) {
        prioritizedTasks.remove(tasks.get(id));
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача с id: " + id + " не найдена");
        }
        tasks.remove(id);
        if (!historyManager.getHistory().isEmpty()) {
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach((id) -> {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        });
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach((id) -> {
            for (SubTask subTask : getAllSubTasksByEpic(epics.get(id))) {
                historyManager.remove(subTask.getId());
                prioritizedTasks.remove(subTasks.get(subTask.getId()));
                subTasks.remove(subTask.getId());
            }
            historyManager.remove(id);
        });
        epics.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        epic.getSubTasksId().forEach((subTaskId) -> {
            prioritizedTasks.remove(subTasks.get(subTaskId));
            subTasks.remove(subTaskId);
            if (historyManager.getHistory().contains(subTaskId)) {
                historyManager.remove(subTaskId);
            }
        });
        epic.getSubTasksId().clear();
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.keySet().forEach((id) -> {
            historyManager.remove(id);
            calculateEpicTime(epics.get(subTasks.get(id).getEpicId()));
        });
        prioritizedTasks.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubtaskById(int id) {
        prioritizedTasks.remove(subTasks.get(id));
        SubTask removeSubTask = subTasks.remove(id);
        Epic epic = epics.get(removeSubTask.getEpicId());
        epic.getSubTasksId().remove(id - 1);
        epic.setStatus(calculateEpicStatus(epic));
        calculateEpicTime(epic);
        historyManager.remove(id);
    }

    @Override
    public List<SubTask> getAllSubTasksByEpic(Epic epic) {
        return epic.getSubTasksId().stream()
                .map(subTasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private void isTasksCrossing(Task task1, Task task2) {
        LocalDateTime task1StartTime = task1.getStartTime();
        LocalDateTime task1EndTime = task1.getEndTime();
        LocalDateTime task2StartTime = task2.getStartTime();
        LocalDateTime task2EndTime = task2.getEndTime();
        if (task1EndTime.isAfter(task2StartTime) && task1StartTime.isBefore(task2StartTime)) {
            throw new ValidationException("Задачи с id [" + task1.getId() + "] , [" + task2.getId() + "] пересекаются во времени");
        } else if (task1EndTime.isAfter(task2EndTime) && task1StartTime.isBefore(task2EndTime)) {
            throw new ValidationException("Задачи с id [" + task1.getId() + "] , [" + task2.getId() + "] пересекаются во времени");
        } else if (task1EndTime.isBefore(task2EndTime) && task1StartTime.isAfter(task2StartTime)) {
            throw new ValidationException("Задачи с id [" + task1.getId() + "] , [" + task2.getId() + "] пересекаются во времени");
        } else if (task2EndTime.isBefore(task1EndTime) && task2StartTime.isAfter(task1StartTime)) {
            throw new ValidationException("Задачи с id [" + task1.getId() + "] , [" + task2.getId() + "] пересекаются во времени");
        }
    }
}
