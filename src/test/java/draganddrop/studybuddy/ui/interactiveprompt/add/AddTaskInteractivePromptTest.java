package draganddrop.studybuddy.ui.interactiveprompt.add;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AddTaskInteractivePromptTest {

/*    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        assertEquals("Please enter the task name.", prompt.interact("add"));
    }*/

    @Test
    public void interact_quitCommand_returnMessage() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        assertEquals(AddTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    /*
    @Test
    public void interact_backCommand_returnMessage() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        assertEquals("Please enter the task name.", prompt.interact("back"));
    }*/

    /*
    @Test
    public void interact_secondInput_returnPrompt() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        prompt.interact("add");
        String userInput = AddTaskCommandParser.parseName("1");
        String reply = "The name of task is set to: " + userInput + ".\n"
                + "Please choose the task type:\n"
                + TaskType.getTypeString();
        assertEquals(reply, prompt.interact("1"));
    }


    @Test
    public void interact_thirdInput_returnPromptAss() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        prompt.interact("add");
        prompt.interact("1");
        TaskType taskType = AddTaskCommandParser.parseType("1", TaskType.getTaskTypes().length);
        String reply = "The type of task is set to: " + taskType.toString() + ".\n"
                + "Please enter the deadline with format: "
                + "HH:mm dd/MM/yyyy";
        assertEquals(reply, prompt.interact("1"));
    }

    @Test
    public void interact_thirdInput_returnPromptOthers() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        prompt.interact("add");
        prompt.interact("1");
        TaskType taskType = AddTaskCommandParser.parseType("2", TaskType.getTaskTypes().length);
        String reply = "The type of task is set to: " + taskType.toString() + ".\n"
                + "Please enter the deadline with format: "
                + "HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy";
        assertEquals(reply, prompt.interact("2"));
    }
     */

    /*
    @Test
    public void interact_thirdInput_returnPromptError() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        prompt.interact("add");
        prompt.interact("1");
        Assertions.assertEquals(new AddTaskCommandException("wrongIndexFormat").getMessage(), prompt.interact("10"));
    }*/

    /*
    @Test
    public void interact_fourthInput_returnPromptAss() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        prompt.interact("add");
        prompt.interact("1");
        prompt.interact("1");
        String userInput = "00:00 31/12/2020";
        LocalDateTime[] dateTimes = AddTaskCommandParser.parseDateTime(userInput, TaskType.Assignment);
        userInput = TimeParser.getDateTimeString(dateTimes[0]);
        String reply = "The date and time is set to: " + userInput + "\n"
                + "Press enter again to add the task:\n"
                + "1" + " " + TaskType.Assignment.toString() + " "
                + "00:00 31/12/2020";
        assertEquals(reply, prompt.interact("00:00 31/12/2020"));
    }*/

    /*
    @Test
    public void interact_fourthInput_returnPromptOther() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        prompt.interact("add");
        prompt.interact("");
        prompt.interact("1");
        prompt.interact("2");
        String userInput = "12:00 31/12/2020-14:00 31/12/2020";
        LocalDateTime[] dateTimes = AddTaskCommandParser.parseDateTime(userInput, TaskType.Quiz);
        userInput = TimeParser.getDateTimeString(dateTimes[0])
                + "-" + TimeParser.getDateTimeString(dateTimes[1]);
        String reply = "The date and time is set to: " + userInput + "\n"
                + "Press enter again to add the task:\n"
                + "1" + " " + TaskType.Quiz.toString() + " "
                + "12:00 31/12/2020-12:00 31/12/2020";
        assertEquals(reply, prompt.interact("12:00 31/12/2020-14:00 31/12/2020"));
    }
    */

/*    @Test
    public void interact_fifthInput_returnPrompt() {
        AddTaskInteractivePrompt prompt = new AddTaskInteractivePrompt();
        prompt.interact("add");
        prompt.interact("1");
        prompt.interact("1");
        prompt.interact("00:00 31/12/2020");
        assertThrows(NullPointerException.class, ()->prompt.interact(""));
    }*/

}
