package ml.rugal.operator.commandSpec;

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
        //These are several internal used only Command class to be reflected.
        //I plan to use Spring to do IoC instead of doing so by adding class file to my package.
        String className = Command.class.getName().replaceAll("Command", textCommand[0] + "Command");
        Command command;
        try
        {
            //TODO
            //some internal verification must be done here
            //method below will initialize the command
            command = (Command) Class.forName(className).newInstance();
            command.init(textCommand);
            return command;
        }
        catch (ClassNotFoundException ex)
        {
            LOG.error("The command word <" + textCommand[0] + "> is invalid, please check command word table for correct spell");
            throw new CommandNotFoundException(ex);
        }
        catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.error("Unable to instantiate this command object: " + textCommand[0] + "Command", ex);
            throw new CommandInvalidException(ex);
        }
    }

    private CommandFactory()
    {
    }

}
