package ml.rugal.operator;

import ml.rugal.operator.commandSpec.Command;
import ml.rugal.operator.commandSpec.CommandFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Rugal Bernstein
 */
public class CommandExecutorTest
{

    public CommandExecutorTest()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    @Ignore
    public void testExecute() throws Exception
    {
        System.out.println("execute");
        CommandExecutor instance = new CommandExecutor();
        String[] args = new String[]
        {
            "Move", "down", "5"
        };

        Command order = CommandFactory.constructCommand(args);
        instance.execute(order);

        args = new String[]
        {
            "Move", "left", "3"
        };

        order = CommandFactory.constructCommand(args);
        instance.execute(order);
    }

}
