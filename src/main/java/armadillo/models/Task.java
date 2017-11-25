package armadillo.models;

public class Task {
    private int id;

    private Task(int id) {
        this.id = id;
    }

    public static Task getTaskByID(int id) {
        return new Task(id);
    }
}
