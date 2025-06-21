import java.util.*;
class Task {
    private int id;
    private String title;
    private String description;
    private String deadline;
    private int priority;

    public Task(int id, String title, String description, String deadline, int priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDeadline() {
        return deadline;
    }

    public int getPriority() {
        return priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String toString() {
        return "Task ID: " + id + "\nTitle: " + title + "\nDescription: " + description +
               "\nDeadline: " + deadline + "\nPriority: " + priority + "\n";
    }
}


public class TaskService {
    private Map<Integer, Task> taskMap = new HashMap<>();
    private Stack<Map<Integer, Task>> undoStack = new Stack<>();
    private Stack<Map<Integer, Task>> redoStack = new Stack<>();
    private int nextId = 1;

    public void addTask(String title, String desc, String deadline, int priority) {
        saveStateForUndo();
        Task task = new Task(nextId++, title, desc, deadline, priority);
        taskMap.put(task.getId(), task);
        System.out.println("Task added.");
    }

    public void deleteTask(int id) {
        if (!taskMap.containsKey(id)) {
            System.out.println("Task not found.");
            return;
        }
        saveStateForUndo();
        taskMap.remove(id);
        System.out.println("Task deleted.");
    }

    public void editTask(int id, String title, String desc, String deadline, int priority) {
        if (!taskMap.containsKey(id)) {
            System.out.println("Task not found.");
            return;
        }
        saveStateForUndo();
        Task task = taskMap.get(id);
        task.setTitle(title);
        task.setDescription(desc);
        task.setDeadline(deadline);
        task.setPriority(priority);
        System.out.println("Task updated.");
    }

    public void showAllTasks() {
        if (taskMap.isEmpty()) {
            System.out.println("No tasks to show.");
            return;
        }
        for (Task task : taskMap.values()) {
            System.out.println(task);
        }
    }

    public void sortTasksByPriority() {
        taskMap.values().stream()
            .sorted(Comparator.comparingInt(Task::getPriority).reversed())
            .forEach(System.out::println);
    }

    public void sortTasksByDeadline() {
        taskMap.values().stream()
            .sorted(Comparator.comparing(Task::getDeadline))
            .forEach(System.out::println);
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(new HashMap<>(taskMap));
            taskMap = undoStack.pop();
            System.out.println("Undo successful.");
        } else {
            System.out.println("No actions to undo.");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(new HashMap<>(taskMap));
            taskMap = redoStack.pop();
            System.out.println("Redo successful.");
        } else {
            System.out.println("No actions to redo.");
        }
    }

    private void saveStateForUndo() {
        undoStack.push(new HashMap<>(taskMap));
        redoStack.clear();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaskService service = new TaskService();

        System.out.println("Welcome to Task Manager Pro");
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (!username.equals("admin") || !password.equals("admin")) {
            System.out.println("Invalid credentials.");
            return;
        }

        while (true) {
            System.out.println("\n1. Add Task");
            System.out.println("2. Edit Task");
            System.out.println("3. Delete Task");
            System.out.println("4. Show All Tasks");
            System.out.println("5. Sort by Priority");
            System.out.println("6. Sort by Deadline");
            System.out.println("7. Undo");
            System.out.println("8. Redo");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Title: ");
                    String title = sc.nextLine();
                    System.out.print("Description: ");
                    String desc = sc.nextLine();
                    System.out.print("Deadline (YYYY-MM-DD): ");
                    String deadline = sc.nextLine();
                    System.out.print("Priority (1-5): ");
                    int priority = sc.nextInt();
                    service.addTask(title, desc, deadline, priority);
                    break;
                case 2:
                    System.out.print("Task ID to edit: ");
                    int editId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("New Title: ");
                    String newTitle = sc.nextLine();
                    System.out.print("New Description: ");
                    String newDesc = sc.nextLine();
                    System.out.print("New Deadline: ");
                    String newDeadline = sc.nextLine();
                    System.out.print("New Priority: ");
                    int newPriority = sc.nextInt();
                    service.editTask(editId, newTitle, newDesc, newDeadline, newPriority);
                    break;
                case 3:
                    System.out.print("Task ID to delete: ");
                    int delId = sc.nextInt();
                    service.deleteTask(delId);
                    break;
                case 4:
                    service.showAllTasks();
                    break;
                case 5:
                    service.sortTasksByPriority();
                    break;
                case 6:
                    service.sortTasksByDeadline();
                    break;
                case 7:
                    service.undo();
                    break;
                case 8:
                    service.redo();
                    break;
                case 9:
                    System.out.println("Exiting Task Manager.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
