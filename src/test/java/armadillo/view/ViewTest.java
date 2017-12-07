package armadillo.view;

import armadillo.views.View;
import com.athaydes.automaton.FXApp;
import com.athaydes.automaton.FXer;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import org.junit.Before;
import org.junit.Test;

import static com.athaydes.automaton.assertion.AutomatonMatcher.hasText;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static org.hamcrest.MatcherAssert.assertThat;


public class ViewTest {

    private FXer fxer;

    @Before
    public void setup() {
        FXApp.startApp( new View());
        fxer = FXer.getUserWith( FXApp.getScene().getRoot() );

           testNormalUseOfEnterTask();
//        testNormalAddPeople();
//        testNormalAddResources();


    }


    @Test
    public void testNormalUseOfEnterTask() {


        fxer.doubleClickOn("text:Enter Task").waitForFxEvents()
                .pause(1000)
                .clickOn("#taskField")
                .pause(200)
                .type("Test task field")
                .pause(200)

                .clickOn("#taskDescArea")
                .pause(200)
                .type("Test description area")
                .pause(200)

                .clickOn("#spinnerHours")
                .pause(200)
                .type(VK_BACK_SPACE)
                .type("3")
                .pause(200)
                .clickOn("#spinnerMins")
                .pause(200)
                .type(VK_BACK_SPACE)
                .type("30")
                .pause(200)

//                .moveTo("#listOfTasks")
//                .pause(500)
                
                .clickOn(((CheckBox)fxer.getAll( CheckBox.class ).get( 1 ))).waitForFxEvents()
                .pause(500)
                .clickOn(((CheckBox)fxer.getAll( CheckBox.class ).get( 3 ))).waitForFxEvents()
                .pause(500)
                .clickOn(((CheckBox)fxer.getAll( CheckBox.class ).get( 5 ))).waitForFxEvents()
                .pause(500)

                .clickOn("#submitButton")
                .pause(500)

                .clickOn((Button)fxer.getAll("text:Delete").get(1)).waitForFxEvents()
                .pause(500)

                .clickOn("#closeButton").waitForFxEvents()
                .pause(1000);




        assertThat( fxer.getAt( "taskLabel" ), hasText( "Task Name:" ) );
        assertThat( fxer.getAt( "taskDescription" ), hasText( "Task Description:" ) );
        assertThat( fxer.getAt( "spinnerLabelHours" ), hasText( "Hours:" ) );
        assertThat( fxer.getAt( "spinnerLabelMins" ), hasText( "Minutes:" ) );
        assertThat( fxer.getAt( "personInstructions" ), hasText( "Select people to assign to task." ) );
        assertThat( fxer.getAt( "taskInstructions" ), hasText( "Select prerequisite tasks or delete pre-existing tasks." ) );
        assertThat( fxer.getAt( "resourcesInstructions" ), hasText( "Enter required resources." ) );
        assertThat( fxer.getAt( "effortLabel" ), hasText( "Effort Estimate:" ) );
        assertThat( fxer.getAt( "submit" ), hasText( "Submit" ) );
        assertThat( fxer.getAt( "close" ), hasText( "Close" ) );
        assertThat( fxer.getAt( "button" ), hasText( "Delete" ) );


    }



    @Test
    public void testNormalAddPeople(){

        fxer.doubleClickOn("text:Add People").waitForFxEvents()
                .pause(1000)
                .clickOn("#nameField")
                .pause(500)
                .type("John")
                .pause(500)
                .clickOn("#surnameField")
                .pause(500)
                .type("Smith")
                .pause(500)
                .clickOn("#submitPeopleButton")
                .pause(500)

                .clickOn("#nameField")
                .pause(500)
                .type("George")
                .pause(500)
                .clickOn("#surnameField")
                .pause(500)
                .type("Taylor")
                .pause(500)
                .clickOn("#submitPeopleButton")
                .pause(500)

                .clickOn("#nameField")
                .pause(500)
                .type("David")
                .pause(500)
                .clickOn("#surnameField")
                .pause(500)
                .type("Williams")
                .pause(500)
                .clickOn("#submitPeopleButton")
                .pause(500)

                .clickOn((Button)fxer.getAll("text:Delete").get(1)).waitForFxEvents()
                .pause(500)

                .clickOn("#closeButton").waitForFxEvents()
                .pause(1000);

        assertThat( fxer.getAt( "nameLabel" ), hasText( "Enter Name: " ) );
        assertThat( fxer.getAt( "surnameLabel" ), hasText( "Enter Surname: " ) );
        assertThat( fxer.getAt( "submit" ), hasText( "Submit" ) );
        assertThat( fxer.getAt( "close" ), hasText( "Close" ) );
        assertThat( fxer.getAt( "button" ), hasText( "Delete" ) );

    }


    @Test
    public void testNormalAddResources(){


        fxer.doubleClickOn("text:Add Resources").waitForFxEvents()
                .pause(1000)
                .clickOn("#resourceField")
                .pause(500)
                .type("Test Resource #1")
                .pause(500)
                .clickOn("#submitResourceButton")
                .pause(500)

                .clickOn("#resourceField")
                .pause(500)
                .type("Test Resource #2")
                .pause(500)
                .clickOn("#submitResourceButton")
                .pause(500)

                .clickOn((Button)fxer.getAll("text:Delete").get(0)).waitForFxEvents()
                .pause(500)

                .clickOn("#closeButton").waitForFxEvents()
                .pause(1000);


        assertThat( fxer.getAt( "resourceLabel" ), hasText( "Enter Resource: " ) );
        assertThat( fxer.getAt( "submit" ), hasText( "Submit" ) );
        assertThat( fxer.getAt( "close" ), hasText( "Close" ) );
        assertThat( fxer.getAt( "button" ), hasText( "Delete" ) );


    }


}