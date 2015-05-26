package ml.rugal.operator.commandSpec;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command for move on screen.
 *
 * @author Rugal Bernstein
 */
public class MoveCommand extends Command
{

    private static final Logger LOG = LoggerFactory.getLogger(MoveCommand.class.getName());

    private Move move;

    private int step = 1;

    public MoveCommand(Move move)
    {
        this.move = move;
    }

    protected MoveCommand()
    {
    }

    /**
     * Here move command only allow several directions of move, this will affect
     * behavior in motion. Moving steps is in third element if defined.
     *
     * @param textCommand
     */
    @Override
    public void init(String[] textCommand)
    {
        this.move = Move.valueOf(textCommand[1].toUpperCase());
        if (textCommand.length < 3)
        {
            LOG.debug("Less than 3 parameters");
            return;
        }
        this.step = Integer.parseInt(textCommand[2]);
    }

    @Override
    public void executeCommand(Robot robot, List<Command> history)
    {
        LOG.debug("Button pressing " + this.move.toString() + " " + step + " time(s)");
        for (int i = 0; i < step; i++)
        {
            robot.keyPress(this.move.getKeyEvent());
        }
    }

    public enum Move
    {

        UP(KeyEvent.VK_UP), DOWN(KeyEvent.VK_DOWN), LEFT(KeyEvent.VK_LEFT), RIGHT(KeyEvent.VK_RIGHT);

        private final int keyEvent;

        private Move(int keyEvent)
        {
            this.keyEvent = keyEvent;
        }

        public int getKeyEvent()
        {
            return keyEvent;
        }

    }

}
