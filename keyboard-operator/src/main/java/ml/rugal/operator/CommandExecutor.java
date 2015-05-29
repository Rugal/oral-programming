package ml.rugal.operator;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import ml.rugal.operator.commandSpec.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the final command executor. Each command must be executed in here.
 * This class not only provide stand execution robot but also have a command
 * history to retrieve historical commands and parameters whenever needed.
 *
 * @author Rugal Bernstein
 * @since 0.1
 */
public class CommandExecutor
{

    private static final Logger LOG = LoggerFactory.getLogger(CommandExecutor.class.getName());

// Command history
    private final List<Command> history = new ArrayList<>();

    private Robot robot;

    public CommandExecutor()
    {

    }

    /**
     * Method to execute an command.
     * Some command pre-check work such as privilege thing must be done in here.
     *
     * @param order
     *
     * @throws AWTException if robot can not be instantiated
     */
    public void execute(Command order) throws AWTException
    {
        if (null == robot)
        {
            try
            {
                robot = new Robot();
            }
            catch (AWTException ex)
            {
                LOG.error("Unable to get robot instance", ex);
                throw ex;
            }
        }
        //TODO
        //verify command
        //LATER parse stage
        //execute
        order.executeCommand(robot, history);
        history.add(order);
    }
}
