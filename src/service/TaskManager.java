package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    int getId();

    void setId(int id);

    Task createTask(Task task);

    SubTask createSubTask(SubTask subTask);

    Epic createEpic(Epic epic);

    List<Task> getAllTasks();

    List<SubTask> getAllSubtasks();

    List<Epic> getAllEpics();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    Task updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTaskById(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteEpicById(int id);

    void deleteAllSubTasks();

    void deleteSubtaskById(int id);

    List<SubTask> getAllSubTasksByEpic(Epic epic);

}
