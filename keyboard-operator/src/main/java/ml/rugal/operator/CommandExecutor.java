package ml.rugal.operator;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import ml.rugal.operator.commandSpec.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rugal Bernstein
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

    public void execute(Command order) throws AWTException, InterruptedException
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
        //verify command
        //LATER parse stage
        //execute
        order.executeCommand(robot, history);
        history.add(order);

//        Thread.sleep(1000);
    }
}
