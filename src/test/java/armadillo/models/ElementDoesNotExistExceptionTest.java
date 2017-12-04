package armadillo.models;

import static org.junit.Assert.*;
import org.junit.Test;

public class ElementDoesNotExistExceptionTest {
    @Test
    public void testMessage() {
        try {
            throw new ElementDoesNotExistException("test_table", 0);
        } catch (ElementDoesNotExistException e) {
            assertEquals("test_table does not contain element ID 0",e.getMessage());
        }
    }

    @Test
    public void testGetID() {
        try {
            throw new ElementDoesNotExistException("test_table", 0);
        } catch (ElementDoesNotExistException e) {
            assertEquals(0, e.getId());
        }
    }

    @Test
    public void testGetTable() {
        try {
            throw new ElementDoesNotExistException("test_table", 0);
        } catch (ElementDoesNotExistException e) {
            assertEquals("test_table", e.getTable());
        }
    }

    @Test
    public void testWhenTableIsNull() {
        try {
            throw new ElementDoesNotExistException(null, 0);
        } catch (ElementDoesNotExistException e) {
            assertEquals(null, e.getTable());
            assertEquals("null does not contain element ID 0", e.getMessage());
        }
    }
}
