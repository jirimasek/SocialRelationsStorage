package cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions;

/**
 * A <code>InvalidPersonException</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class InvalidPersonException extends Exception
{

    /**
     * Creates a new instance of <code>InvalidPersonException</code> without detail message.
     */
    public InvalidPersonException()
    {
    }

    /**
     * Constructs an instance of <code>InvalidPersonException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidPersonException(String msg)
    {
        super(msg);
    }
}
