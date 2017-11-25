package armadillo.models;

public class ElementDoesNotExistException extends Exception {
    private int id;
    private String table;

    public ElementDoesNotExistException(String table, int id) {
        super(String.format("%s does not contain element ID %d", table, id));
        this.id = id;
        this.table = table;
    }

    public int getId() {
        return id;
    }

    public String getTable() {
        return table;
    }
}
