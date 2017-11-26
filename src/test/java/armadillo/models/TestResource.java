package armadillo.models;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.SQLException;
import java.util.TreeSet;

public class TestResource {
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenNameIsTooLong() throws SQLException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= 255; i++) {
            sb.append('a');
        }
        Resource rs = new Resource(sb.toString());
    }

    @Test
    public void testResourceCreationAndDeletionAndGetNameAndGetIDAndExists() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs = new Resource("logs");
        assertEquals(rs.getName(), "logs");
        assertTrue(rs.exists());
        int id = rs.getId();
        assertTrue(Resource.exists(id));
        Resource.deleteResource(id);
        assertFalse(rs.exists());
        assertFalse(Resource.exists(id));
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetNameThrowsElementDoesNotExistExceptionCorrectly() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs = new Resource("logs");
        Resource.deleteResource(rs.getId());
        rs.getName();
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testGetIDThrowsElementDoesNotExistExceptionCorrectly() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs = new Resource("logs");
        Resource.deleteResource(rs.getId());
        rs.getId();
    }

    @Test
    public void testSetName() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs = new Resource("logs");
        rs.setName("trees");
        assertEquals("trees", rs.getName());
        Resource.deleteResource(rs.getId());
    }

    @Test(expected = ElementDoesNotExistException.class)
    public void testSetNameThrowsElementDoesNotExistExceptionCorrectly() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs = new Resource("logs");
        Resource.deleteResource(rs.getId());
        rs.setName("trees");
    }

    @Test
    public void testGetResourceByIDAndEqualsWhenEqualID() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs1 = new Resource("logs");
        Resource rs2 = Resource.getResourceByID(rs1.getId());
        assertTrue(rs1.equals(rs2));
        Resource.deleteResource(rs1.getId());
    }

    @Test
    public void testEqualsWhenOneIsNull() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs = new Resource("dogs");
        assertFalse(rs.equals(null));
        Resource.deleteResource(rs.getId());
    }

    @Test
    public void testEqualsWhenOneIsAnotherClass() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs = new Resource("dogs");
        assertFalse(rs.equals("Hello"));
        Resource.deleteResource(rs.getId());
    }

    @Test
    public void testEqualsWhenTheyAreTheSame() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs = new Resource("dogs");
        assertTrue(rs == rs);
        assertTrue(rs.equals(rs));
        Resource.deleteResource(rs.getId());
    }

    @Test
    public void testEqualsWhenTheyAreDifferent() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs1 = new Resource("dogs");
        Resource rs2 = new Resource("logs");
        assertFalse(rs1.equals(rs2));
        Resource.deleteResource(rs1.getId());
        Resource.deleteResource(rs2.getId());
    }

    @Test
    public void testHashCode() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs = new Resource("dogs");
        assertEquals(rs.getId(), rs.hashCode());
        Resource.deleteResource(rs.getId());
    }

    @Test
    public void testCompareTo() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        Resource rs1 = new Resource("dogs");
        Resource rs2 = new Resource("logs");
        assertEquals(-1, rs1.compareTo(rs2));
        assertEquals(1, rs2.compareTo(rs1));
        Resource.deleteResource(rs1.getId());
        Resource.deleteResource(rs2.getId());
    }

    @Test
    public void testGetAllResources() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        TreeSet<Resource> resources = Resource.getAllResources();
        resources.add(new Resource("dogs"));
        resources.add(new Resource("logs"));
        resources.add(new Resource("cats"));
        assertEquals(resources, Resource.getAllResources());
        for (Resource resource : resources) {
            Resource.deleteResource(resource.getId());
        }
    }
}
