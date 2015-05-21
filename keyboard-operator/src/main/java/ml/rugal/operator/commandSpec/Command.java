package ml.rugal.operator.commandSpec;

import java.awt.Robot;
import java.util.List;

/**
 *
 * @author Rugal Bernstein
 */
public abstract class Command
{

    public static Command verifyCommand(String[] textCommand) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = Command.class.getName().replaceAll("Command", textCommand[0] + "Command");
        Command command = (Command) Class.forName(className).newInstance();
        //some internal verification must be done here
        //method below will initialize the command
        command.setOperation(textCommand);
        return command;
    }

    protected String[] parameters = null;

    public Command()
    {
    }

    public String[] getParameters()
    {
        return parameters;
    }

    public abstract void setOperation(String[] textCommand);

    public abstract void executeCommand(final Robot robot, final List<Command> history);

}
