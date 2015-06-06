package ml.rugal.operator.commandSpec;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command for move on screen.<BR>
 * Format below are allowed:<BR>
 * 1. &lt; key-word &gt; direction [step]<BR>
 * 2. go to direction [step]
 * 3. direction [step]<BR>
 *
 * @author Rugal Bernstein
 * @since 0.1
 */
public class MoveCommand extends Command
{

    private static final String GO = "go";

    private static final String G2 = "02";

    private static final String GHOST = "ghost";

    private static final String GOLDEN = "golden";

    private static final String DOG = "dog";

    private static final String BOW = "bow";

    private static final String CALL = "call";

    private static final String GOLF = "golf";

    private static final String BOOST = "boost";

    private static final String OOVOO = "oovoo";

    private static final String MOVE = "move";

    private static final String GOTO = "goto";

    private static final String TO = "to";

    private static final String WHO = "who";

    private static final String BOOB = "boob";

    private static final String GOOF = "goof";

    private static final String UV = "uv";

    private static final String MOVIE = "movie";

    private static final String WOLF = "wolf";

    public static String[] COMMAND_MAP =
    {

        //GO
        GO, GOLF, GOOF, CALL, DOG, BOW, GOLDEN, GHOST,
        //MOVEG2,
        MOVE, WHO, BOOB, MOVIE, UV, BOOST, OOVOO, WOLF,
        //GOTO
        G2, GOTO
    };

    private static final Logger LOG = LoggerFactory.getLogger(MoveCommand.class
        .getName());

    private Move direction;

    private int step = 1;

    /**
     * Get a move command with specified direction
     * <p>
     * @param move specified direction
     */
    public MoveCommand(Move move)
    {
        this.direction = move;
    }

    /**
     *
     */
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
            //This if statement is for case. 2
            if (textCommand[0].equalsIgnoreCase(GO) && textCommand[1].equalsIgnoreCase(TO))
            {
                directionIndex = 2;
            }
        }

        this.direction = Move.valueOf(textCommand[directionIndex].toUpperCase());
        if (textCommand.length <= directionIndex + 1)
        {
            LOG.debug("No step parameter, using default step length: " + this.step);
            return;
        }
        //TODO What if it is English word?
        //But I find google speech API could convert number word to digit number automatically in most of the case,
        //thus here I do not need to do the convertion myself.
        this.step = Integer.parseInt(textCommand[directionIndex + 1]);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void executeCommand(Robot robot, List<Command> history)
    {
        LOG.debug("Button pressing " + KeyEvent.getKeyText(this.direction.getKeyEvent()) + " " + step + " time(s)");
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

    /**
     * This enumerator defines some directions used in moving, up till 0.1 we
     * have up, down, left and right directions.
     * Those duplicated words that sound similar are for the ambiguous process
     * for higher reflection accuracy.
     * <p>
     * For instance AT are the ambiguous word for UP.
     * <p>
     * @since 0.1
     */
    public enum Move
    {

        //UP
        UP(KeyEvent.VK_UP), AT(KeyEvent.VK_UP), ART(KeyEvent.VK_UP), OFF(KeyEvent.VK_UP), OF(KeyEvent.VK_UP), APP(KeyEvent.VK_UP), FUCK(KeyEvent.VK_UP), BAT(KeyEvent.VK_UP), OP(KeyEvent.VK_UP),
        //DOWN
        DOWN(KeyEvent.VK_DOWN), DONE(KeyEvent.VK_DOWN), BANK(KeyEvent.VK_DOWN),
        //LEFT
        LEFT(KeyEvent.VK_LEFT), LOFT(KeyEvent.VK_LEFT), LIVE(KeyEvent.VK_LEFT), LEAVE(KeyEvent.VK_LEFT), LAST(KeyEvent.VK_LEFT), LAUGH(KeyEvent.VK_LEFT),
        //RIGHT
        RIGHT(KeyEvent.VK_RIGHT), LIGHT(KeyEvent.VK_RIGHT), WRITE(KeyEvent.VK_RIGHT), WRIGHT(KeyEvent.VK_RIGHT), WHITE(KeyEvent.VK_RIGHT),;

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
