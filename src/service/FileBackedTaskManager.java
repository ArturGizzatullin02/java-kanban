package service;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileBackedTaskManager extends InMemoryTaskManager {
    public static final String TASK_CSV = "rsc/tasks.csv";
    File file;

    public FileBackedTaskManager(String filePath) {
        this(Managers.getDefaultHistoryManager(), filePath);
    }

    public FileBackedTaskManager(HistoryManager historyManager, String filePath) {
        this(historyManager, new File(filePath));
    }

    public FileBackedTaskManager(File file) {
        this(Managers.getDefaultHistoryManager(), file);
    }

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                writer.write(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                writer.write(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                writer.write(toString(entry.getValue()));
                writer.newLine();
            }
            writer.newLine();
            writer.write(historyToString(historyManager));
            writer.newLine();

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл.", file);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.loadFromFile();
        return fileBackedTaskManager;
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + "," + task.getEpicId();
    }

    private Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[FileFields.ID.ordinal()]);
        TaskType taskType = TaskType.valueOf(fields[FileFields.TASK_TYPE.ordinal()]);
        String name = fields[FileFields.NAME.ordinal()];
        Status status = Status.valueOf(fields[FileFields.STATUS.ordinal()]);
        String description = fields[FileFields.DESCRIPTION.ordinal()];
        int epicId;
        if (!fields[FileFields.EPIC_ID.ordinal()].equals("null")) {
            epicId = Integer.parseInt(fields[FileFields.EPIC_ID.ordinal()]);
        } else {
            epicId = -1;
        }
        switch (taskType) {
            case TASK:
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            case SUBTASK:
                SubTask subTask = new SubTask(name, description, status, epicId);
                subTask.setId(id);
                return subTask;
            case EPIC:
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                return epic;
            default:
                return null;
        }
    }

    static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Integer id : manager.getHistory()) {
            sb.append(id).append(", ");
        }
        return sb.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> ids = new ArrayList<>();
        for (String id : value.split(", ")) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    private void loadFromFile() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            br.readLine();
            while (true) {
                String string = br.readLine();
                if (string == null || string.isEmpty()) {
                    break;
                }
                Task task = fromString(string);
                int idFromFile = task.getId();
                switch (task.getType()) {
                    case TASK:
                        task.setId(idFromFile);
                        tasks.put(task.getId(), task);
                        break;
                    case SUBTASK:
                        task.setId(idFromFile);
                        subTasks.put(task.getId(), (SubTask) task);
                        break;
                    case EPIC:
                        task.setId(idFromFile);
                        task.setStatus(Status.NEW);
                        epics.put(task.getId(), (Epic) task);
                        break;
                }
                if (idFromFile > maxId) {
                    maxId = idFromFile;
                }
            }
            for (SubTask subTask : subTasks.values()) {
                epics.get(subTask.getEpicId()).getSubTasksId().add(subTask.getId());
            }
            br.readLine();
            String string = br.readLine();
            if (!historyManager.getHistory().isEmpty()) {
                List<Integer> idsFromHistory = historyFromString(string);
                for (Integer id : idsFromHistory) {
                    Task task = tasks.get(id);
                    SubTask subTask = subTasks.get(id);
                    Epic epic = epics.get(id);
                    if (task != null) {
                        historyManager.add(task);
                    } else if (subTask != null) {
                        historyManager.add(subTask);
                    } else if (epic != null) {
                        historyManager.add(epic);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.id = maxId;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        save();
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask newSubTask = super.createSubTask(subTask);
        save();
        return newSubTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Task updateTask(Task task) {
        Task newTask = super.updateTask(task);
        save();
        return newTask;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = super.getAllTasks();
        save();
        return tasks;
    }

    @Override
    public List<SubTask> getAllSubtasks() {
        List<SubTask> subTasks = super.getAllSubtasks();
        save();
        return subTasks;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> epics = super.getAllEpics();
        save();
        return epics;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public List<SubTask> getAllSubTasksByEpic(Epic epic) {
        List<SubTask> subTasksByEpic = super.getAllSubTasksByEpic(epic);
        save();
        return subTasksByEpic;
    }
}
