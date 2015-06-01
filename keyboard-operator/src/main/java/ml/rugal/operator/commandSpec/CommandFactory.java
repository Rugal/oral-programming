package ml.rugal.operator.commandSpec;

import java.util.HashMap;
import java.util.Map;
import ml.rugal.operator.exception.CommandInvalidException;
import ml.rugal.operator.exception.CommandNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This factory class is used to create a valid command. This involves command
 * key word reflection and command initialization.
 *
 * @author Rugal Bernstein
 * @since 0.1
 */
public class CommandFactory
{

    private static final Logger LOG = LoggerFactory.getLogger(CommandFactory.class.getName());

    private static final Map<String, Class<? extends Command>> commandMap = new HashMap<>();

    static
    {
        for (String map : MoveCommand.COMMAND_MAP)
        {
            commandMap.put(map, MoveCommand.class);
        }
        for (String map : EditCommand.COMMAND_MAP)
        {
            commandMap.put(map, EditCommand.class);
        }
    }

    /**
     * This static method used to create valid command from String[] by
     * validating and reflecting it.
     * Some pre-check work such as validation and verification must be done
     * here.
     *
     *
     * @param textCommand this parameter is user specified. generally speaking
     *                    the first element of the string is the command key
     *                    word like "Move" or "Edit". Please ensure those key word
     *                    is valid as this method will not do the validation work for key word.
     *
     * @return give a valid command if every parameters turn to be correct for
     *         specific command requirement.
     *
     * @throws CommandNotFoundException usually because command key word is
     *                                  invalid.
     * @throws CommandInvalidException  use this exception to represent for the
     *                                  reflection issues like accessibility
     *                                  and instantiation problems.
     */
    public static Command constructCommand(String[] textCommand) throws CommandNotFoundException, CommandInvalidException
    {
        String commandText = textCommand[0].toLowerCase();
        try
        {
            if (!commandMap.containsKey(commandText))
            {
                throw new CommandNotFoundException("The command word <" + commandText + "> is not found");
            }
            //These are several internal used only Command class to be reflected.
            //TODO I plan to use Spring to do IoC instead of doing so by adding class file to my package.
            Class<? extends Command> clazz = commandMap.get(commandText);
            Command command = clazz.newInstance();
            LOG.debug("Command " + clazz.getSimpleName() + " with key word <" + commandText + "> is received");
            LOG.debug(parameterLog(textCommand));
            //TODO some internal verification must be done here
            //method below will initialize the command
            command.init(textCommand);
            LOG.debug("Command " + clazz.getSimpleName() + " initialization completed");
            return command;
        }
        catch (CommandNotFoundException ex)
        {
            LOG.error("The command word <" + commandText + "> is not found, please check command word table for correct spell");
            throw ex;
        }
        catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.error("Unable to instantiate this command: " + commandText + "Command", ex);
            throw new CommandInvalidException(ex);
        }
    }

    /**
     * Just for clear code
     * <p>
     * @param textCommand
     *                    <p>
     * @return
     */
    private static String parameterLog(String[] textCommand)
    {
        StringBuilder sb = new StringBuilder("Parameter includes:");
        for (String text : textCommand)
        {
            sb.append("\n").append("   ").append(text);
        }
        return sb.toString();
    }

    private CommandFactory()
    {
    }

}
