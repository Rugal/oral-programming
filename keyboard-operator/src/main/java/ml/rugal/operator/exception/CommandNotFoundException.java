package ml.rugal.operator.exception;

/**
 * Throw if the command key word is not found or not yet defined.
 *
 * @author Rugal Bernstein
 */
public class CommandNotFoundException extends Exception
{

    public CommandNotFoundException()
    {
    }

    public CommandNotFoundException(String string)
    {
        super(string);
    }

    public CommandNotFoundException(Throwable thrwbl)
    {
        super(thrwbl);
    }

}
