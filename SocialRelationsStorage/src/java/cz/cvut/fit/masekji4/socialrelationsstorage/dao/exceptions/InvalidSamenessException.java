package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * A <code>InvalidSamenessException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidSamenessException extends Exception
{

    /**
     * Creates a new instance of <code>InvalidSamenessException</code> without detail message.
     */
    public InvalidSamenessException()
    {
    }

    /**
     * Constructs an instance of <code>InvalidSamenessException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidSamenessException(String msg)
    {
        super(msg);
    }
}
