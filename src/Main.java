import model.Epic;
import model.SubTask;
import model.Task;
import model.Status;
import service.*;

public class Main {

    public static void main(String[] args) {
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        TaskManager taskManager = Managers.getDefaultTaskManager(historyManager);
        Task task1 = taskManager.create(new Task("Убраться в комнате", "Пропылесосить + влажная уборка",
                Status.NEW));
        Task task2 = taskManager.create(new Task("Потренироваться", "Поехать в спортзал и провести " +
                "тренировку верха тела", Status.NEW));
        Epic epic1 = taskManager.createEpic(new Epic("Пройти 1 модуль Практикума", "Пройти всю теорию" +
                "и сдать все проекты", Status.NEW));
        Epic epic2 = taskManager.createEpic(new Epic("Закрыть семестр в ВУЗе", "закрыть 1 оставшийся " +
                "предмет", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("Пройти 4 спринт", "Пройти теорию и " +
                "сдать 4 проект", Status.NEW, epic1.getId()));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("Пройти 5 спринт", "Пройти теорию и " +
                "сдать 5 проект", Status.NEW, epic1.getId()));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("Закрыть вычислительную математику",
                "Подготовиться и сдать экзамен", Status.NEW, epic2.getId()));
        System.out.println("Вывод всех задач после создания: " + taskManager.getAllTasks() + " " +
                taskManager.getAllSubtasks() + " " + taskManager.getAllEpics());
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(epic1);
        System.out.println("Вывод всех задач после обновления: " + " " + taskManager.getAllTasks() +
                " " + taskManager.getAllSubtasks() + " " + taskManager.getAllEpics());
        taskManager.deleteById(task1.getId());
        taskManager.deleteEpicById(epic2.getId());
        System.out.println("Вывод всех задач после удаления: " + taskManager.getAllTasks() + " " +
                taskManager.getAllSubtasks() + " " + taskManager.getAllEpics());
        System.out.println();
        System.out.println("Задача с id 1:");
        System.out.println(taskManager.getById(1));
        System.out.println();
        System.out.println("История:");
        System.out.println(historyManager.getHistory());
        System.out.println();
        System.out.println("Эпик с id 2:");
        System.out.println(taskManager.getEpicById(2));
        System.out.println();
        System.out.println("История:");
        historyManager.getHistory();
        System.out.println();
        System.out.println("Сабтаск с id 5:");
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println();
        System.out.println("История:");
        historyManager.getHistory();
        Epic epic1ww = new Epic("Task1", "Description1", Status.NEW);
        Epic epic2ww = new Epic("Task2", "Description2", Status.NEW);
    }
}
