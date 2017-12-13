package armadillo.controllers;

import static org.junit.Assert.*;

import armadillo.models.Database;
import armadillo.models.ElementDoesNotExistException;
import armadillo.models.Person;
import armadillo.models.Task;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.TreeSet;

public class TaskControllerTest {

    @Mock
    Database database;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testWhenTaskStartsInThePast() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        TaskController.Pair<Boolean, String> result = TaskController.taskValid(new TreeSet<Task>(), new TreeSet<Person>(), 4L, 10000);
        assertEquals(false, result._1);
        assertEquals("Task must start in the future", result._2);
    }

    @Test
    public void testWhenTaskStartsAfterPrereq() throws SQLException, ClassNotFoundException, ElementDoesNotExistException {
        long dateTime1 = LocalDateTime.of(2018, 02, 03, 02, 02).toEpochSecond(ZoneOffset.UTC);
        long dateTime2 = LocalDateTime.of(2018, 02, 03, 02, 03).toEpochSecond(ZoneOffset.UTC);
        Task t = Mockito.mock(Task.class);
        Mockito.when(t.getEffortEstimate()).thenReturn(10000L);
        Mockito.when(t.getDateTime()).thenReturn(dateTime1);
        Set<Task> tasks = new TreeSet<>();
        tasks.add(t);
        TaskController.Pair<Boolean, String> result = TaskController.taskValid(tasks, new TreeSet<Person>(), dateTime2, 10000);
        assertEquals(false, result._1);
        assertEquals("Task must start after prerequisite tasks have finished", result._2);
    }

    @Test
    public void testWhenPeopleHaveTasksAtSameTime() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        long dateTime1 = LocalDateTime.of(2018, 02, 03, 02, 02).toEpochSecond(ZoneOffset.UTC);
        long dateTime2 = LocalDateTime.of(2018, 02, 03, 02, 03).toEpochSecond(ZoneOffset.UTC);
        Task t = Mockito.mock(Task.class);
        Mockito.when(t.getDateTime()).thenReturn(dateTime1 + 30).thenReturn(dateTime1 + 30).thenReturn(dateTime1);
        Mockito.when(t.getEffortEstimate()).thenReturn(30L).thenReturn(1000000L).thenReturn(120L);
        Person p = Mockito.mock(Person.class);
        TreeSet<Task> tasks = new TreeSet<>();
        tasks.add(t);
        Mockito.when(p.getTasks()).thenReturn(tasks);
        Set<Person> people = new TreeSet<>();
        people.add(p);
        TaskController.Pair<Boolean, String> result = TaskController.taskValid(new TreeSet<Task>(),people, dateTime1, 120);
        assertEquals(false, result._1);
        assertEquals("People can only work on one task at a time", result._2);
        result = TaskController.taskValid(new TreeSet<Task>(),people, dateTime1, 120);
        assertEquals(false, result._1);
        assertEquals("People can only work on one task at a time", result._2);
        result = TaskController.taskValid(new TreeSet<Task>(),people, dateTime2, 10);
        assertEquals(false, result._1);
        assertEquals("People can only work on one task at a time", result._2);
    }

    @Test
    public void testWhenEmpty() throws SQLException, ClassNotFoundException, ElementDoesNotExistException{
        long dateTime1 = LocalDateTime.of(2018, 02, 03, 02, 02).toEpochSecond(ZoneOffset.UTC);
        TaskController.Pair<Boolean, String> result = TaskController.taskValid(new TreeSet<Task>(), new TreeSet<Person>(), dateTime1, 120);
        assertEquals(true, result._1);
        assertNull(result._2);
    }
}
