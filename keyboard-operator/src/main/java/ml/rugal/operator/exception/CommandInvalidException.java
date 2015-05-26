package ml.rugal.operator.exception;

/**
 * Throw if command is not valid, that means the command parameters may includes
 * some bad values.
 *
 * @author Rugal Bernstein
 */
public class CommandInvalidException extends Exception
{

    public CommandInvalidException(Throwable thrwbl)
    {
        super(thrwbl);
    }

}
