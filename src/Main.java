import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.FileBackedTaskManager;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        TaskManager taskManager = Managers.getDefaultTaskManager(historyManager);
        Task task1 = taskManager.createTask(new Task("task1", "description",
                Status.NEW));
        Task task2 = taskManager.createTask(new Task("task2", "description", Status.NEW));
        Epic epic1 = taskManager.createEpic(new Epic("epic1", "description", Status.NEW));
        Epic epic2 = taskManager.createEpic(new Epic("epic2", "description", Status.NEW));
        Epic epic3 = taskManager.createEpic(new Epic("epic3", "description", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("subTask1ForEpic1", "description",
                Status.NEW, epic1.getId()));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("subTask2ForEpic1", "description",
                Status.NEW, epic1.getId()));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("subTask3ForEpic1",
                "description", Status.NEW, epic1.getId()));
        System.out.println();
        System.out.println("Задача с id 1:");
        System.out.println(taskManager.getTaskById(1));
        System.out.println();
        System.out.println("История:");
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println("Сабтаск с id 5:");
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println();
        System.out.println("История:");
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println("Эпик с id 2:");
        System.out.println(taskManager.getEpicById(2));
        System.out.println();
        System.out.println("История:");
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println("Эпик с id 4:");
        System.out.println(taskManager.getEpicById(4));
        System.out.println();
        System.out.println("История:");
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println("Задача с id 1:");
        System.out.println(taskManager.getTaskById(1));
        System.out.println();
        System.out.println("История:");
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println("Эпик с id 2:");
        System.out.println(taskManager.getEpicById(2));
        System.out.println();
        System.out.println("История:");
        System.out.println(historyManager.getHistory());
        taskManager.deleteEpicById(4);
        System.out.println();
        System.out.println("История после удаления эпика с id 4:");
        System.out.println(historyManager.getHistory());
        taskManager.deleteEpicById(2);
        System.out.println();
        System.out.println("История после удаления эпика с id 2(Имеет подзадачи с id 5, 6, 7):");
        System.out.println(historyManager.getHistory());

        Task task = taskManager.createTask(new Task("Новая задача", "описание", Status.NEW));
        System.out.println("Create task: " + task);

        Task taskFromManager = taskManager.getTaskById(task.getId());
        System.out.println("Get task: " + taskFromManager);

        taskFromManager.setName("New name");
        taskManager.updateTask(taskFromManager);
        System.out.println("Update task: " + taskFromManager);

        taskManager.deleteTaskById(taskFromManager.getId());
        System.out.println("Delete: " + task);

        try {
            File testFile = File.createTempFile("temp", ".csv");
            TaskManager taskManagerReload = FileBackedTaskManager.loadFromFile(testFile);
            System.out.println("Все задачи тестового менеджера: " + taskManagerReload.getAllTasks());
            taskManagerReload.createTask(new Task("testTask1", "description", Status.NEW));
            taskManagerReload.createTask(new Task("testTask2", "description", Status.NEW));
            taskManagerReload.createTask(new Task("testTask3", "description", Status.NEW));
            System.out.println("Все задачи тестового менеджера: " + taskManagerReload.getAllTasks());
            TaskManager taskManagerReloadFromTestFile = FileBackedTaskManager.loadFromFile(testFile);
            System.out.println("Все задачи нового тестового менеджера, загруженные из тестового файла: "
                    + taskManagerReloadFromTestFile.getAllTasks());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
