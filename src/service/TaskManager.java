package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int getId();

    void setId(int id);

    Task create(Task task);

    SubTask createSubTask(SubTask subTask);

    Epic createEpic(Epic epic);

    List<Task> getAllTasks();

    List<SubTask> getAllSubtasks();

    List<Epic> getAllEpics();

    Task getById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    Task update(Task task);

    void updateEpic(Epic epic);

    void updateSubTak(SubTask subTask);

    void deleteById(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteEpicById(int id);

    void deleteAllSubTasks();

    void deleteSubtaskById(int id);

    List<SubTask> getAllSubTasksByEpic(Epic epic);

}
