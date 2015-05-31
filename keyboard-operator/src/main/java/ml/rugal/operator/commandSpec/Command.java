package ml.rugal.operator.commandSpec;

import java.awt.Robot;
import java.util.List;

/**
 * This is the abstract base for all Command in program.
 * Best using the {@code CommandFactory} to instantiate a valid command instead
 * of doing so by yourself.
 *
 * @author Rugal Bernstein
 * @since 0.1
 */
public abstract class Command
{
//    private static final Logger LOG = LoggerFactory.getLogger(Command.class.getName());

    protected String[] parameters = null;

    Command()
    {
    }

    public String[] getParameters()
    {
        return parameters;
    }

    /**
     * This method used for initializing a command. Since different commands use
     * different key words and parameter elements. Initialization in here so we
     * could do any work in same code.
     * This method must be implemented. Also must be well initialized to ensure
     * the command will be executed correctly.
     *
     * @param textCommand Usually the first element is command key work, the
     *                    rest of parameters are specific to different commands.
     */
    public abstract void init(String[] textCommand);

    /**
     * Command will be executed in real when invoking this method.
     * history provided for reading historical command to do work like redo and
     * undo.
     *
     * @param robot   A robot instance to execute in real.
     * @param history a list of commands that have been executed.
     */
    public abstract void executeCommand(final Robot robot, final List<Command> history);
}
