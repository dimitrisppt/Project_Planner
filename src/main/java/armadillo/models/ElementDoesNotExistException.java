package armadillo.models;

/**
 * This is an Exception that should be thrown when the element trying to be accessed no longer exists in the database
 */
public class ElementDoesNotExistException extends Exception {
    /**
     * The ID of the element in the table
     */
    private int id;
    /**
     * The name of the table in the database
     */
    private String table;

    /**
     * Creates a new ElementDoesNotExistException
     * @param table the table name
     * @param id the id of the element in the table
     */
    public ElementDoesNotExistException(String table, int id) {
        super(String.format("%s does not contain element ID %d", table, id));
        this.id = id;
        this.table = table;
    }

    /**
     * Gets the ID of the element in the table
     * @return the ID of the element
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the table in the database
     * @return the name of the table
     */
    public String getTable() {
        return table;
    }
}
