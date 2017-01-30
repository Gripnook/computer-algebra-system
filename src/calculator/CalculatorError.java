package calculator;

/**
 * An exception thrown by the classes in the package calculator.
 * 
 * @author Andrei Purcarus
 *
 */
public class CalculatorError extends Exception
{

    /**
     * Returns the error that caused the exception.
     * 
     * @return the error that caused the exception.
     */
    public ErrorType getError()
    {
        return this.error;
    }

    /**
     * Initializes the exception with a value of error.
     * 
     * @param error
     *            - the error obtained that caused the exception to be thrown.
     */
    public CalculatorError(ErrorType error)
    {
        this.error = error;
    }

    private static final long serialVersionUID = 4100647516714602954L;

    /**
     * The type of error the exception is thrown with.
     */
    private ErrorType error = ErrorType.none;

}
