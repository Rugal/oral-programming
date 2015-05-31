package ml.rugal.operator.commandSpec;

import java.awt.Robot;
import java.util.List;

/**
 *
 * @author Rugal Bernstein
 */
public class ControlCommand extends Command
{

    public static String[] COMMAND_MAP =
    {
        "do", "repeat", "redo", "undo", "save", "close", "again"
    };

    @Override
    public void init(String[] textCommand)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void executeCommand(Robot robot, List<Command> history)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
