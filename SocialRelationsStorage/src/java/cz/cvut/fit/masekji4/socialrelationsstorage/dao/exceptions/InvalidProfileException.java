package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * A <code>InvalidProfileException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidProfileException extends Exception
{

    /**
     * Creates a new instance of <code>InvalidProfileException</code> without detail message.
     */
    public InvalidProfileException()
    {
    }

    /**
     * Constructs an instance of <code>InvalidProfileException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidProfileException(String msg)
    {
        super(msg);
    }
}
