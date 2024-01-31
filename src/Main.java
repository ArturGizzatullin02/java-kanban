import model.Epic;
import model.SubTask;
import model.Task;
import model.Status;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = taskManager.create(new Task("Убраться в комнате", "Пропылесосить + влажная уборка",
                Status.NEW));
        Task task2 = taskManager.create(new Task("Потренироваться", "Поехать в спортзал и провести " +
                "тренировку верха тела", Status.NEW));
        Epic epic1 = taskManager.createEpic(new Epic("Пройти 1 модуль Практикума", "Пройти всю теорию" +
                "и сдать все проекты", Status.NEW));
        Epic epic2 = taskManager.createEpic(new Epic("Закрыть семестр в ВУЗе", "закрыть 1 оставшийся " +
                "предмет", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("Пройти 4 спринт", "Пройти теорию и " +
                "сдать 4 проект", Status.NEW, epic1));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("Пройти 5 спринт", "Пройти теорию и " +
                "сдать 5 проект", Status.NEW, epic1));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("Закрыть вычислительную математику",
                "Подготовиться и сдать экзамен", Status.NEW, epic2));
        System.out.println(taskManager.getAllTasks());
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(epic1);
        System.out.println(taskManager.getAllTasks());
        taskManager.deleteById(task1.getId());
        taskManager.deleteEpicById(epic2.getId());
        System.out.println(taskManager.getAllTasks());

    }
}
