package ml.rugal.operator.commandSpec;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command for move on screen.<BR/>
 * Format below are allowed:<BR/>
 * 1. &lt; key-word &gt; direction [step]<BR/>
 * 2. direction [step]<BR/>
 *
 * @author Rugal Bernstein
 * @since 0.1
 */
public class MoveCommand extends Command
{

    public static String[] COMMAND_MAP =
    {
        "move", "go", "goto"
    };

    private static final Logger LOG = LoggerFactory.getLogger(MoveCommand.class.getName());

    private Move direction;

    private int step = 1;

    public MoveCommand(Move move)
    {
        this.direction = move;
    }

    protected MoveCommand()
    {
    }

    /**
     * Here move command only allows moving towards several directions, this
     * will decide motion behavior.
     *
     * @param textCommand the complete command text String
     */
    @Override
    public void init(String[] textCommand)
    {
        int directionIndex = 0;
        if (isKeyWord(textCommand[0]))
        {
            directionIndex = 1;
        }

        this.direction = Move.valueOf(textCommand[directionIndex].toUpperCase());
        if (textCommand.length <= directionIndex + 1)
        {
            LOG.debug("No step parameter, using default step length: " + this.step);
            return;
        }
        //What if it is English word?
        //TODO parse English word number to digit number
        this.step = Integer.parseInt(textCommand[directionIndex + 1]);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void executeCommand(Robot robot, List<Command> history)
    {
        LOG.debug("Button pressing " + this.direction.toString() + " " + step + " time(s)");
        for (int i = 0; i < step; i++)
        {
            robot.keyPress(this.direction.getKeyEvent());
        }
    }

    private boolean isKeyWord(String word)
    {
        boolean value = false;
        for (String keyword : COMMAND_MAP)
        {
            if (word.equalsIgnoreCase(keyword))
            {
                value = true;
                break;
            }
        }
        return value;
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
